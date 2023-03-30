package com.sicobo.sicobo.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class DTOCostType {


    @NotNull(message = "Campo obligatorio")
    @Positive(message = "Ingresa un valor válido")
    private Double amount;

    private int status;


    @Positive(message = "Ingresa un valor válido")
    private int beanWarehousesType;

}
