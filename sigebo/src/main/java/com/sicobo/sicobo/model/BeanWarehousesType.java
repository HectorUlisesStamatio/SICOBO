package com.sicobo.sicobo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "warehouses_type")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class BeanWarehousesType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private String description;


    @OneToMany(mappedBy = "beanWarehousesType")
    @JsonIgnore
    private List<BeanCostType> beanCostTypes;

    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_act")
    private LocalDateTime fechaActualizacion;

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
        return "BeanWarehousesType{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", fechaCreacion=" + fechaCreacion +
                ", fechaActualizacion=" + fechaActualizacion +
                '}';
    }
}
