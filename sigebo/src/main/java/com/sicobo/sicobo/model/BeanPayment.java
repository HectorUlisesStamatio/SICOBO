package com.sicobo.sicobo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@Entity
@Table(name = "payment")
public class BeanPayment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double amount;

    private int amountMonths;

    private Date paymentDate;

    private Date dueDate;


    @Column(columnDefinition = "integer default 1")
    private int status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
    @JoinColumn(name = "warehouse_id")
    private BeanWarehouse beanWarehouse;


    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
    @JoinColumn(name = "user_id")
    private BeanUser beanUser;

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
