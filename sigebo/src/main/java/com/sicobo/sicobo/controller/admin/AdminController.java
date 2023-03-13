package com.sicobo.sicobo.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {


    @GetMapping("/sitios")
    public String listar(){

        return "adminViews/listSites";
    }

}
