package com.sicobo.sicobo.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class DTOSite {

    private Long id;

    @NotBlank(message = "El campo no debe estar en blanco")
    @Pattern(regexp = "^(?!\\s)(?!.*\\s$)[a-zA-Z0-9&%#\\s]+$", message = "Ingresa un valor válido")
    @Size(min = 3, max = 255, message = "El nombre debe tener entre 3 y 255 caracteres")
    private String name;

    @PositiveOrZero(message = "Tiene que ser un número positivo o 0")
    private int status;


    @NotBlank(message = "El campo no debe estar en blanco")
    @Pattern(regexp = "^(?!\\s)(?!.*\\s$)[a-zA-Z0-9#%&.,áéíóúÁÉÍÓÚüÜñÑ\\s]*$", message = "Ingresa un valor válido")
    @Size(min = 10, max = 255, message = "La dirección debe tener entre 10 y 255 caracteres")
    private String address;

    @NotNull(message = "El campo no debe ser nulo")
    @Positive(message = "Selecciona un valor válido")
    private int beanState;
}
