package com.sicobo.sicobo.controller.gestor;

import com.sicobo.sicobo.dto.DTOSite;
import com.sicobo.sicobo.dto.DTOWarehouse;
import com.sicobo.sicobo.model.BeanSite;
import com.sicobo.sicobo.model.BeanUser;
import com.sicobo.sicobo.model.BeanWarehousesType;
import com.sicobo.sicobo.serviceImpl.SiteServiceImpl;
import com.sicobo.sicobo.serviceImpl.UserServiceImpl;
import com.sicobo.sicobo.serviceImpl.WarehouseServiceImpl;
import com.sicobo.sicobo.serviceImpl.WarehousesTypeServiceImpl;
import com.sicobo.sicobo.util.Message;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

import static com.sicobo.sicobo.util.Constantes.Message_Type.FAILED;
import static com.sicobo.sicobo.util.Constantes.Redirects.*;
import static com.sicobo.sicobo.util.Constantes.Message_Codes.*;
import static com.sicobo.sicobo.util.Constantes.Roles.ROLE_GESTOR;
import static com.sicobo.sicobo.util.Constantes.Stuff.*;

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
    @Autowired
    private WarehousesTypeServiceImpl warehousesTypeService;

    @Secured({ROLE_GESTOR})
    @PostMapping("/prepararRegistro")
    public String prepareRegisterWarehouse(@RequestParam("idSite") @NotNull long idSite, Model model, RedirectAttributes redirectAttributes,@Valid @ModelAttribute("warehouse")  DTOWarehouse warehouse, BindingResult bindingResult) {
        try {
            System.out.println(idSite);
            ResponseEntity<?> responseEntity = warehousesTypeService.listar();
            Message message = (Message) responseEntity.getBody();
            assert message != null;

            String error = handleErrorMessage(message, redirectAttributes);
            if (error != null)
                return REDIRECT_ERROR;
            model.addAttribute(SITIOID, idSite);
            model.addAttribute(RESPONSE, message);
            Object status = responseEntity.getStatusCode().value();
            model.addAttribute(STATUS, status);

            return "/gestorViews/registerWarehouse";
        }catch (Exception e){
            log.error("Ocurrio un error en GestorController - prepareRegisterWarehouse" + e.getMessage());
            redirectAttributes.addFlashAttribute(STATUS,SERVER_FAIL_CODE);
            return REDIRECT_ERROR;
        }
    }

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
            model.addAttribute(SITIOID, beanSite.getId());
            model.addAttribute(RESPONSE, message);
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
