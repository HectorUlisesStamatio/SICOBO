package com.sicobo.sicobo.controller.gestor;

import com.sicobo.sicobo.dto.DTOSite;
import com.sicobo.sicobo.dto.DTOWarehouse;
import com.sicobo.sicobo.model.BeanAuthorities;
import com.sicobo.sicobo.model.BeanUser;
import com.sicobo.sicobo.serviceImpl.SiteServiceImpl;
import com.sicobo.sicobo.serviceImpl.StateServiceImpl;
import com.sicobo.sicobo.serviceImpl.WarehouseServiceImpl;
import com.sicobo.sicobo.util.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/gestor")
@Slf4j
public class GestorController {
    @Autowired
    private SiteServiceImpl siteService;

    @Autowired
    private WarehouseServiceImpl warehouseService;

    @GetMapping("/bodegas")
    public String listWarehouses(Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        String username = ((User) auth.getPrincipal()).getUsername();
        String pass = ((User) auth.getPrincipal()).getPassword();


        model.addAttribute("response", warehouseService.listWarehousesBySiteId(Long.parseLong(idSite)).getBody());
        model.addAttribute("status", warehouseService.listWarehousesBySiteId(Long.parseLong(idSite)).getStatusCode());
        return "gestorViews/listWarehouses";
    }



}
