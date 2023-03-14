package com.sicobo.sicobo.controller.admin;

import com.sicobo.sicobo.serviceImpl.SiteServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private SiteServiceImpl siteService;

    @GetMapping("/sitios")
    public String listar(Model model){
        model.addAttribute("response", siteService.listar().getBody());
        model.addAttribute("status", siteService.listar().getStatusCode());
        return "adminViews/listSites";
    }

}
