package com.sicobo.sicobo.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "authorities")
public class BeanAuthorities {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String username;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private int enabled;
    @Column(nullable = false)
    private String authority;

    public BeanAuthorities() {

    }

    public BeanAuthorities(String username, String password, int enabled, String authority) {
        this.username = username;
        this.password = password;
        this.enabled = enabled;
        this.authority = authority;
    }


}