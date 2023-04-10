package com.sicobo.sicobo.controller.user;

import com.sicobo.sicobo.serviceimpl.WarehouseServiceImpl;
import com.sicobo.sicobo.util.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import static com.sicobo.sicobo.util.Constantes.Redirects.*;
import static com.sicobo.sicobo.util.Constantes.Roles.ROLE_USUARIO;
import static com.sicobo.sicobo.util.Constantes.Stuff.*;

@Controller
@Slf4j
public class UserController {
    private final WarehouseServiceImpl warehouseService;

    @Autowired
    public UserController(WarehouseServiceImpl warehouseService) {
        this.warehouseService = warehouseService;
    }

    @Secured({ROLE_USUARIO})
    @GetMapping("/misBodegas")
    public String misBodegas(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Message warehousesClient = (Message) warehouseService.buscarBodegaPorUsername(auth.getName()).getBody();

        System.out.println(warehousesClient);

        model.addAttribute(RESPONSE, warehousesClient.getResult());

        return MIS_BODEGAS;
    }
}
