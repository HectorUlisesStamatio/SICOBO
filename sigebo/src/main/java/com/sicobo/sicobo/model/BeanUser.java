package com.sicobo.sicobo.model;

import jakarta.persistence.*;
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

    @Length(max = 13)
    private String rfc;


    @Length(max = 15)
    @Column(name = "phone_number")
    private String phoneNumber;

    private String username;

    @Length(min = 10)
    private String password;

    private String role;


    @Column(name = "number_attempts")
    private int numberAttempts;

    @Column(columnDefinition = "integer default 1", name = "policy_acceptance")
    private int policyAcceptance;

    @Column(columnDefinition = "integer default 1")
    private int enabled;

    @OneToOne(mappedBy = "beanUser")
    private BeanSiteAssigment beanSiteAssigment;


    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_act")
    private LocalDateTime fechaActualizacion;

    public BeanUser() {

    }

    public BeanUser(String name, String lastname, String surname, String email, String rfc, String phoneNumber, String username, String password, String role, int numberAttempts, int policyAcceptance, int enabled) {
        this.name = name;
        this.lastname = lastname;
        this.surname = surname;
        this.email = email;
        this.rfc = rfc;
        this.phoneNumber = phoneNumber;
        this.username = username;
        this.password = password;
        this.role = role;
        this.numberAttempts = numberAttempts;
        this.policyAcceptance = policyAcceptance;
        this.enabled = enabled;
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
