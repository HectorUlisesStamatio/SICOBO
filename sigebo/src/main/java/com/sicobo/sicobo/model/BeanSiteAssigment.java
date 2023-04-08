package com.sicobo.sicobo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "site_assigment")
public class BeanSiteAssigment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
    @JoinColumn(name = "user_id")
    private BeanUser beanUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
    @JoinColumn(name = "site_id")
    private BeanSite beanSite;

    @Column(columnDefinition = "integer default 1")
    private int status;

    public BeanSiteAssigment() {
    }

    public BeanSiteAssigment(BeanUser beanUser, BeanSite beanSite) {
        this.beanUser = beanUser;
        this.beanSite = beanSite;
    }
}
