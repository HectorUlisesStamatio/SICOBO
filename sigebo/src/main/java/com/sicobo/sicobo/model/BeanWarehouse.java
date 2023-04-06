package com.sicobo.sicobo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "warehouse")
public class BeanWarehouse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    private String section;

    private Double finalCost;

    @Column(columnDefinition = "integer default 1")
    private int status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
    @JoinColumn(name = "site_id_site")
    private BeanSite beanSite;


    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
    @JoinColumn(name = "warehouses_types")
    private BeanWarehousesType warehousesType;

    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_act")
    private LocalDateTime fechaActualizacion;


    @OneToMany(mappedBy = "beanWarehouse", cascade = CascadeType.ALL)
    @JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
    @JsonIgnore
    private List<BeanWarehouseImage> images;

    @PrePersist
    private void prePersist(){
        this.fechaCreacion = LocalDateTime.now();
    }

    @PreUpdate
    private void preUpdate(){
        this.fechaActualizacion = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "BeanWarehouse{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", section='" + section + '\'' +
                ", finalCost=" + finalCost +
                ", status=" + status +
                ", beanSite=" + beanSite +
                ", warehousesType=" + warehousesType +
                ", fechaCreacion=" + fechaCreacion +
                ", fechaActualizacion=" + fechaActualizacion +
                ", images=" + images +
                '}';
    }
}
