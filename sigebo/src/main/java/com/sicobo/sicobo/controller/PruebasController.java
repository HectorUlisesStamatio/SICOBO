package com.sicobo.sicobo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PruebasController {


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
