package com.sicobo.sicobo.serviceimpl;

import com.sicobo.sicobo.model.BeanWarehouse;
import com.sicobo.sicobo.model.BeanWarehouseImage;
import com.sicobo.sicobo.service.IStripeService;
import com.sicobo.sicobo.util.Message;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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

    @Value("${stripe.secret.key}")
    private String stripeSecretKey;

    @Override
    public ResponseEntity<Object> checkout(BeanWarehouse beanWarehouse) {
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
                                put("name", "Bodega");
                                put("description", beanWarehouse.getDescription());
                                put("images", images);
                            }});
                            put("unit_amount_decimal", beanWarehouse.getFinalCost() * 100);
                        }});
                        put("quantity", 1);
                    }}
            ));
            params.put("success_url", "http://localhost:8080/informacionPago");
            params.put("cancel_url", "http://localhost:8080/detalleProducto/" + beanWarehouse.getId());
            params.put("mode", "payment");

            Session session = Session.create(params);
            return new ResponseEntity<>(new Message(SUCCESSFUL_SEARCH,SEARCH_SUCCESSFUL, SUCCESS,SUCCESS_CODE, session), HttpStatus.OK);
        } catch (StripeException e) {
            log.error("Ocurrio un error en PoliciesServiceImpl - guardar" + e.getMessage());
            return new ResponseEntity<>(new Message(FAILED_EXECUTION,INTERNAL_ERROR, FAILED,SERVER_FAIL_CODE, null), HttpStatus.INTERNAL_SERVER_ERROR);

        }

    }

    @Override
    public ResponseEntity<Object> paymentIntent() {
        return null;
    }
}
