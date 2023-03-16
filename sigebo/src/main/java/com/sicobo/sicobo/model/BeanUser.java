package com.sicobo.sicobo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "users")
public class BeanUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String lastname;

    private String surname;

    private String email;

    @Column(length = 13)
    private String rfc;

    @Column(length = 15)
    private String phone_number;

    @Column(unique = true)
    private String username;

    @Length(min = 12)
    private String password;

    @Enumerated(EnumType.STRING)
    private Rol role;

    private int number_attempts;

    @Column(columnDefinition = "integer default 1")
    private int policy_acceptance;

    @Column(columnDefinition = "integer default 1")
    private int enabled;

    @OneToOne(mappedBy = "beanUser")
    private BeanSiteAssigment beanSiteAssigment;


    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_act")
    private LocalDateTime fechaActualizacion;

    public enum Rol {
        ADMIN,
        GESTOR,
        USUARIO
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
