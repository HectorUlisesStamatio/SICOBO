package com.sicobo.sicobo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "site")
public class BeanSite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;


    @Column(columnDefinition = "integer default 1")
    private int status;

    private String address;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
    @JoinColumn(name = "states_id")
    private BeanState beanState;

    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_act")
    private LocalDateTime fechaActualizacion;

    public BeanSite() {

    }
    public BeanSite(Long id, String name, int status, String address, BeanState beanState) {
        this.name = name;
        this.status = status;
        this.address = address;
        this.beanState = beanState;
        this.id =id;
    }



    public BeanSite(String name, int status, String address, BeanState beanState) {
        this.name = name;
        this.status = status;
        this.address = address;
        this.beanState = beanState;
    }

    @PrePersist
    private void prePersist(){
        this.fechaCreacion = LocalDateTime.now();
    }

    @PreUpdate
    private void preUpdate(){
        this.fechaActualizacion = LocalDateTime.now();
    }

}
