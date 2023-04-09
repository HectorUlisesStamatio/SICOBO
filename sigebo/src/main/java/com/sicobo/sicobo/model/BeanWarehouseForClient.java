package com.sicobo.sicobo.model;


import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
public class BeanWarehouseForClient {

    private String siteName;

    private String warehouseDescription;

    private String warehouseTypeDescription;

    private String warehouseStatus;

    private String stateName;

    private List<String> warehouseImageUrl;

    private Long paymentId;

    private Date dueDate;

    private Date paymentDate;

    public BeanWarehouseForClient() {
    }

    public BeanWarehouseForClient(String siteName, String warehouseDescription, String warehouseTypeDescription, String warehouseStatus, String stateName, List<String> warehouseImageUrl, Long paymentId, Date dueDate, Date paymentDate) {
        this.siteName = siteName;
        this.warehouseDescription = warehouseDescription;
        this.warehouseTypeDescription = warehouseTypeDescription;
        this.warehouseStatus = warehouseStatus;
        this.stateName = stateName;
        this.warehouseImageUrl = warehouseImageUrl;
        this.paymentId = paymentId;
        this.dueDate = dueDate;
        this.paymentDate = paymentDate;
    }
}
