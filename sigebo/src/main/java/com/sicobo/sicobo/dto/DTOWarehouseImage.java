package com.sicobo.sicobo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class DTOWarehouseImage {
    @NotBlank(message = "El campo no puede estar en blanco")
    private String url;

    @NotNull(message = "El campo no debe ser nulo")
    @PositiveOrZero(message = "Tiene que ser un número positivo o 0")
    private  int beanWarehouse;

    @NotBlank(message = "El campo no puede estar en blanco")
    private String secureUrl;

    @NotBlank(message = "El campo no puede estar en blanco")
    private String publicId;

}
