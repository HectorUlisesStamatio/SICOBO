package com.sicobo.sicobo.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class DTOCostType {

    @PositiveOrZero(message = "El campo tiene que ser mínimo de 0")
    private Double amount;

    @PositiveOrZero(message = "Tiene que ser un número positivo o 0")
    private int status;

    @NotNull(message = "El campo no debe ser nulo")
    @PositiveOrZero(message = "Tiene que ser un número positivo o 0")
    private int beanWarehousesType;

}
