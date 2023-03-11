package com.sicobo.sicobo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DTOState {

    @NotBlank(message = "El campo no debe estar en blanco")
    private String name;

}
