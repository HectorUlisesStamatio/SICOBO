package com.sicobo.sicobo.model;


import lombok.Data;

@Data
public class BeanGestorInfo {

    private Long userId;
    private String siteName;

    public BeanGestorInfo() {
    }

    public BeanGestorInfo(Long userId, String siteName) {
        this.userId = userId;
        this.siteName = siteName;
    }
}
