package com.sicobo.sicobo.model;


import lombok.Data;

import java.util.Date;

@Data
public class BeanListPayments {

    private String fullName;

    private Date dueDate;

    private Date paymentDate;

    private Long amountMonths;

    private Long status;

    private String section;

    private double finalCost;

    private String siteName;

    private String stateName;

    public BeanListPayments() {
    }

    public BeanListPayments(String fullName, Date dueDate, Date paymentDate, Long amountMonths, Long status, String section, double finalCost, String siteName, String stateName) {
        this.fullName = fullName;
        this.dueDate = dueDate;
        this.paymentDate = paymentDate;
        this.amountMonths = amountMonths;
        this.status = status;
        this.section = section;
        this.finalCost = finalCost;
        this.siteName = siteName;
        this.stateName = stateName;
    }
}
