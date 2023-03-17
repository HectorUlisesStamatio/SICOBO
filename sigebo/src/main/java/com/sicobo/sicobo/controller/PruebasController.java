package com.sicobo.sicobo.controller;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Objects;

@Controller
public class PruebasController {


    @GetMapping("/")
    public String inicio(Model model){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String rol = auth.getAuthorities().toString();
        try{
            System.out.println();
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
            System.out.println("No se pudo");
        }

        return "index";
    }

    @GetMapping("/dash")
    public String dash(Model model){
        return "adminViews/dashboard";
    }
    @GetMapping("/login")
    public String login(Model model){
        return "login";
    }

    @GetMapping("/register")
    public String register(Model model){
        return "register";
    }

    @GetMapping("/blanco")
    public String blanco(Model model){
        return "paginaEnBlanco";
    }

    @GetMapping("/bodegas/blanco")
    public String bblanco(Model model){
        return "paginaEnBlanco";
    }

    @GetMapping("/gestor/blanco")
    public String gblanco(Model model){
        return "paginaEnBlanco";
    }

    @GetMapping("/admin/blanco")
    public String ablanco(Model model){
        return "paginaEnBlanco";
    }
}
