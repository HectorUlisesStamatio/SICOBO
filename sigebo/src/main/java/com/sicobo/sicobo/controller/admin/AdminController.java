package com.sicobo.sicobo.controller.admin;

import com.sicobo.sicobo.dto.DTOSite;
import com.sicobo.sicobo.model.BeanSite;
import com.sicobo.sicobo.serviceImpl.SiteServiceImpl;
import com.sicobo.sicobo.serviceImpl.StateServiceImpl;
import com.sicobo.sicobo.util.Message;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;


@Controller
@RequestMapping("/admin")
@Slf4j
public class AdminController {

    public static final String ROLE_ADMIN = "ROLE_ADMIN";

    @Autowired
    private SiteServiceImpl siteService;

    @Autowired
    private StateServiceImpl stateService;

    @Secured({ROLE_ADMIN})
    @GetMapping("/dashboard")
    public String dash(Model model){
        model.addAttribute("opcion",null);
        return "adminViews/dashboard";
    }

    @Secured({ROLE_ADMIN})
    @GetMapping("/sitios")
    public String listar(Model model){
        model.addAttribute("opcion","sitios");
        model.addAttribute("response", siteService.listar().getBody());
        model.addAttribute("status", siteService.listar().getStatusCode());
        return "adminViews/listSites";
    }

    @Secured({ROLE_ADMIN})
    @GetMapping("/registrarSitio")
    public String prepareRegistration(Model model, DTOSite site){
        model.addAttribute("opcion","sitios");
        Message response = (Message) stateService.listar().getBody();
        model.addAttribute("states", response.getResult());
        model.addAttribute("site", site);
        return "adminViews/registerSite";
    }

    @Secured({ROLE_ADMIN})
    @PostMapping("/guardarSitio")
    public String saveSite(@Valid @ModelAttribute("site") DTOSite site, BindingResult result, RedirectAttributes attributes, Model model){
        model.addAttribute("opcion","sitios");
        if(result.hasErrors()){
            for (ObjectError error: result.getAllErrors()){
                log.error("Error: " + error.getDefaultMessage());
            }
            model.addAttribute("states", ((Message) stateService.listar().getBody()).getResult());
            return "adminViews/registerSite";
        }
        Message message = (Message) siteService.guardar(site).getBody();
        if(message.getType().equals("failed")){
            model.addAttribute("message", message);
            model.addAttribute("states", ((Message) stateService.listar().getBody()).getResult());
            return "adminViews/registerSite";
        }
        attributes.addFlashAttribute("message", message);
        return "redirect:/admin/sitios";
    }

    @Secured({ROLE_ADMIN})
    @PostMapping("/prepararModificacion")
    public String prepareModification( @RequestParam("idSite") long idSite, Model model, RedirectAttributes attributes){
        model.addAttribute("opcion","sitios");
        Message message = (Message) siteService.buscar(idSite).getBody();
        if(message.getType().equals("failed")){
            attributes.addFlashAttribute("message", message);
            return "redirect:/admin/sitios";
        }
        model.addAttribute("response", message);
        model.addAttribute("states", ((Message) stateService.listar().getBody()).getResult());
        model.addAttribute("site", message.getResult());
        return "adminViews/updateSite";
    }

    @Secured({ROLE_ADMIN})
    @PostMapping("/actualizarSitio")
    public String updateSite(@Valid @ModelAttribute("site") DTOSite site, BindingResult result, RedirectAttributes attributes, Model model){
        model.addAttribute("opcion","sitios");
        if(result.hasErrors()){
            for (ObjectError error: result.getAllErrors()){
                log.error("Error: " + error.getDefaultMessage());
            }
            model.addAttribute("states", ((Message) stateService.listar().getBody()).getResult());
            return "adminViews/updateSite";
        }
        Message message = (Message) siteService.editar(site).getBody();
        if(message.getType().equals("failed")){
            model.addAttribute("message", message);
            model.addAttribute("states", ((Message) stateService.listar().getBody()).getResult());
            return "adminViews/updateSite";
        }
        attributes.addFlashAttribute("message", message);
        return "redirect:/admin/sitios";
    }


    @Secured({ROLE_ADMIN})
    @PostMapping("/cambiarEstadoSitio")
    public String changeStateSite(@RequestParam("idSite") Optional<Long> idSite, @RequestParam("statusSite") Optional<Boolean> statusSite, Model model, RedirectAttributes attributes){
        if(!idSite.isPresent() || !statusSite.isPresent()){
            attributes.addFlashAttribute("message", new Message("Ejecución fallida", "Ingresa valores válidos", "failed", 400, null));
            return "redirect:/admin/sitios";
        }

        BeanSite beanSite = new BeanSite();
        beanSite.setId(idSite.get());
        beanSite.setStatus(statusSite.get() ? 0 : 1);

        Message message = (Message) siteService.eliminar(beanSite).getBody();
        attributes.addFlashAttribute("message", message);
        return "redirect:/admin/sitios";
    }


}
