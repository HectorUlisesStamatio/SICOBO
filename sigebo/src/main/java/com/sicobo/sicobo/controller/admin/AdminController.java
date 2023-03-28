package com.sicobo.sicobo.controller.admin;

import com.sicobo.sicobo.dto.DTOSite;
import com.sicobo.sicobo.dto.DTOUser;
import com.sicobo.sicobo.model.BeanSite;
import com.sicobo.sicobo.model.BeanState;
import com.sicobo.sicobo.serviceimpl.SiteServiceImpl;
import com.sicobo.sicobo.serviceimpl.StateServiceImpl;
import com.sicobo.sicobo.serviceimpl.UserServiceImpl;
import com.sicobo.sicobo.util.Message;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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
    private UserServiceImpl userService;

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


}
