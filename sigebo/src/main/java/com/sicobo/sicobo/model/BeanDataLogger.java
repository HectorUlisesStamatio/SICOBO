package com.sicobo.sicobo.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Date;

@Data
@Entity
@Table(name = "data_logging")
public class BeanDataLogger {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Date date;

    @Column(nullable = false, name = "table_used")
    private String table;

    @Column(nullable = false)
    private String action;

    @Column(nullable = false, name = "user_used")
    private String user;

    @Column(columnDefinition = "longtext", name = "old_values")
    private String oldValues;

    @Column(columnDefinition = "longtext", name = "new_values")
    private String newValues;


    @Column(nullable = false)
    private String description;

}
