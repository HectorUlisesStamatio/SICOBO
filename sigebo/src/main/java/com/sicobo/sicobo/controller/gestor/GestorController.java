package com.sicobo.sicobo.controller.gestor;

import com.sicobo.sicobo.dto.DTOWarehouse;
import com.sicobo.sicobo.model.BeanSite;
import com.sicobo.sicobo.model.BeanState;
import com.sicobo.sicobo.model.BeanUser;
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
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

import static com.sicobo.sicobo.util.Constantes.MessageType.FAILED;
import static com.sicobo.sicobo.util.Constantes.ObjectMessages.MESSAGE_CATCH_ERROR;
import static com.sicobo.sicobo.util.Constantes.Redirects.*;
import static com.sicobo.sicobo.util.Constantes.MessageCodes.*;
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
    public String prepareRegisterWarehouse(@RequestParam("idSite") @NotNull long idSite, Model model, RedirectAttributes redirectAttributes, DTOWarehouse warehouse) {
        try {
            ResponseEntity<?> responseEntity = warehousesTypeService.listar();
            Message message = (Message) responseEntity.getBody();
            assert message != null;

            String error = handleErrorMessage(message, redirectAttributes);
            if (error != null)
                return REDIRECT_ERROR;
            model.addAttribute(RESPONSE, message);
            int status = responseEntity.getStatusCode().value();
            model.addAttribute(STATUS, status);
            warehouse.setBeanSite( (int) idSite);
            warehouse.setStatus(1);
            model.addAttribute("warehouse", warehouse);

            return "/gestorViews/registerWarehouse";
        }catch (Exception e){
            log.error("Ocurrio un error en GestorController - prepareRegisterWarehouse" + e.getMessage());
            redirectAttributes.addFlashAttribute(STATUS,SERVER_FAIL_CODE);
            return REDIRECT_ERROR;
        }
    }

    @Secured({ROLE_GESTOR})
    @PostMapping("/guardarBodega")
    public String saveSite(@Valid @ModelAttribute("warehouse") DTOWarehouse warehouse, BindingResult result, RedirectAttributes redirectAttributes, Model model) {
        try{
            ResponseEntity<?> responseEntity = warehousesTypeService.listar();
            Message message = (Message) responseEntity.getBody();
            assert message != null;

            String error = handleErrorMessage(message, redirectAttributes);
            if (error != null)
                model.addAttribute(RESPONSE, message);
                int status = responseEntity.getStatusCode().value();
                model.addAttribute(STATUS, status);
                warehouse.setBeanSite( warehouse.getBeanSite());
                warehouse.setStatus(1);
                model.addAttribute("warehouse", warehouse);
                return GESTOR_REGISTERWAREHOUSE;
            // aqui habria que ver como se va a hacer ya que primero se debe subir las imagenes y
            // de acuerdo se van a regresar los id y url de donde se inserto para en el futuro actualizar
            Message response = (Message) siteService.guardar(site).getBody();
            assert response != null;
            if (response.getType().equals(FAILED)) {
                model.addAttribute(MESSAGE, response);
                model.addAttribute(STATES, states);
                return ADMIN_REGISTERSITE;
            }
            attributes.addFlashAttribute(MESSAGE, response);
        }catch (AssertionError e) {
            attributes.addFlashAttribute(MESSAGE, MESSAGE_CATCH_ERROR);
            log.error("Ocurrio un error en AdminController - saveSite" + e.getMessage());
        }
        return REDIRECT_ADMIN_LISTSITES;
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

            return GESTOR_WAREHOUSES;
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
