package com.sicobo.sicobo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PruebasController {

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
