package com.sicobo.sicobo.controller.admin;

import com.sicobo.sicobo.dto.DTOSite;
import com.sicobo.sicobo.model.BeanSite;
import com.sicobo.sicobo.model.BeanState;
import com.sicobo.sicobo.serviceImpl.SiteServiceImpl;
import com.sicobo.sicobo.serviceImpl.StateServiceImpl;
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


@Controller
@RequestMapping("/admin")
@Slf4j
public class AdminController {


    @Autowired
    private SiteServiceImpl siteService;

    @Autowired
    private StateServiceImpl stateService;

    @Secured({ROLE_ADMIN})
    @GetMapping("/dashboard")
    public String dash(Model model) {
        model.addAttribute("opcion", null);
        return "adminViews/dashboard";
    }

    @Secured({ROLE_ADMIN})
    @GetMapping("/sitios")
    public String listar(Model model) {
        model.addAttribute("opcion", "sitios");
        try {
            Message message = (Message) siteService.listar().getBody();
            model.addAttribute("response", message);
        }catch (NullPointerException e) {
            log.error("Valor nulo un error en AdminController - listar" + e.getMessage());
        } catch (Exception e) {
            log.error("Ocurrio un error en AdminController - listar" + e.getMessage());
        }
        return "adminViews/listSites";
    }

    @Secured({ROLE_ADMIN})
    @GetMapping("/registrarSitio")
    public String prepareRegistration(Model model, DTOSite site) {
        model.addAttribute("opcion", "sitios");
        try {
            Message response = (Message) stateService.listar().getBody();
            model.addAttribute("states", response.getResult());
            model.addAttribute("site", site);
        }catch (NullPointerException e) {
            log.error("Valor nulo un error en AdminController - prepareRegistration" + e.getMessage());
        }  catch (Exception e) {
            log.error("Ocurrio un error en AdminController - prepareRegistration" + e.getMessage());
        }


        return "adminViews/registerSite";
    }

    @Secured({ROLE_ADMIN})
    @PostMapping("/guardarSitio")
    public String saveSite(@Valid @ModelAttribute("site") DTOSite site, BindingResult result, RedirectAttributes attributes, Model model) {
        try{
            Message message = (Message) stateService.listar().getBody();
            assert message != null;
            List<BeanState> states = (List<BeanState>) message.getResult();

            model.addAttribute("opcion", "sitios");
            if (result.hasErrors()) {
                for (ObjectError error : result.getAllErrors()) {
                    log.error("Error: " + error.getDefaultMessage());
                }
                model.addAttribute("states", states);
                return "adminViews/registerSite";
            }

            Message response = (Message) siteService.guardar(site).getBody();
            assert response != null;
            if (message.getType().equals("failed")) {
                model.addAttribute("message", response);
                model.addAttribute("states", states);
                return "adminViews/registerSite";
            }
            attributes.addFlashAttribute("message", message);
        }catch (AssertionError e) {
            log.error("Ocurrio un error en AdminController - saveSite" + e.getMessage());
        }
        return "redirect:/admin/sitios";
    }

    @Secured({ROLE_ADMIN})
    @PostMapping("/prepararModificacion")
    public String prepareModification(@RequestParam("idSite") long idSite, Model model, RedirectAttributes attributes) {
        model.addAttribute("opcion", "sitios");
        try{
            Message message = (Message) siteService.buscar(idSite).getBody();
            if (message.getType().equals("failed")) {
                attributes.addFlashAttribute("message", message);
                return "redirect:/admin/sitios";
            }
            model.addAttribute("response", message);
            model.addAttribute("states", ((Message) stateService.listar().getBody()).getResult());
            model.addAttribute("site", message.getResult());
        }catch (NullPointerException e) {
            log.error("Valor nulo un error en AdminController - prepareModification" + e.getMessage());
        }catch(Exception e){
            log.error("Ocurrio un error en AdminController - prepareModification" + e.getMessage());
        }

        return "adminViews/updateSite";
    }

    @Secured({ROLE_ADMIN})
    @PostMapping("/actualizarSitio")
    public String updateSite(@Valid @ModelAttribute("site") DTOSite site, BindingResult result, RedirectAttributes attributes, Model model) {
        model.addAttribute("opcion", "sitios");
        try{
            Message message =  (Message) stateService.listar().getBody();
            assert message != null;
            if (result.hasErrors()) {
                for (ObjectError error : result.getAllErrors()) {
                    log.error("Error: " + error.getDefaultMessage());
                }
                model.addAttribute("states", message.getResult());
                return "adminViews/updateSite";
            }
            Message response = (Message) siteService.editar(site).getBody();
            if (message.getType().equals("failed")) {
                model.addAttribute("message", response);
                model.addAttribute("states", message.getResult());
                return "adminViews/updateSite";
            }
            attributes.addFlashAttribute("message", message);
        }catch (NullPointerException e) {
            log.error("Valor nulo un error en AdminController - updateSite" + e.getMessage());
        }catch(Exception e){
            log.error("Ocurrio un error en AdminController - updateSite" + e.getMessage());
        }
        return "redirect:/admin/sitios";
    }


    @Secured({ROLE_ADMIN})
    @PostMapping("/cambiarEstadoSitio")
    public String changeStateSite(@RequestParam("idSite") Optional<Long> idSite, @RequestParam("statusSite") Optional<Boolean> statusSite, Model model, RedirectAttributes attributes) {
        try{
            if (!idSite.isPresent() || !statusSite.isPresent()) {
                attributes.addFlashAttribute("message", new Message("Ejecución fallida", "Ingresa valores válidos", "failed", 400, null));
                return "redirect:/admin/sitios";
            }

            BeanSite beanSite = new BeanSite();
            beanSite.setId(idSite.get());
            beanSite.setStatus(statusSite.get() ? 0 : 1);
            Message message = (Message) siteService.eliminar(beanSite).getBody();
            attributes.addFlashAttribute("message", message);

        }catch (NullPointerException e) {
            log.error("Valor nulo un error en AdminController - changeStateSite" + e.getMessage());
        }catch(Exception e){
            log.error("Ocurrio un error en AdminController - changeStateSite" + e.getMessage());
        }
        return "redirect:/admin/sitios";
    }


}
