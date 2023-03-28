package com.sicobo.sicobo.controller.admin;

import com.sicobo.sicobo.dao.DaoWarehousesType;
import com.sicobo.sicobo.dto.DTOCostType;
import com.sicobo.sicobo.dto.DTOSite;
import com.sicobo.sicobo.model.BeanSite;
import com.sicobo.sicobo.model.BeanState;
import com.sicobo.sicobo.serviceimpl.CostTypeServiceImpl;
import com.sicobo.sicobo.serviceimpl.SiteServiceImpl;
import com.sicobo.sicobo.serviceimpl.StateServiceImpl;
import com.sicobo.sicobo.serviceimpl.WarehousesTypeServiceImpl;
import com.sicobo.sicobo.util.Message;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

import static com.sicobo.sicobo.util.Constantes.Roles.ROLE_ADMIN;

import static com.sicobo.sicobo.util.Constantes.Stuff.*;
import static com.sicobo.sicobo.util.Constantes.Redirects.*;
import static com.sicobo.sicobo.util.Constantes.ObjectMessages.*;
import static com.sicobo.sicobo.util.Constantes.MessageType.*;


@Controller
@RequestMapping("/admin")
@Slf4j
public class AdminController {


    @Autowired
    private SiteServiceImpl siteService;

    @Autowired
    private StateServiceImpl stateService;

    @Autowired
    private WarehousesTypeServiceImpl warehousesTypeService;

    @Autowired
    private CostTypeServiceImpl costTypeService;

    @Secured({ROLE_ADMIN})
    @GetMapping("/dashboard")
    public String dash(Model model) {
        model.addAttribute(OPTION, null);
        return ADMIN_DASHBOARD;
    }

    @Secured({ROLE_ADMIN})
    @GetMapping("/sitios")
    public String listar(Model model) {
        model.addAttribute(OPTION, SITES);
        try {
            Message message = (Message) siteService.listar().getBody();
            assert message !=null;
            model.addAttribute(RESPONSE, message);
        }catch (NullPointerException e) {
            log.error("Valor nulo un error en AdminController - listar" + e.getMessage());
            model.addAttribute(MESSAGE, MESSAGE_CATCH_ERROR);
        } catch (Exception e) {
            log.error("Ocurrio un error en AdminController - listar" + e.getMessage());
            model.addAttribute(MESSAGE, MESSAGE_CATCH_ERROR);
        }
        return ADMIN_LISTSITES;
    }

    @Secured({ROLE_ADMIN})
    @GetMapping("/registrarSitio")
    public String prepareRegistration(Model model, DTOSite site) {
        model.addAttribute(OPTION, SITES);
        try {
            Message response = (Message) stateService.listar().getBody();
            assert response != null;
            model.addAttribute(STATES, response.getResult());
            model.addAttribute(SITE, site);
        }catch (NullPointerException e) {
            model.addAttribute(MESSAGE, MESSAGE_CATCH_ERROR);
            log.error("Valor nulo un error en AdminController - prepareRegistration" + e.getMessage());
        }  catch (Exception e) {
            model.addAttribute(MESSAGE, MESSAGE_CATCH_ERROR);
            log.error("Ocurrio un error en AdminController - prepareRegistration" + e.getMessage());
        }


        return ADMIN_REGISTERSITE;
    }

    @Secured({ROLE_ADMIN})
    @PostMapping("/guardarSitio")
    public String saveSite(@Valid @ModelAttribute("site") DTOSite site, BindingResult result, RedirectAttributes attributes, Model model) {
        model.addAttribute(OPTION, SITES);
        try{
            Message message = (Message) stateService.listar().getBody();
            assert message != null;
            List<BeanState> states = (List<BeanState>) message.getResult();

            if (result.hasErrors()) {
                for (ObjectError error : result.getAllErrors()) {
                    log.error("Error: " + error.getDefaultMessage());
                }
                model.addAttribute(STATES, states);
                return ADMIN_REGISTERSITE;
            }

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

    @Secured({ROLE_ADMIN})
    @PostMapping("/prepararModificacion")
    public String prepareModification(@RequestParam("idSite") long idSite, Model model, RedirectAttributes attributes) {
        model.addAttribute(OPTION, SITES);
        try{
            Message message = (Message) siteService.buscar(idSite).getBody();
            assert message != null;
            if (message.getType().equals(FAILED)) {
                attributes.addFlashAttribute(MESSAGE, message);
                return REDIRECT_ADMIN_LISTSITES;
            }
            Message statesResponse = (Message) stateService.listar().getBody();
            assert statesResponse != null;
            model.addAttribute(RESPONSE, message);
            model.addAttribute(STATES, statesResponse.getResult());
            model.addAttribute(SITE, message.getResult());
        }catch (NullPointerException e) {
            log.error("Valor nulo un error en AdminController - prepareModification" + e.getMessage());
            model.addAttribute(MESSAGE, MESSAGE_CATCH_ERROR);
        }catch(Exception e){
            log.error("Ocurrio un error en AdminController - prepareModification" + e.getMessage());
            model.addAttribute(MESSAGE, MESSAGE_CATCH_ERROR);
        }

        return ADMIN_UPDATESITE;
    }

    @Secured({ROLE_ADMIN})
    @PostMapping("/actualizarSitio")
    public String updateSite(@Valid @ModelAttribute("site") DTOSite site, BindingResult result, RedirectAttributes attributes, Model model) {
        model.addAttribute(OPTION, SITES);
        try{
            Message message =  (Message) stateService.listar().getBody();
            assert message != null;
            if (result.hasErrors()) {
                for (ObjectError error : result.getAllErrors()) {
                    log.error("Error: " + error.getDefaultMessage());
                }
                model.addAttribute(STATES, message.getResult());
                return ADMIN_UPDATESITE;
            }
            Message response = (Message) siteService.editar(site).getBody();
            assert response !=null;
            if (response.getType().equals(FAILED)) {
                model.addAttribute(MESSAGE, response);
                model.addAttribute(STATES, message.getResult());
                return ADMIN_UPDATESITE;
            }
            attributes.addFlashAttribute(MESSAGE, response);
        }catch (NullPointerException e) {
            log.error("Valor nulo un error en AdminController - updateSite" + e.getMessage());
            attributes.addFlashAttribute(MESSAGE, MESSAGE_CATCH_ERROR);
        }catch(Exception e){
            log.error("Ocurrio un error en AdminController - updateSite" + e.getMessage());
            attributes.addFlashAttribute(MESSAGE, MESSAGE_CATCH_ERROR);
        }
        return REDIRECT_ADMIN_LISTSITES;
    }


    @Secured({ROLE_ADMIN})
    @PostMapping("/cambiarEstadoSitio")
    public String changeStateSite(@RequestParam("idSite") Optional<Long> idSite, @RequestParam("statusSite") Optional<Boolean> statusSite, Model model, RedirectAttributes attributes) {
        model.addAttribute(OPTION, SITES);
        try {
            BeanSite beanSite = new BeanSite();
            if (idSite.isPresent() && statusSite.isPresent()) {
                beanSite.setId(idSite.get());
                if(statusSite.get().booleanValue()){
                    beanSite.setStatus(0);
                }else{
                    beanSite.setStatus(1);
                }
                Message message = (Message) siteService.eliminar(beanSite).getBody();
                assert message !=null;
                attributes.addFlashAttribute(MESSAGE, message);
            } else {
                attributes.addFlashAttribute(MESSAGE, MESSAGE_CATCH_ERROR);
            }
        }catch (NullPointerException e) {
            log.error("Valor nulo un error en AdminController - changeStateSite" + e.getMessage());
            attributes.addFlashAttribute(MESSAGE, MESSAGE_CATCH_ERROR);
        }catch(Exception e){
            log.error("Ocurrio un error en AdminController - changeStateSite" + e.getMessage());
            attributes.addFlashAttribute(MESSAGE, MESSAGE_CATCH_ERROR);
        }
        return REDIRECT_ADMIN_LISTSITES;
    }

    @Secured({ROLE_ADMIN})
    @GetMapping("/costos")
    public String redirectCostType(Model model, DTOCostType costType){
        model.addAttribute(OPTION, COSTTYPES);
        Message warehouseTypes = (Message) warehousesTypeService.listar().getBody();
        assert warehouseTypes != null;
        Message costTypes = (Message) costTypeService.listar().getBody();
        assert costTypes != null;
        model.addAttribute(WAREHOUSE_TYPES, warehouseTypes.getResult());
        model.addAttribute(COST_TYPES, costTypes.getResult());
        model.addAttribute("cost", costType);
        return ADMIN_REGISTERCOSTTYPE;
    }

    @Secured({ROLE_ADMIN})
    @PostMapping("/montoBodega")
    public String findAmountByWarehouse(Model model, DTOCostType costType){


        return ADMIN_REGISTERCOSTTYPE;
    }

    @Secured({ROLE_ADMIN})
    @PostMapping("/modificarCosto")
    public String saveCost(@Valid @ModelAttribute("cost") DTOCostType cost, BindingResult result, Model model){
        if (result.hasErrors()) {
            for (ObjectError error : result.getAllErrors()) {
                log.error("Error: " + error.getDefaultMessage());
            }
            return ADMIN_REGISTERCOSTTYPE;
        }

        return ADMIN_REGISTERCOSTTYPE;
    }


}
