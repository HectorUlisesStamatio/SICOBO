package com.sicobo.sicobo.controller.user;

import com.sicobo.sicobo.dto.DTOPolicies;
import com.sicobo.sicobo.model.BeanPolicies;
import com.sicobo.sicobo.serviceimpl.PoliciesServiceImpl;
import com.sicobo.sicobo.serviceimpl.WarehouseServiceImpl;
import com.sicobo.sicobo.util.Message;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static com.sicobo.sicobo.util.Constantes.ObjectMessages.MESSAGE_CATCH_ERROR;
import static com.sicobo.sicobo.util.Constantes.Redirects.*;
import static com.sicobo.sicobo.util.Constantes.Roles.ROLE_USUARIO;
import static com.sicobo.sicobo.util.Constantes.Stuff.*;

@Controller
@Slf4j
public class UserController {
    @Autowired
    private PoliciesServiceImpl policiesService;

    @Autowired
    private WarehouseServiceImpl warehouseService;

    @GetMapping("/politicas")
    public String mostrarContenido(Model model, DTOPolicies termsAndConditions) {
        try{
            ResponseEntity<?> responseEntity = policiesService.listar();
            Message message = (Message) responseEntity.getBody();
            assert message != null;
            BeanPolicies termsAndConditionsSearched = (BeanPolicies) message.getResult();

            if (termsAndConditionsSearched.getDescription() != null){
                termsAndConditions.setDescription(termsAndConditionsSearched.getDescription());
            }else{
                termsAndConditions.setDescription("No se han establecido los términos y condiciones");
            }

            model.addAttribute(TERMSANDCONDTIONS, termsAndConditions);
        }catch (Exception e){
            model.addAttribute(MESSAGE, MESSAGE_CATCH_ERROR);
            log.error("Ocurrio un error en UserController - mostrarContenido" + e.getMessage());
        }
        return USER_TERMSANDCONDITIONS;
    }

    @Secured({ROLE_USUARIO})
    @GetMapping("/misBodegas")
    public String misBodegas(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Message warehousesClient = (Message) warehouseService.buscarBodegaPorUsername(auth.getName()).getBody();

        System.out.println(warehousesClient);

        model.addAttribute(RESPONSE, warehousesClient.getResult());

        return MIS_BODEGAS;
    }
}
