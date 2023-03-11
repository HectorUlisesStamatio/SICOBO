package com.sicobo.sicobo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class DTOPolicies {
    @NotBlank(message = "No debe estar vacío")
    private String description;

    @PositiveOrZero(message = "Tiene que ser un número positivo o 0")
    private int status;


}
