package com.sicobo.sicobo.model;

import lombok.Data;

import java.util.List;
@Data
public class BeanSiteOcupation {

    private BeanSite site;

    private List<BeanWarehouse> warehouses;

    public BeanSiteOcupation(BeanSite site, List<BeanWarehouse> warehouses) {
        this.site = site;
        this.warehouses = warehouses;
    }
}
