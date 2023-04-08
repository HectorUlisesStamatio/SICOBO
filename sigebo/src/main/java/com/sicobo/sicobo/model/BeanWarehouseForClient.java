package com.sicobo.sicobo.model;


import lombok.Data;

import java.util.List;

@Data
public class BeanWarehouseForClient {

    private String siteName;

    private String warehouseDescription;

    private String warehouseTypeDescription;

    private String warehouseStatus;

    private String stateName;

    private List<String> warehouseImageUrl;

    public BeanWarehouseForClient() {
    }

    public BeanWarehouseForClient(String siteName, String warehouseDescription, String warehouseTypeDescription, String warehouseStatus, String stateName, List<String> warehouseImageUrl) {
        this.siteName = siteName;
        this.warehouseDescription = warehouseDescription;
        this.warehouseTypeDescription = warehouseTypeDescription;
        this.warehouseStatus = warehouseStatus;
        this.stateName = stateName;
        this.warehouseImageUrl = warehouseImageUrl;
    }
}
