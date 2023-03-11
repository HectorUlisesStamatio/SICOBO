package com.sicobo.sicobo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class DTOSite {
    @NotBlank(message = "El campo no debe estar en blanco")
    private String name;

    @PositiveOrZero(message = "Tiene que ser un número positivo o 0")
    private int status;


    @NotBlank(message = "El campo no debe estar en blanco")
    private String address;

    @NotNull(message = "El campo no debe ser nulo")
    @PositiveOrZero(message = "Tiene que ser un número positivo o 0")
    private int beanState;
}
