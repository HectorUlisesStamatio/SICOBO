package com.sicobo.sicobo.controller.gestor;

import com.sicobo.sicobo.dto.DTOWarehouse;
import com.sicobo.sicobo.model.*;
import com.sicobo.sicobo.serviceimpl.*;
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
import java.util.Optional;

import static com.sicobo.sicobo.util.Constantes.MessageHeaders.FAILED_SEARCH;
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

    @Autowired
    private CostTypeServiceImpl costTypeService;

    @Secured({ROLE_GESTOR})
    @PostMapping("/prepararRegistro")
    public String prepareRegisterWarehouse(@RequestParam("idSite") @NotNull long idSite, Model model, RedirectAttributes redirectAttributes, DTOWarehouse warehouse) {
        try {
            model.addAttribute(OPTION, WAREHOUSES);
            ResponseEntity<?> responseEntity = costTypeService.listar();
            Message message = (Message) responseEntity.getBody();
            assert message != null;
            List<BeanCostType> listBeanCostType = (List<BeanCostType>)message.getResult();
            if (listBeanCostType.isEmpty()){
                message = new Message(FAILED_SEARCH,"No hay registros de tipos de bodegas", FAILED,FAIL_CODE, null);
                redirectAttributes.addFlashAttribute(MESSAGE, message);
                return REDIRECT_GESTOR_LISTWAREHOUSES;
            }

            responseEntity = warehousesTypeService.listar();
            message = (Message) responseEntity.getBody();
            assert message != null;

            model.addAttribute(RESPONSE, message);
            model.addAttribute(COST_TYPES, listBeanCostType);
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
            model.addAttribute(OPTION, WAREHOUSES);
            ResponseEntity<?> responseEntity = warehouseService.buscar(idWarehouse);
            Message message = (Message) responseEntity.getBody();
            assert message != null;
            if (message.getType().equals(FAILED)) {
                redirectAttributes.addFlashAttribute(SITIOID, idSite);
                redirectAttributes.addFlashAttribute(MESSAGE, message);
                int status = responseEntity.getStatusCode().value();
                redirectAttributes.addFlashAttribute(STATUS, status);
                redirectAttributes.addFlashAttribute(RESPONSE, RESPONSE);
                return REDIRECT_GESTOR_LISTWAREHOUSES;
            }else{
                BeanWarehouse beanWarehouse = (BeanWarehouse) message.getResult();
                warehouse.setId(beanWarehouse.getId());
                warehouse.setDescription(beanWarehouse.getDescription());
                warehouse.setSection(beanWarehouse.getSection());
                warehouse.setFinalCost(beanWarehouse.getFinalCost());
                warehouse.setStatus(beanWarehouse.getStatus());
                warehouse.setBeanSite(beanWarehouse.getBeanSite().getId().intValue());
                warehouse.setWarehousesType(beanWarehouse.getWarehousesType().getId().intValue());
            }

            responseEntity = costTypeService.listar();
            message = (Message) responseEntity.getBody();
            assert message != null;
            List<BeanCostType> listBeanCostType = (List<BeanCostType>) message.getResult();

            responseEntity = warehousesTypeService.listar();
            Message message2 = (Message) responseEntity.getBody();
            assert message2 != null;
            if (message2.getType().equals(FAILED)) {
                redirectAttributes.addFlashAttribute(SITIOID, idSite);
                redirectAttributes.addFlashAttribute(MESSAGE, message2);
                int status = responseEntity.getStatusCode().value();
                redirectAttributes.addFlashAttribute(STATUS, status);
                redirectAttributes.addFlashAttribute(RESPONSE, RESPONSE);
                return REDIRECT_GESTOR_LISTWAREHOUSES;
            }

            model.addAttribute(COST_TYPES, listBeanCostType);
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
            redirectAttributes.addFlashAttribute(OPTION, WAREHOUSES);
            ResponseEntity<?> responseEntity = warehousesTypeService.listar();
            Message message = (Message) responseEntity.getBody();
            assert message != null;

            String error = handleErrorMessage(message);
            if (error != null){
                redirectAttributes.addFlashAttribute(MESSAGE, message);
                int status = responseEntity.getStatusCode().value();
                redirectAttributes.addFlashAttribute(STATUS, status);
                return REDIRECT_GESTOR_LISTWAREHOUSES;
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
                    return REDIRECT_GESTOR_LISTWAREHOUSES;
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
            return REDIRECT_GESTOR_LISTWAREHOUSES;
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
            model.addAttribute(OPTION, WAREHOUSES);
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
                model.addAttribute(STATUS, SUCCESS_CODE);
                model.addAttribute(RESPONSE, null);
                return GESTOR_WAREHOUSES;
            }
            BeanSite beanSite = (BeanSite) message.getResult();

            responseEntity = warehouseService.findWarehousesByBeanSite(beanSite);
            message = (Message) responseEntity.getBody();
            assert message != null;

            error = handleErrorMessage(message);
            if (error != null){
                model.addAttribute(SITIOID,  beanSite.getId());
                model.addAttribute(RESPONSE, message);
                model.addAttribute(STATUS, SUCCESS_CODE);
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
            redirectAttributes.addFlashAttribute(OPTION, WAREHOUSES);
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
                    return REDIRECT_GESTOR_LISTWAREHOUSES;
                }

                redirectAttributes.addFlashAttribute(MESSAGE, message);
                redirectAttributes.addFlashAttribute(STATUS, responseEntity.getStatusCode().value());
            } else {
                redirectAttributes.addFlashAttribute(MESSAGE, MESSAGE_CATCH_ERROR);

            }
            return REDIRECT_GESTOR_LISTWAREHOUSES;
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
