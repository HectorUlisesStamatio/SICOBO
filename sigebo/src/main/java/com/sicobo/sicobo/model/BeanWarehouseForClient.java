package com.sicobo.sicobo.model;


import lombok.Data;

import java.util.Date;


@Data
public class BeanWarehouseForClient {

    private String siteName;

    private String warehouseDescription;

    private String warehouseTypeDescription;

    private String warehouseStatus;

    private String stateName;

    private String warehouseImageUrl;

    private Long paymentId;

    private Date dueDate;

    private Date paymentDate;

    private Long paymentStatus;

    private boolean isRenovation;

    public BeanWarehouseForClient() {
    }

    public BeanWarehouseForClient(String siteName, String warehouseDescription, String warehouseTypeDescription, String warehouseStatus, String stateName, String warehouseImageUrl, Long paymentId, Date dueDate, Date paymentDate, Long paymentStatus, boolean isRenovation) {
        this.siteName = siteName;
        this.warehouseDescription = warehouseDescription;
        this.warehouseTypeDescription = warehouseTypeDescription;
        this.warehouseStatus = warehouseStatus;
        this.stateName = stateName;
        this.warehouseImageUrl = warehouseImageUrl;
        this.paymentId = paymentId;
        this.dueDate = dueDate;
        this.paymentDate = paymentDate;
        this.paymentStatus = paymentStatus;
        this.isRenovation = isRenovation;
    }
}
