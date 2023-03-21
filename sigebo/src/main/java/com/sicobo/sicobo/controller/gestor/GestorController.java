package com.sicobo.sicobo.controller.gestor;

import com.sicobo.sicobo.dto.DTOSite;
import com.sicobo.sicobo.dto.DTOWarehouse;
import com.sicobo.sicobo.serviceImpl.SiteServiceImpl;
import com.sicobo.sicobo.serviceImpl.StateServiceImpl;
import com.sicobo.sicobo.serviceImpl.WarehouseServiceImpl;
import com.sicobo.sicobo.util.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/gestor")
@Slf4j
public class GestorController {
    @Autowired
    private SiteServiceImpl siteService;

    @Autowired
    private WarehouseServiceImpl warehouseService;

    @GetMapping("/bodegas/{idSite}")
    public String listWarehouses(Model model, @PathVariable(required = false) String idSite){
        model.addAttribute("response", warehouseService.listWarehousesBySiteId(Long.parseLong(idSite)).getBody());
        model.addAttribute("status", warehouseService.listWarehousesBySiteId(Long.parseLong(idSite)).getStatusCode());

        return "gestorViews/listWarehouses";
    }

    @GetMapping("/bodegaRegistro/{idSite}")
    public String prepareWarehouseRegister(Model model, DTOWarehouse warehouse){
        Message response = (Message) stateService.listar().getBody();
        model.addAttribute("states", response.getResult());
        model.addAttribute("site", site);
        return "adminViews/registerSite";
    }

}
