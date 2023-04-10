package com.sicobo.sicobo.model;

import lombok.Data;

@Data
public class BeanWarehouseDetails {
    private String final_cost;
    private String section;
    private String description;
    private String secure_url;
    private String costo;
    private String addres;
    private String name;
    private String id;

    public BeanWarehouseDetails() {
    }

    public BeanWarehouseDetails(String final_cost, String section, String description, String secure_url, String costo, String addres, String name, String id ) {
        this.final_cost = final_cost;
        this.section = section;
        this.description = description;
        this.secure_url = secure_url;
        this.costo = costo;
        this.addres = addres;
        this.name = name;
        this.id = id;
    }
}
