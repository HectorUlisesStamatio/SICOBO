package com.sicobo.sicobo.serviceimpl;

import com.sicobo.sicobo.dao.DaoPayment;
import com.sicobo.sicobo.dao.DaoWarehouse;
import com.sicobo.sicobo.dto.DTOPayment;
import com.sicobo.sicobo.model.BeanPayment;
import com.sicobo.sicobo.model.BeanUser;
import com.sicobo.sicobo.model.BeanWarehouse;
import com.sicobo.sicobo.model.BeanWarehouseImage;
import com.sicobo.sicobo.service.IStripeService;
import com.sicobo.sicobo.util.Message;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.Refund;
import com.stripe.model.checkout.Session;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.*;

import static com.sicobo.sicobo.util.Constantes.MessageBody.*;
import static com.sicobo.sicobo.util.Constantes.MessageCodes.SERVER_FAIL_CODE;
import static com.sicobo.sicobo.util.Constantes.MessageCodes.SUCCESS_CODE;
import static com.sicobo.sicobo.util.Constantes.MessageHeaders.*;
import static com.sicobo.sicobo.util.Constantes.MessageType.FAILED;
import static com.sicobo.sicobo.util.Constantes.MessageType.SUCCESS;


@Service
@Slf4j
public class StripeServiceImpl implements IStripeService {

    @Autowired
    private PaymentServiceImpl paymentService;

    @Autowired
    private WarehouseServiceImpl warehouseService;

    @Autowired
    private DaoPayment daoPayment;

    @Autowired
    private DaoWarehouse daoWarehouse;

    @Value("${stripe.secret.key}")
    private String stripeSecretKey;

    @Override
    public ResponseEntity<Object> checkout(BeanWarehouse beanWarehouse, int meses) {
        Stripe.apiKey = stripeSecretKey;
        List<String> images = new ArrayList<>();
        for (BeanWarehouseImage image:
             beanWarehouse.getImages()) {
            images.add(image.getSecureUrl());
        }
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("payment_method_types", Collections.singletonList("card"));
            params.put("line_items", Collections.singletonList(
                    new HashMap<String, Object>() {{
                        put("price_data", new HashMap<String, Object>() {{
                            put("currency", "mxn");
                            put("product_data", new HashMap<String, Object>() {{
                                put("name", beanWarehouse.getSection());
                                put("description", beanWarehouse.getDescription());
                                put("images", images);
                            }});
                            put("unit_amount_decimal", beanWarehouse.getFinalCost() * 100);
                        }});
                        put("quantity", 1);
                    }}
            ));
            beanWarehouse.setDescription("");
            params.put("success_url", "http://localhost:8080/usuario/estadoPago");
            params.put("cancel_url", "http://localhost:8080/usuario/detalleProducto/" + beanWarehouse.getId());
            params.put("mode", "payment");
            params.put("metadata", new HashMap<String, Object>() {{
                put("idWarehouse", beanWarehouse.getId());
                put("meses", meses);
            }});

            Session session = Session.create(params);
            return new ResponseEntity<>(new Message(SUCCESSFUL_SEARCH,SEARCH_SUCCESSFUL, SUCCESS,SUCCESS_CODE, session), HttpStatus.OK);
        } catch (StripeException e) {
            log.error("Ocurrio un error en StripeServiceImpl - checkout" + e.getMessage());
            return new ResponseEntity<>(new Message(FAILED_EXECUTION,INTERNAL_ERROR, FAILED,SERVER_FAIL_CODE, null), HttpStatus.INTERNAL_SERVER_ERROR);

        }

    }

    @Override
    public ResponseEntity<Object> checkoutRenovation(BeanWarehouse beanWarehouse, int meses, Long idPayment) {
        Stripe.apiKey = stripeSecretKey;
        List<String> images = new ArrayList<>();
        for (BeanWarehouseImage image:
                beanWarehouse.getImages()) {
            images.add(image.getSecureUrl());
        }
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("payment_method_types", Collections.singletonList("card"));
            params.put("line_items", Collections.singletonList(
                    new HashMap<String, Object>() {{
                        put("price_data", new HashMap<String, Object>() {{
                            put("currency", "mxn");
                            put("product_data", new HashMap<String, Object>() {{
                                put("name", beanWarehouse.getSection() + " - (RENOVACIÓN)");
                                put("description", beanWarehouse.getDescription());
                                put("images", images);
                            }});
                            put("unit_amount_decimal", beanWarehouse.getFinalCost() * 100);
                        }});
                        put("quantity", 1);
                    }}
            ));
            beanWarehouse.setDescription("");
            params.put("success_url", "http://localhost:8080/usuario/estadoRenovacion");
            params.put("cancel_url", "http://localhost:8080/usuario/renovacionBodega/" + idPayment);
            params.put("mode", "payment");
            params.put("metadata", new HashMap<String, Object>() {{
                put("idWarehouse", beanWarehouse.getId());
                put("idPayment", idPayment);
                put("meses", meses);
            }});

            Session session = Session.create(params);
            return new ResponseEntity<>(new Message(SUCCESSFUL_SEARCH,SEARCH_SUCCESSFUL, SUCCESS,SUCCESS_CODE, session), HttpStatus.OK);
        } catch (StripeException e) {
            log.error("Ocurrio un error en StripeServiceImpl - checkout" + e.getMessage());
            return new ResponseEntity<>(new Message(FAILED_EXECUTION,INTERNAL_ERROR, FAILED,SERVER_FAIL_CODE, null), HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    @Override
    public ResponseEntity<Object> paymentIntent(Session session, BeanUser user){
        DTOPayment payment = new DTOPayment();
        Date currentDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        Integer idWarehouse = Integer.parseInt(session.getMetadata().get("idWarehouse"));

        try {
            Session sessionWarehouse = Session.retrieve(session.getId());
            idWarehouse = Integer.parseInt(sessionWarehouse.getMetadata().get("idWarehouse"));
            PaymentIntent paymentIntent = PaymentIntent.retrieve(sessionWarehouse.getPaymentIntent());

            if(!paymentIntent.getStatus().equals("succeeded")){
                return new ResponseEntity<>(new Message(FAILED_EXECUTION,"No se puedo realizar correctamente el pago", FAILED,SERVER_FAIL_CODE, idWarehouse), HttpStatus.INTERNAL_SERVER_ERROR);
            }

            payment.setAmount(Double.valueOf(paymentIntent.getAmount()));
            payment.setAmountMonths(Integer.parseInt(session.getMetadata().get("meses")));
            calendar.add(Calendar.MONTH, Integer.parseInt(session.getMetadata().get("meses")));
            payment.setBeanWarehouse(idWarehouse);
            payment.setBeanUser(user.getId());
            payment.setPaymentId(paymentIntent.getId());
            payment.setStatus(1);
            payment.setPaymentDate(currentDate);
            payment.setDueDate(calendar.getTime());



            ResponseEntity<?> responseRented = warehouseService.rentar(Long.parseLong(session.getMetadata().get("idWarehouse")));
            assert responseRented != null;
            Message messageRented = (Message) responseRented.getBody();
            assert messageRented != null;

            if (messageRented.getType().equals(FAILED)) {
                Map<String, Object> params = new HashMap<>();
                params.put("payment_intent", paymentIntent.getId());
                Refund refund = Refund.create(params);
                messageRented.setResult(idWarehouse);
                return new ResponseEntity<>(messageRented, HttpStatus.OK);
            }
            BeanWarehouse beanWarehouseResult = (BeanWarehouse) messageRented.getResult();
            assert  beanWarehouseResult != null;


            ResponseEntity<?> response = paymentService.guardar(payment);
            assert response != null;
            Message message = (Message) response.getBody();
            assert message != null;

            if (message.getType().equals(FAILED)) {
                message.setResult(idWarehouse);
                return new ResponseEntity<>(message, HttpStatus.OK);
            }
            BeanPayment beanPaymentResult = (BeanPayment) message.getResult();
            assert  beanPaymentResult != null;


            Map<String, Object> data = new HashMap<>();
            data.put("payment", beanPaymentResult);
            data.put("warehouse", beanWarehouseResult);




            return new ResponseEntity<>(new Message(SUCCESSFUL_REGISTRATION, "Renta de bodega exitosa", SUCCESS,SUCCESS_CODE, data), HttpStatus.OK);
        }catch (StripeException e){
            log.error("Ocurrio un error en StripeServiceImpl - paymentIntent" + e.getMessage());
            return new ResponseEntity<>(new Message(FAILED_EXECUTION,INTERNAL_ERROR, FAILED,SERVER_FAIL_CODE, idWarehouse), HttpStatus.INTERNAL_SERVER_ERROR);
        }catch (Exception e){
            log.error("Ocurrio un error en StripeServiceImpl - paymentIntent" + e.getMessage());
            return new ResponseEntity<>(new Message(FAILED_EXECUTION,INTERNAL_ERROR, FAILED,SERVER_FAIL_CODE, idWarehouse), HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }


    @Override
    public ResponseEntity<Object> paymentIntentRenovation(Session session, BeanUser user){
        Optional<BeanPayment> paymentSearched = daoPayment.findById(Long.parseLong(session.getMetadata().get("idPayment")));

        if(!paymentSearched.isPresent()){
            return new ResponseEntity<>(new Message(FAILED_EXECUTION,"No se ha encontrado el pago de renta", FAILED,SERVER_FAIL_CODE, null), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        DTOPayment payment = new DTOPayment();
        Date currentDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(paymentSearched.get().getDueDate());
        Integer idWarehouse = Integer.parseInt(session.getMetadata().get("idWarehouse"));

        try {
            Session sessionWarehouse = Session.retrieve(session.getId());
            idWarehouse = Integer.parseInt(sessionWarehouse.getMetadata().get("idWarehouse"));
            PaymentIntent paymentIntent = PaymentIntent.retrieve(sessionWarehouse.getPaymentIntent());

            if(!paymentIntent.getStatus().equals("succeeded")){
                return new ResponseEntity<>(new Message(FAILED_EXECUTION,"No se puedo realizar correctamente el pago", FAILED,SERVER_FAIL_CODE, idWarehouse), HttpStatus.INTERNAL_SERVER_ERROR);
            }

            payment.setAmount(Double.valueOf(paymentIntent.getAmount()));
            payment.setAmountMonths(Integer.parseInt(session.getMetadata().get("meses")));
            calendar.add(Calendar.MONTH, Integer.parseInt(session.getMetadata().get("meses")));
            payment.setBeanWarehouse(idWarehouse);
            payment.setBeanUser(user.getId());
            payment.setPaymentId(paymentIntent.getId());
            payment.setStatus(1);
            payment.setPaymentDate(currentDate);
            payment.setDueDate(calendar.getTime());


            ResponseEntity<?> response = paymentService.guardar(payment);
            assert response != null;
            Message message = (Message) response.getBody();
            assert message != null;

            if (message.getType().equals(FAILED)) {
                message.setResult(idWarehouse);
                return new ResponseEntity<>(message, HttpStatus.OK);
            }
            BeanPayment beanPaymentResult = (BeanPayment) message.getResult();
            assert  beanPaymentResult != null;

            Optional<BeanWarehouse> warehouse = daoWarehouse.findById(beanPaymentResult.getBeanWarehouse().getId());
            if(!warehouse.isPresent()){
                return new ResponseEntity<>(new Message(FAILED_EXECUTION,INTERNAL_ERROR, FAILED,SERVER_FAIL_CODE, null), HttpStatus.INTERNAL_SERVER_ERROR);
            }

            BeanPayment beanPaymentResponse = paymentSearched.get();
            beanPaymentResponse.setStatus(0);
            daoPayment.save(beanPaymentResponse);

            Map<String, Object> data = new HashMap<>();
            data.put("payment", beanPaymentResult);
            data.put("warehouse",warehouse.get());


            return new ResponseEntity<>(new Message(SUCCESSFUL_REGISTRATION, "Renovación de renta de bodega exitosa", SUCCESS,SUCCESS_CODE, data), HttpStatus.OK);
        }catch (StripeException e){
            log.error("Ocurrio un error en StripeServiceImpl - paymentIntent" + e.getMessage());
            return new ResponseEntity<>(new Message(FAILED_EXECUTION,INTERNAL_ERROR, FAILED,SERVER_FAIL_CODE, idWarehouse), HttpStatus.INTERNAL_SERVER_ERROR);
        }catch (Exception e){
            log.error("Ocurrio un error en StripeServiceImpl - paymentIntent" + e.getMessage());
            return new ResponseEntity<>(new Message(FAILED_EXECUTION,INTERNAL_ERROR, FAILED,SERVER_FAIL_CODE, idWarehouse), HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }
}
