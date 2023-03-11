package com.sicobo.sicobo.model;

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
    private BeanUser beanUser;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "site_id")
    private BeanSite beanSite;


    @Column(columnDefinition = "integer default 1")
    private int status;

}
