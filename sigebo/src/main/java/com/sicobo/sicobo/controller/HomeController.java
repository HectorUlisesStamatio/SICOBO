
package com.sicobo.sicobo.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Objects;

@Controller
public class HomeController {

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
    public String register(Model model){
        return "register";
    }
}