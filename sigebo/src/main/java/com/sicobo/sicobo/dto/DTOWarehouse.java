package com.sicobo.sicobo.dto;


import com.sicobo.sicobo.util.ImageList;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class DTOWarehouse {

    private long id;

    @NotBlank(message = "El campo no debe estar en blanco")
    @Pattern(regexp = "^(?!\\s)(?!.*\\s$)[a-zA-Z0-9#%&.,áéíóúÁÉÍÓÚüÜñÑ\\s]+$", message = "Ingresa un valor válido")
    @Size(min = 3, max = 255, message = "El campo debe tener entre 3 y 255 caracteres")
    private String description;

    @NotBlank(message = "El campo no debe estar en blanco")
    @Pattern(regexp = "^(?!\\s)(?!.*\\s$)[a-zA-Z0-9#%&.,áéíóúÁÉÍÓÚüÜñÑ\\s]+$", message = "Ingresa un valor válido solo con alfanuméricos, carácteres especiales disponibles: &, % y #")
    @Size(min = 5, max = 100, message = "El campo debe tener entre 3 y 255 caracteres")
    private String section;


    @DecimalMin(value = "1.00", inclusive = true, message = "El valor mínimo permitido es 1.00")
    @Digits(integer = Integer.MAX_VALUE, fraction = 2, message = "El valor debe contener máximo dos decimales")
    @Positive(message = "Tiene que ser un número positivo")
    private Double finalCost;

    @PositiveOrZero(message = "Tiene que ser un número positivo o 0")
    private int status;

    @NotNull(message = "El campo no debe ser nulo")
    @Positive(message = "Tiene que ser un número positivo")
    private int beanSite;

    @NotNull(message = "El campo no debe ser nulo")
    @Positive(message = "Tiene que ser un número positivo")
    private int warehousesType;

    @NotNull(message = "El campo no debe ser nulo")
    @Size(min = 1, message = "Debes agregar al menos una imagen.")
    @Size(max = 3, message = "Solo se permiten hasta 3 imágenes.")
    @ImageList(message = "Solo se aceptan imágenes")
    private List<MultipartFile> images;

    private LocalDateTime fechaCreacion;

}
