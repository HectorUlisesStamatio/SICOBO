package com.sicobo.sicobo.controller.admin;

import com.sicobo.sicobo.dto.DTOSite;
import com.sicobo.sicobo.serviceImpl.SiteServiceImpl;
import com.sicobo.sicobo.serviceImpl.StateServiceImpl;
import com.sicobo.sicobo.util.Message;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/admin")
@Slf4j
public class AdminController {

    @Autowired
    private SiteServiceImpl siteService;

    @Autowired
    private StateServiceImpl stateService;


    @GetMapping("/dashboard")
    public String dash(Model model){
        model.addAttribute("opcion",null);
        return "adminViews/dashboard";
    }

    @GetMapping("/sitios")
    public String listar(Model model){
        model.addAttribute("opcion","sitios");
        model.addAttribute("response", siteService.listar().getBody());
        model.addAttribute("status", siteService.listar().getStatusCode());
        return "adminViews/listSites";
    }

    @GetMapping("/registrarSitio")
    public String prepareRegistration(Model model, DTOSite site){
        model.addAttribute("opcion","sitios");
        Message response = (Message) stateService.listar().getBody();
        model.addAttribute("states", response.getResult());
        model.addAttribute("site", site);
        return "adminViews/registerSite";
    }

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

    @PostMapping("/actualizarSitio")
    public String updateSite(@Valid @ModelAttribute("site") DTOSite site, BindingResult result, RedirectAttributes attributes, Model model){
        model.addAttribute("opcion","sitios");
        if(result.hasErrors()){
            for (ObjectError error: result.getAllErrors()){
                log.error("Error: " + error.getDefaultMessage());
            }
            log.info("Entro en fallos de datos");
            model.addAttribute("states", ((Message) stateService.listar().getBody()).getResult());
            return "adminViews/updateSite";
        }
        Message message = (Message) siteService.editar(site).getBody();
        if(message.getType().equals("failed")){
            log.info("Entro en fallos de editar");
            model.addAttribute("message", message);
            model.addAttribute("states", ((Message) stateService.listar().getBody()).getResult());
            return "adminViews/updateSite";
        }
        attributes.addFlashAttribute("message", message);
        return "redirect:/admin/sitios";
    }


}
