
package com.sicobo.sicobo.controller;


import com.sicobo.sicobo.dto.DTOUser;
import com.sicobo.sicobo.serviceImpl.UserServiceImpl;
import com.sicobo.sicobo.util.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Objects;

@Controller
public class HomeController {

    @Autowired
    private UserServiceImpl userService;

    private static final Logger log = LoggerFactory.getLogger(HomeController.class);

    @GetMapping("/")
    public String inicio(Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String rol = auth.getAuthorities().toString();
        try{
            if(Objects.equals(rol, "[ROLE_ADMIN]")) {
                return "adminViews/dashboard";
            }else if(Objects.equals(rol, "[ROLE_GESTOR]")) {
                return "paginaEnBlanco";
            }else if(Objects.equals(rol, "[ROLE_USUARIO]")) {
                return "index";
            }else{
                return "index";
            }
        }catch (Exception E){
            log.warn("No se pudo realizar la validación del rol");
        }
        return "index";
    }

    @GetMapping("/register")
    public String register(Model model, DTOUser user){
        model.addAttribute("user", user);
        return "register";
    }

    @PostMapping("/registrarUsuario")
    public String registrarUsuario(@Validated @ModelAttribute("user") DTOUser user, BindingResult result, RedirectAttributes attributes, Model model){

        if(result.hasErrors()){
            for (ObjectError error: result.getAllErrors()){
                log.error("Error: " + error.getDefaultMessage());
            }
            return "register";
        }
        Message message = (Message) userService.registrar(user).getBody();
        assert message != null;
        if(message.getType().equals("failed")){
            model.addAttribute("message", message);
            return "register";
        }
        return "redirect:/login";
    }
}