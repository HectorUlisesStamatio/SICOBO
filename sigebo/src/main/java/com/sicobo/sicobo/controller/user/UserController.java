package com.sicobo.sicobo.controller.user;

import com.sicobo.sicobo.model.BeanPayment;
import com.sicobo.sicobo.model.BeanWarehouse;
import com.sicobo.sicobo.serviceimpl.PaymentServiceImpl;
import com.sicobo.sicobo.serviceimpl.StateServiceImpl;
import com.sicobo.sicobo.serviceimpl.WarehouseServiceImpl;
import com.sicobo.sicobo.util.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

import static com.sicobo.sicobo.util.Constantes.MessageType.FAILED;
import static com.sicobo.sicobo.util.Constantes.Redirects.*;
import static com.sicobo.sicobo.util.Constantes.Roles.ROLE_USUARIO;
import static com.sicobo.sicobo.util.Constantes.Stuff.*;
import static com.sicobo.sicobo.util.Constantes.ObjectMessages.*;

@Controller
@Slf4j
public class UserController {
    private final WarehouseServiceImpl warehouseService;

    private final StateServiceImpl stateService;

    private final PaymentServiceImpl paymentService;

    @Autowired
    public UserController(WarehouseServiceImpl warehouseService, StateServiceImpl stateService, PaymentServiceImpl paymentService) {
        this.warehouseService = warehouseService;
        this.stateService = stateService;
        this.paymentService = paymentService;
    }

    @Secured({ROLE_USUARIO})
    @GetMapping("/usuario/misBodegas")
    public String misBodegas(Model model) {
        model.addAttribute(OPTION, MYWAREHOUSE_OPTION);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Message responseStates = (Message) stateService.listar().getBody();
        assert responseStates != null;
        model.addAttribute(STATES, responseStates.getResult());
        Message warehousesClient = (Message) warehouseService.buscarBodegaPorUsername(auth.getName()).getBody();
        
        model.addAttribute(RESPONSE, warehousesClient.getResult());

        return MIS_BODEGAS;
    }

    @Secured({ROLE_USUARIO})
    @GetMapping("/usuario/invoice/{idPayment}")
    public String invoice(@PathVariable("idPayment") Optional<String> idPayment, Model model) {

        try{
            Message response = (Message) stateService.listar().getBody();
            assert response != null;
            model.addAttribute(STATES, response.getResult());
            try {
                Long id = Long.parseLong(idPayment.get());
                Message message = (Message) paymentService.buscar(id).getBody();
                assert message !=null;
                if(message.getType().equals(FAILED)){
                    model.addAttribute(MESSAGE, message);
                    return INVOICE;
                }
                BeanPayment beanPayment = (BeanPayment) message.getResult();
                assert  beanPayment != null;

                Message messageWarehouse = (Message) warehouseService.buscarPorId(beanPayment.getBeanWarehouse().getId()).getBody();
                assert  messageWarehouse != null;
                if(messageWarehouse.getType().equals(FAILED)){
                    model.addAttribute(MESSAGE, message);
                    return INVOICE;
                }
                BeanWarehouse beanWarehouse = (BeanWarehouse) messageWarehouse.getResult();
                assert beanWarehouse != null;
                model.addAttribute(MESSAGE, null);
                model.addAttribute(WAREHOUSE, beanWarehouse);
                model.addAttribute(PAYMENT, beanPayment);
            } catch (NumberFormatException e) {
                model.addAttribute(MESSAGE, MESSAGE_FIELD_ERRORS);
                return INVOICE;
            }
        } catch (NullPointerException e) {
            log.error("Valor nulo un error en UserController - invoice" + e.getMessage());
        } catch (Exception e) {
            log.error("Ocurrio un error en UserController - invoice" + e.getMessage());
        }

        return INVOICE;
    }
}
