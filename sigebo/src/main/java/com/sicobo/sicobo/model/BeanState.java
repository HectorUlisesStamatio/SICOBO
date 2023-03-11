package com.sicobo.sicobo.model;


import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "state")
public class BeanState {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

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

}
