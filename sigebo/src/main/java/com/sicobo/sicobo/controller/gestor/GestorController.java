package com.sicobo.sicobo.controller.gestor;

import com.sicobo.sicobo.model.BeanSite;
import com.sicobo.sicobo.model.BeanUser;
import com.sicobo.sicobo.serviceImpl.SiteServiceImpl;
import com.sicobo.sicobo.serviceImpl.UserServiceImpl;
import com.sicobo.sicobo.serviceImpl.WarehouseServiceImpl;
import com.sicobo.sicobo.util.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

import static com.sicobo.sicobo.util.Constantes.Message_Type.FAILED;
import static com.sicobo.sicobo.util.Constantes.Redirects.*;
import static com.sicobo.sicobo.util.Constantes.Message_Codes.*;
import static com.sicobo.sicobo.util.Constantes.Roles.ROLE_GESTOR;
import static com.sicobo.sicobo.util.Constantes.Stuff.MESSAGE;
import static com.sicobo.sicobo.util.Constantes.Stuff.STATUS;

@Controller
@RequestMapping("/gestor")
@Slf4j
public class GestorController {
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private SiteServiceImpl siteService;
    @Autowired
    private WarehouseServiceImpl warehouseService;

    @Secured({ROLE_GESTOR})
    @GetMapping("/bodegas")
    public String listWarehouses(Model model, RedirectAttributes redirectAttributes){
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = ((User) auth.getPrincipal()).getUsername();
            assert username != null;

            // Búsqueda de usuario
            Message message = (Message) userService.findBeanUserByUsername(username).getBody();
            assert message != null;

            String error = handleErrorMessage(message, redirectAttributes);
            if (error != null)
                return REDIRECT_ERROR;

            BeanUser beanUser = (BeanUser) message.getResult();

            // Búsqueda de sitio
            message = (Message) siteService.findBeanSiteByBeanUser(beanUser).getBody();
            assert message != null;

            error = handleErrorMessage(message, redirectAttributes);
            if (error != null)
                return REDIRECT_ERROR;
            BeanSite beanSite = (BeanSite) message.getResult();

            // Búsqueda de bodegas
            ResponseEntity<?> responseEntity = warehouseService.findWarehousesByBeanSite(beanSite);
            message = (Message) responseEntity.getBody();
            assert message != null;

            error = handleErrorMessage(message, redirectAttributes);
            if (error != null)
                return REDIRECT_ERROR;

            model.addAttribute("response", message);
            Object status = responseEntity.getStatusCode().value();
            model.addAttribute(STATUS, status);

            return GESTOR_BODEGAS;
        } catch (Exception e) {
            log.error("Ocurrio un error en GestorController - listWarehouses" + e.getMessage());
            redirectAttributes.addFlashAttribute(STATUS,SERVER_FAIL_CODE);
            return REDIRECT_ERROR;
        }
    }

    private String handleErrorMessage(Message message, RedirectAttributes redirectAttributes) {
        if(message.getType().equals(FAILED)) {
            redirectAttributes.addFlashAttribute(MESSAGE,message.getMessage());
            redirectAttributes.addFlashAttribute(STATUS,FAIL_CODE);
            return REDIRECT_ERROR;
        }
        return null;
    }
}
