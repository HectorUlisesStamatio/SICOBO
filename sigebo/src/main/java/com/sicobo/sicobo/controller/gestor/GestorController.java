package com.sicobo.sicobo.controller.gestor;

import com.sicobo.sicobo.dto.DTOWarehouse;
import com.sicobo.sicobo.model.BeanSite;
import com.sicobo.sicobo.model.BeanUser;
import com.sicobo.sicobo.model.BeanWarehouse;
import com.sicobo.sicobo.model.BeanWarehouseImage;
import com.sicobo.sicobo.serviceimpl.SiteServiceImpl;
import com.sicobo.sicobo.serviceimpl.UserServiceImpl;
import com.sicobo.sicobo.serviceimpl.WarehouseServiceImpl;
import com.sicobo.sicobo.serviceimpl.WarehousesTypeServiceImpl;
import com.sicobo.sicobo.util.Message;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.sicobo.sicobo.util.Constantes.MessageType.FAILED;
import static com.sicobo.sicobo.util.Constantes.ObjectMessages.MESSAGE_CATCH_ERROR;
import static com.sicobo.sicobo.util.Constantes.Redirects.*;
import static com.sicobo.sicobo.util.Constantes.MessageCodes.*;
import static com.sicobo.sicobo.util.Constantes.Roles.ROLE_ADMIN;
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

            String error = handleErrorMessage(message);
            if (error != null){
                System.out.println("error prepararRegistro");
                model.addAttribute(SITIOID, idSite);
                model.addAttribute(MESSAGE, message);
                int status = responseEntity.getStatusCode().value();
                model.addAttribute(STATUS, status);
                model.addAttribute(RESPONSE, RESPONSE);
                return GESTOR_WAREHOUSES;
            }
            model.addAttribute(RESPONSE, message);
            int status = responseEntity.getStatusCode().value();
            model.addAttribute(STATUS, status);
            warehouse.setBeanSite( (int) idSite);
            warehouse.setStatus(1);
            model.addAttribute(WAREHOUSE, warehouse);

            return GESTOR_REGISTERWAREHOUSE;
        }catch (Exception e){
            log.error("Ocurrio un error en GestorController - prepareRegisterWarehouse" + e.getMessage());
            redirectAttributes.addFlashAttribute(STATUS,SERVER_FAIL_CODE);
            return REDIRECT_ERROR;
        }
    }

    @Secured({ROLE_GESTOR})
    @PostMapping("/prepararModificacion")
    public String prepareUpdateWarehouse(@RequestParam("idSite") @NotNull long idSite, @RequestParam("idWarehouse") @NotNull long idWarehouse,
                                         Model model, RedirectAttributes redirectAttributes, DTOWarehouse warehouse) {
        try {
            ResponseEntity<?> responseEntity = warehouseService.buscar(idWarehouse);
            Message message = (Message) responseEntity.getBody();
            assert message != null;
            if (message.getType().equals(FAILED)) {
                redirectAttributes.addFlashAttribute(SITIOID, idSite);
                redirectAttributes.addFlashAttribute(MESSAGE, message);
                int status = responseEntity.getStatusCode().value();
                redirectAttributes.addFlashAttribute(STATUS, status);
                redirectAttributes.addFlashAttribute(RESPONSE, RESPONSE);
                return REDIRECT_GESTOR_LISTSITES;
            }else{
                BeanWarehouse beanWarehouse = (BeanWarehouse) message.getResult();
                warehouse.setId(beanWarehouse.getId());
                warehouse.setDescription(beanWarehouse.getDescription());
                warehouse.setSection(beanWarehouse.getSection());
                warehouse.setFinalCost(beanWarehouse.getFinalCost());
                warehouse.setStatus(beanWarehouse.getStatus());
                warehouse.setBeanSite(Long.valueOf(beanWarehouse.getBeanSite().getId()).intValue());
                warehouse.setWarehousesType(Long.valueOf(beanWarehouse.getWarehousesType().getId()).intValue());
            }

            responseEntity = warehousesTypeService.listar();
            Message message2 = (Message) responseEntity.getBody();
            assert message2 != null;
            if (message2.getType().equals(FAILED)) {
                redirectAttributes.addFlashAttribute(SITIOID, idSite);
                redirectAttributes.addFlashAttribute(MESSAGE, message2);
                int status = responseEntity.getStatusCode().value();
                redirectAttributes.addFlashAttribute(STATUS, status);
                redirectAttributes.addFlashAttribute(RESPONSE, RESPONSE);
                return REDIRECT_GESTOR_LISTSITES;
            }

            model.addAttribute(RESPONSE, message2);
            model.addAttribute(SITIOID, idSite);
            model.addAttribute(WAREHOUSE, warehouse);

            return GESTOR_REGISTERWAREHOUSE;
        }catch (Exception e){
            log.error("Ocurrio un error en GestorController - prepareUpdateWarehouse" + e.getMessage());
            redirectAttributes.addFlashAttribute(STATUS,SERVER_FAIL_CODE);
            return REDIRECT_ERROR;
        }
    }

    @Secured({ROLE_GESTOR})
    @PostMapping("/guardarBodega")
    public String saveWarehouse(@Valid @ModelAttribute("warehouse") DTOWarehouse warehouse, BindingResult result, RedirectAttributes redirectAttributes, Model model) {
        try{
            ResponseEntity<?> responseEntity = warehousesTypeService.listar();
            Message message = (Message) responseEntity.getBody();
            assert message != null;

            String error = handleErrorMessage(message);
            if (error != null){
                redirectAttributes.addFlashAttribute(MESSAGE, message);
                int status = responseEntity.getStatusCode().value();
                redirectAttributes.addFlashAttribute(STATUS, status);
                return REDIRECT_GESTOR_LISTSITES;
            }

            if (result.hasErrors()) {
                for (ObjectError errors : result.getAllErrors()) {
                    log.error("Error: " + errors.getDefaultMessage());
                }

                model.addAttribute(RESPONSE, message);
                int status = responseEntity.getStatusCode().value();
                model.addAttribute(STATUS, status);
                model.addAttribute(WAREHOUSE, warehouse);
                return GESTOR_REGISTERWAREHOUSE;
            }

            if (warehouse.getId() != 0) { // update
                responseEntity = warehouseService.buscar(warehouse.getId());
                Message message2 = (Message) responseEntity.getBody();
                assert message2 != null;

                error = handleErrorMessage(message2);
                if (error != null){
                    redirectAttributes.addFlashAttribute(RESPONSE, message);
                    int status = responseEntity.getStatusCode().value();
                    redirectAttributes.addFlashAttribute(STATUS, status);
                    return REDIRECT_GESTOR_LISTSITES;
                }

                BeanWarehouse beanWarehouse = (BeanWarehouse) message2.getResult();
                warehouse.setStatus(beanWarehouse.getStatus());
                warehouse.setFechaCreacion(beanWarehouse.getFechaCreacion());

                responseEntity = warehouseService.eliminarImagenes(warehouse.getId());
                message2 = (Message) responseEntity.getBody();
                assert message2 != null;

                error = handleErrorMessage(message2);
                if (error != null){
                    model.addAttribute(MESSAGE, message2);
                    model.addAttribute(RESPONSE, message);
                    int status = responseEntity.getStatusCode().value();
                    model.addAttribute(STATUS, status);
                    model.addAttribute(WAREHOUSE, warehouse);
                    return GESTOR_REGISTERWAREHOUSE;
                }
            }else{
                warehouse.setStatus(1);
                warehouse.setBeanSite(warehouse.getBeanSite());
            }
            responseEntity = warehouseService.guardar(warehouse);
            Message message2 = (Message) responseEntity.getBody();
            assert message2 != null;

            error = handleErrorMessage(message2);
            if (error != null){
                model.addAttribute(MESSAGE, message2);
                model.addAttribute(RESPONSE, message);
                int status = responseEntity.getStatusCode().value();
                model.addAttribute(STATUS, status);
                model.addAttribute(WAREHOUSE, warehouse);
                return GESTOR_REGISTERWAREHOUSE;
            }

            redirectAttributes.addFlashAttribute(MESSAGE, message2);
            redirectAttributes.addFlashAttribute(STATUS, responseEntity.getStatusCode().value());
            return REDIRECT_GESTOR_LISTSITES;
        }catch (AssertionError e) {
            log.error("Ocurrio un error en AdminController - saveSite" + e.getMessage());
            redirectAttributes.addFlashAttribute(STATUS, SERVER_FAIL_CODE);
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

            ResponseEntity<?> responseEntity = userService.findBeanUserByUsername(username);
            Message message = (Message) responseEntity.getBody();
            assert message != null;

            String error = handleErrorMessage(message);
            if (error != null) {
                redirectAttributes.addFlashAttribute(MESSAGE, message);
                int status = responseEntity.getStatusCode().value();
                redirectAttributes.addFlashAttribute(STATUS, status);
                return REDIRECT_ERROR;
            }
            BeanUser beanUser = (BeanUser) message.getResult();

            responseEntity = siteService.findBeanSiteByBeanUser(beanUser);
            message = (Message) responseEntity.getBody();
            assert message != null;

            error = handleErrorMessage(message);
            if (error != null){
                model.addAttribute(MESSAGE, message);
                int status = responseEntity.getStatusCode().value();
                model.addAttribute(STATUS, status);
                model.addAttribute(RESPONSE, null);
                return GESTOR_WAREHOUSES;
            }
            BeanSite beanSite = (BeanSite) message.getResult();

            responseEntity = warehouseService.findWarehousesByBeanSite(beanSite);
            message = (Message) responseEntity.getBody();
            assert message != null;

            error = handleErrorMessage(message);
            if (error != null){
                System.out.println("asdasdasdsad");
                model.addAttribute(SITIOID,  beanSite.getId());
                model.addAttribute(MESSAGE, message);
                int status = responseEntity.getStatusCode().value();
                model.addAttribute(STATUS, status);
                model.addAttribute(RESPONSE, RESPONSE);
                return GESTOR_WAREHOUSES;
            }

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

    @Secured({ROLE_GESTOR})
    @PostMapping("/cambiarEstadoBodega")
    public String changeStateWarehouse(@RequestParam("idWarehouse") Optional<Long> idWarehouse, @RequestParam("statusWarehouse") Optional<Boolean> statusWarehouse, Model model, RedirectAttributes redirectAttributes) {
        try {
            BeanWarehouse beanWarehouse = new BeanWarehouse();
            if (idWarehouse.isPresent() && statusWarehouse.isPresent()) {
                beanWarehouse.setId(idWarehouse.get());
                if(statusWarehouse.get().booleanValue()){
                    beanWarehouse.setStatus(0);
                }else{
                    beanWarehouse.setStatus(1);
                }

                ResponseEntity<?> responseEntity = warehouseService.eliminar(beanWarehouse);
                Message message = (Message) responseEntity.getBody();
                assert message != null;

                String error = handleErrorMessage(message);
                if (error != null){
                    redirectAttributes.addFlashAttribute(MESSAGE, message);
                    int status = responseEntity.getStatusCode().value();
                    redirectAttributes.addFlashAttribute(STATUS, status);
                    return REDIRECT_GESTOR_LISTSITES;
                }

                redirectAttributes.addFlashAttribute(MESSAGE, message);
                redirectAttributes.addFlashAttribute(STATUS, responseEntity.getStatusCode().value());
            } else {
                redirectAttributes.addFlashAttribute(MESSAGE, MESSAGE_CATCH_ERROR);

            }
            return REDIRECT_GESTOR_LISTSITES;
        }catch(Exception e){
            log.error("Ocurrio un error en GestorController - changeStateWarehouse" + e.getMessage());
            redirectAttributes.addFlashAttribute(STATUS,SERVER_FAIL_CODE);
            return REDIRECT_ERROR;
        }

    }

    private String handleErrorMessage(Message message) {
        if(message.getType().equals(FAILED)) {
            return ERRORS;
        }
        return null;
    }
}
