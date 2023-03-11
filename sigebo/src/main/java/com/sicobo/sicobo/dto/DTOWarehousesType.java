package com.sicobo.sicobo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DTOWarehousesType {

    @NotBlank(message = "El campo no puede estar en blanco")
    private String description;

}
