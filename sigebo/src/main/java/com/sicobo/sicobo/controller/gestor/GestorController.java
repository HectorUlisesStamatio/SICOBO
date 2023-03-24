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
import org.springframework.security.access.annotation.Secured;
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
    public static final String ROLE_GESTOR = "ROLE_GESTOR";

    @Autowired
    private SiteServiceImpl siteService;
    @Autowired
    private WarehouseServiceImpl warehouseService;

    @Secured({ROLE_GESTOR})
    @GetMapping("/bodegas")
    public String listWarehouses(Model model){
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();

            String username = ((User) auth.getPrincipal()).getUsername();
            String pass = ((User) auth.getPrincipal()).getPassword();

            Message message = (Message) warehouseService.listWarehousesBySiteId(username, pass).getBody();
            model.addAttribute("response", message);
            Object status = warehouseService.listWarehousesBySiteId(username, pass).getStatusCode();
            model.addAttribute("status", status);
        } catch (Exception e) {
            log.error("Ocurrio un error en GestorController - listWarehouses" + e.getMessage());
        }
        return "gestorViews/listWarehouses";
    }



}
