package com.sicobo.sicobo.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;


import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "warehouse_images")
public class BeanWarehouseImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String url;

    @Column(name = "secure_url")
    private String secureUrl;

    @Column(name = "public_id")
    private String publicId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
    @JoinColumn(name = "warehouse_id")
    private BeanWarehouse beanWarehouse;

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
        return "BeanWarehouseImage{" +
                "id=" + id +
                ", url='" + url + '\'' +
                ", secureUrl='" + secureUrl + '\'' +
                ", publicId='" + publicId + '\'' +
                ", beanWarehouse=" + beanWarehouse +
                ", fechaCreacion=" + fechaCreacion +
                ", fechaActualizacion=" + fechaActualizacion +
                '}';
    }
}
