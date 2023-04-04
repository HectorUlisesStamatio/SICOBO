package com.sicobo.sicobo.controller.admin;

import com.sicobo.sicobo.dto.DTOCostType;
import com.sicobo.sicobo.dto.DTOPolicies;
import com.sicobo.sicobo.dto.DTOSite;
import com.sicobo.sicobo.dto.DTOUser;
import com.sicobo.sicobo.model.BeanPolicies;
import com.sicobo.sicobo.model.BeanSite;
import com.sicobo.sicobo.model.BeanState;
import com.sicobo.sicobo.model.BeanUser;
import com.sicobo.sicobo.serviceimpl.*;
import com.sicobo.sicobo.util.Constantes;
import com.sicobo.sicobo.util.Message;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

import static com.sicobo.sicobo.util.Constantes.Roles.ROLE_ADMIN;
import static com.sicobo.sicobo.util.Constantes.MessageCodes.SUCCESS_CODE;
import static com.sicobo.sicobo.util.Constantes.MessageCodes.FAIL_CODE;
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
    
    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private PoliciesServiceImpl policiesService;

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
        try {
            Message warehouseTypes = (Message) warehousesTypeService.listarTodas().getBody();
            assert warehouseTypes != null;
            Message costTypes = (Message) costTypeService.listar().getBody();
            assert costTypes != null;
            model.addAttribute(HIDE_COST, false);
            model.addAttribute(WAREHOUSE_TYPES, warehouseTypes.getResult());
            model.addAttribute(COST_TYPES, costTypes.getResult());
            model.addAttribute(COST, costType);
        }catch (AssertionError e) {
            log.error("Ocurrio un error en AdminController - redirectCostType" + e.getMessage());
            model.addAttribute(MESSAGE, MESSAGE_CATCH_ERROR);
        }catch(Exception e){
            log.error("Ocurrio un error en AdminController - redirectCostType" + e.getMessage());
            model.addAttribute(MESSAGE, MESSAGE_CATCH_ERROR);
        }
        return ADMIN_REGISTERCOSTTYPE;
    }

    @Secured({ROLE_ADMIN})
    @PostMapping("/montoBodega")
    public String findAmountByWarehouse(Model model, DTOCostType costType){
        return ADMIN_REGISTERCOSTTYPE;
    }

    @Secured({ROLE_ADMIN})
    @GetMapping("/listarTerminosYCondiciones")
    public String listTermsAndConditions(Model model, DTOPolicies termsAndConditions){
        model.addAttribute(OPTION, POLICIES);
        try{
            ResponseEntity<?> responseEntity = policiesService.listar();
            Message message = (Message) responseEntity.getBody();
            assert message != null;
            BeanPolicies termsAndConditionsSearched = (BeanPolicies) message.getResult();

            if (termsAndConditionsSearched.getDescription() != null){
                termsAndConditions.setDescription(termsAndConditionsSearched.getDescription());
            }else{
                termsAndConditions.setDescription("No hay términos y condiciones registrados");
            }

            model.addAttribute(TERMSANDCONDTIONS, termsAndConditions);
        }catch (Exception e){
            model.addAttribute(MESSAGE, MESSAGE_CATCH_ERROR);
            log.error("Ocurrio un error en AdminController - listarTerminosYCondiciones" + e.getMessage());
        }
        return ADMIN_TERMSANDCONDITIONS;
    }

    @Secured({ROLE_ADMIN})
    @PostMapping("/guardarTerminosYCondiciones")
    public String saveTermsAndConditions(Model model, @Valid @ModelAttribute("termsAndConditions") DTOPolicies termsAndConditions, BindingResult bindingResult, RedirectAttributes redirectAttributes){
        model.addAttribute(OPTION, POLICIES);
        try{
            if (bindingResult.hasErrors()) {
                for (ObjectError errors : bindingResult.getAllErrors()) {
                    log.error("Error: " + errors.getDefaultMessage());
                }

                model.addAttribute(STATUS, FAIL_CODE);
                model.addAttribute(TERMSANDCONDTIONS,termsAndConditions);
                return ADMIN_TERMSANDCONDITIONS;
            }
            Safelist listaBlanca = Safelist.basicWithImages().removeTags("script");
            String contenidoSanitizado = Jsoup.clean(termsAndConditions.getDescription(), listaBlanca);
            termsAndConditions.setDescription(contenidoSanitizado);
            ResponseEntity<?> responseEntity = policiesService.guardar(termsAndConditions);

            Message response = (Message) responseEntity.getBody();
            assert response != null;
            if (response.getType().equals(FAILED)) {
                model.addAttribute(MESSAGE, "Algo salio mál con el registro, corrobora que no ingreses carácteres que no esten integrados en la herramienta");
                model.addAttribute(TERMSANDCONDTIONS,termsAndConditions);
                return ADMIN_REGISTERCOSTTYPE;
            }
            Message message = (Message) responseEntity.getBody();
            redirectAttributes.addFlashAttribute(MESSAGE,message);
        }catch (Exception e){
            redirectAttributes.addFlashAttribute(MESSAGE, MESSAGE_CATCH_ERROR);
            log.error("Ocurrio un error en AdminController - guardarTerminosYCondiciones" + e.getMessage());
        }
        return REDIRECT_ADMIN_TERMSANDCONDITIONS;
    }

    @Secured({ROLE_ADMIN})
    @PostMapping("/modificarCosto")
    public String saveCost(@Valid @ModelAttribute("cost") DTOCostType cost, BindingResult result, Model model, RedirectAttributes attributes){
        model.addAttribute(OPTION, COSTTYPES);
        try {
            Message warehouseTypes = (Message) warehousesTypeService.listarTodas().getBody();
            assert warehouseTypes != null;
            Message costTypes = (Message) costTypeService.listar().getBody();
            assert costTypes != null;
            
            if (result.hasErrors()) {
                for (ObjectError error : result.getAllErrors()) {
                    log.error("Error: " + error.getDefaultMessage());
                }

                model.addAttribute(WAREHOUSE_TYPES, warehouseTypes.getResult());
                model.addAttribute(COST_TYPES, costTypes.getResult());
                model.addAttribute(HIDE_COST, true);
                model.addAttribute(COST, cost);
                return ADMIN_REGISTERCOSTTYPE;
            }

            Message response = (Message) costTypeService.guardar(cost).getBody();
            assert response != null;
            if (response.getType().equals(FAILED)) {
                model.addAttribute(MESSAGE, response);
                model.addAttribute(WAREHOUSE_TYPES, warehouseTypes.getResult());
                model.addAttribute(COST_TYPES, costTypes.getResult());
                model.addAttribute(HIDE_COST, true);
                model.addAttribute(COST, cost);
                return ADMIN_REGISTERCOSTTYPE;
            }

            attributes.addFlashAttribute(MESSAGE, response);
        }catch (AssertionError e) {
            attributes.addFlashAttribute(MESSAGE, MESSAGE_CATCH_ERROR);
            log.error("Ocurrio un error en AdminController - saveCost" + e.getMessage());
        }catch(Exception e){
            log.error("Ocurrio un error en AdminController - saveCost" + e.getMessage());
            attributes.addFlashAttribute(MESSAGE, MESSAGE_CATCH_ERROR);
        }

        return REDIRECT_ADMIN_REGISTERCOSTTYPE;
    }

    @Secured({ROLE_ADMIN})
    @GetMapping("/listarGestores")
    public String listarGestores(Model model){
        model.addAttribute(OPTION, GESTORES);
        try {
            Message message = (Message) userService.listar().getBody();
            assert message !=null;
            model.addAttribute(RESPONSE, message);
        }catch (NullPointerException e) {
            log.error("Valor nulo un error en AdminController - al listar los gestores" + e.getMessage());
            model.addAttribute(MESSAGE, MESSAGE_CATCH_ERROR);
        } catch (Exception e) {
            log.error("Ocurrio un error en AdminController - al listar los gestores" + e.getMessage());
            model.addAttribute(MESSAGE, MESSAGE_CATCH_ERROR);
        }
        return ADMIN_LISTGESTORES;
    }

    @Secured({ROLE_ADMIN})
    @GetMapping("/registrarGestor")
    public String registrarGestor(Model model, DTOUser user){
        model.addAttribute(OPTION, GESTORES);
        model.addAttribute(USER, user);
        return ADMIN_REGISTERGESTORES;
    }

    @Secured({ROLE_ADMIN})
    @PostMapping("/guardarGestor")
    public String guardarGestor(@Validated @ModelAttribute(USER) DTOUser user, BindingResult result, RedirectAttributes attributes, Model model){
        try{
        if(result.hasErrors()){
            for (ObjectError error: result.getAllErrors()){
                log.error(ERRORS + error.getDefaultMessage());
            }
            return ADMIN_REGISTERGESTORES;
        }
        Message response = (Message) userService.guardar(user).getBody();
        assert response != null;
        if(response.getType().equals(FAILED)){
            model.addAttribute(MESSAGE, response);
            return ADMIN_REGISTERGESTORES;
        }
        attributes.addFlashAttribute(MESSAGE, response);
        }catch (AssertionError e) {
            attributes.addFlashAttribute(MESSAGE, MESSAGE_CATCH_ERROR);
            log.error("Ocurrio un error en AdminController - saveGestor" + e.getMessage());
        }
        return REDIRECT_ADMIN_LISTGESTORES;
    }

    @Secured({ROLE_ADMIN})
    @PostMapping("/prepararEdicion")
    public String prepararEdicion(@RequestParam("idGestor") long idGestor, Model model, RedirectAttributes attributes) {
        model.addAttribute(OPTION, GESTORES);
        try{
            Message message = (Message) userService.buscar(idGestor).getBody();
            assert message != null;
            if (message.getType().equals(FAILED)) {
                attributes.addFlashAttribute(MESSAGE, message);
                return REDIRECT_ADMIN_LISTGESTORES;
            }
            model.addAttribute(RESPONSE, message);
            model.addAttribute(USER, message.getResult());
        }catch (NullPointerException e) {
            log.error("Valor nulo un error en AdminController - prepareEdicion" + e.getMessage());
            model.addAttribute(MESSAGE, MESSAGE_CATCH_ERROR);
        }catch(Exception e){
            log.error("Ocurrio un error en AdminController - prepareEdicion" + e.getMessage());
            model.addAttribute(MESSAGE, MESSAGE_CATCH_ERROR);
        }

        return ADMIN_UPDATEGESTORES;
    }

    @Secured({ROLE_ADMIN})
    @PostMapping("/actualizarGestor")
    public String updateGestor(@Valid @ModelAttribute(USER) DTOUser user, BindingResult result, RedirectAttributes attributes, Model model) {
        model.addAttribute(OPTION, GESTORES);
        try{
            if (result.hasErrors()) {
                for (ObjectError error : result.getAllErrors()) {
                    log.error("Error: " + error.getDefaultMessage());
                }
                return ADMIN_UPDATEGESTORES;
            }
            Message response = (Message) userService.editar(user).getBody();
            assert response !=null;
            if (response.getType().equals(FAILED)) {
                model.addAttribute(MESSAGE, response);
                return ADMIN_UPDATEGESTORES;
            }
            attributes.addFlashAttribute(MESSAGE, response);
        }catch (NullPointerException e) {
            log.error("Valor nulo un error en AdminController - updateGestores" + e.getMessage());
            attributes.addFlashAttribute(MESSAGE, MESSAGE_CATCH_ERROR);
        }catch(Exception e){
            log.error("Ocurrio un error en AdminController - updateGestores" + e.getMessage());
            attributes.addFlashAttribute(MESSAGE, MESSAGE_CATCH_ERROR);
        }
        return REDIRECT_ADMIN_LISTGESTORES;
    }


    @Secured({ROLE_ADMIN})
    @PostMapping("/cambiarEstadoGestor")
    public String changeStatGestores(@RequestParam("idGestor2") Optional<Long> idGestor, @RequestParam("statusGestor") Optional<Boolean> statusGestor, Model model, RedirectAttributes attributes) {
        model.addAttribute(OPTION, GESTORES);
        try {
            BeanUser beanUser = new BeanUser();
            if (idGestor.isPresent() && statusGestor.isPresent()) {
                beanUser.setId(idGestor.get());
                if(statusGestor.get().booleanValue()){
                    beanUser.setEnabled(0);
                }else{
                    beanUser.setEnabled(1);
                }
                Message message = (Message) userService.eliminar(beanUser).getBody();
                assert message !=null;
                attributes.addFlashAttribute(MESSAGE, message);
            } else {
                attributes.addFlashAttribute(MESSAGE, MESSAGE_CATCH_ERROR);
            }
        }catch (NullPointerException e) {
            log.error("Valor nulo un error en AdminController - changeStatGestores" + e.getMessage());
            attributes.addFlashAttribute(MESSAGE, MESSAGE_CATCH_ERROR);
        }catch(Exception e){
            log.error("Ocurrio un error en AdminController - changeStatGestores" + e.getMessage());
            attributes.addFlashAttribute(MESSAGE, MESSAGE_CATCH_ERROR);
        }
        return REDIRECT_ADMIN_LISTGESTORES;
    }





}
