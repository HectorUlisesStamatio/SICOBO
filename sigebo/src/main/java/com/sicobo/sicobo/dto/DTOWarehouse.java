package com.sicobo.sicobo.dto;

import com.sicobo.sicobo.model.BeanWarehouseImage;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class DTOWarehouse {

    @NotBlank(message = "El campo no debe estar en blanco")
    @Pattern(regexp = "^(?!\\s)(?!.*\\s$)[a-zA-Z0-9&%#\\s]+$", message = "Ingresa un valor válido")
    @Size(min = 3, max = 255, message = "El campo debe tener entre 3 y 255 caracteres")
    private String description;

    @NotBlank(message = "El campo no debe estar en blanco")
    @Pattern(regexp = "^(?!\\s)(?!.*\\s$)[a-zA-Z0-9&%#\\s]+$", message = "Ingresa un valor válido solo con alfanuméricos, carácteres especiales disponibles: &, % y #")
    @Size(min = 5, max = 100, message = "El campo debe tener entre 3 y 255 caracteres")
    private String section;

    @PositiveOrZero(message="El campo tiene que como mínimo valer 0")
    private Double finalCost;

    @PositiveOrZero(message = "Tiene que ser un número positivo o 0")
    private int status;

    @NotNull(message = "El campo no debe ser nulo")
    @PositiveOrZero(message = "Tiene que ser un número positivo o 0")
    private int beanSite;

    @NotNull(message = "El campo no debe ser nulo")
    @PositiveOrZero(message = "Tiene que ser un número positivo o 0")
    private int warehousesType;

    @NotNull(message = "El campo no debe ser nulo")
    @Size(min = 1, message = "Debes agregar al menos una imagen")
    private List<MultipartFile> images;
}
