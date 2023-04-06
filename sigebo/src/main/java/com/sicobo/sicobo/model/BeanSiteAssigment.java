package com.sicobo.sicobo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "site_assigment")
public class BeanSiteAssigment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private BeanUser beanUser;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "site_id")
    @JsonIgnore
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
