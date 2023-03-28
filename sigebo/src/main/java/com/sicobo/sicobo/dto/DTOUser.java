package com.sicobo.sicobo.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class DTOUser {

    @NotBlank(message = "El campo no puede estar vacío")
    @Pattern(regexp = "^(?!\\s)(?!.*\\s$)[a-zA-ZñÑáéíóúüÁÉÍÓÚÜ0-9&%#\\s]+$", message = "Ingresa un nombre válido")
    private String name;

    @NotBlank(message = "El campo no puede estar vacío")
    @Pattern(regexp = "^(?!\\s)(?!.*\\s$)[a-zA-ZñÑáéíóúüÁÉÍÓÚÜ0-9&%#\\s]+$", message = "Ingresa un apellido válido")
    private String lastname;

    @NotBlank(message = "El campo no puede estar vacío")
    @Pattern(regexp = "^(?!\\s)(?!.*\\s$)[a-zA-ZñÑáéíóúüÁÉÍÓÚÜ0-9&%#\\s]+$", message = "Ingresa un apellido válido")
    private String surname;

    @Email
    @NotBlank(message = "El campo no puede estar vacío")
    private String email;

    @Length(max = 13)
    @NotBlank(message = "El campo no puede estar vacío")
    @Pattern(regexp = "^([A-ZÑ&]{3,4}) ?(?:- ?)?(\\d{2}(?:0[1-9]|1[0-2])(?:0[1-9]|[12]\\d|3[01])) ?(?:- ?)?([A-Z\\d]{2})([A\\d])$", message = "Ingresa un rfc válido")
    private String rfc;

    @Length(max = 12,message = "Solo se permite máximo 12 caracteres")
    @NotBlank(message = "El campo no puede estar vacío")
    @Pattern(regexp = "[0-9]{1,12}", message = "Ingresa un teléfono válido")
    private String phoneNumber;

    @Length(max = 3, message = "Solo se permite máximo 3 caracteres")
    @Pattern(regexp = "[0-9]{0,3}", message = "Ingresa una extensión válida")
    private String ext;

    @PositiveOrZero(message = "Tiene que ser un número positivo o 0")
    private int enabled;

    @NotBlank(message = "El campo no puede estar vacío")
    @Pattern(regexp = "^(?!\\s)(?!.*\\s$)[a-zA-ZñÑáéíóúüÁÉÍÓÚÜ0-9&%#\\s]+$", message = "Ingresa un usuario válido")
    private String username;

    @Length(min = 10)
    @NotBlank(message = "El campo no puede estar vacío")
    @Pattern(regexp = "^(?=^.{10,}$)(?=.*\\d)(?=.*[!@#$%^&*]+)(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*$", message = "Ingresa una contraseña válida")
    private String password;

    private String role;

    private int numberAttempts;

    @NotNull(message = "Tienes que aceptar los términos y condiciones")
    private int policyAcceptance;





}
