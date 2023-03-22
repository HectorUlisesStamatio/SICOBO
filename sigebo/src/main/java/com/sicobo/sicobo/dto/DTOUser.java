package com.sicobo.sicobo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class DTOUser {

    @NotBlank(message = "El campo no puede estar vacío")
    private String name;

    @NotBlank(message = "El campo no puede estar vacío")
    private String lastname;

    @NotBlank(message = "El campo no puede estar vacío")
    private String surname;

    @NotBlank(message = "El campo no puede estar vacío")
    private String email;

    @Length(max = 13)
    @NotBlank(message = "El campo no puede estar vacío")
    private String rfc;

    @Length(max = 12,message = "Solo se permite máximo 12 caracteres")
    @NotBlank(message = "El campo no puede estar vacío")
    private String phone_number;

    @Length(max = 3, message = "Solo se permite máximo 3 caracteres")
    private String ext;

    @PositiveOrZero(message = "Tiene que ser un número positivo o 0")
    private int enabled;

    @NotBlank(message = "El campo no puede estar vacío")
    private String username;

    @Length(min = 10)
    @NotBlank(message = "El campo no puede estar vacío")
    @Pattern(regexp = "^(?=^.{10,}$)(?=.*\\d)(?=.*[!@#$%^&*]+)(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*$", message = "Ingresa una contraseña válida")
    private String password;

    private String role;

    private int number_attempts;

    @PositiveOrZero(message = "Tiene que ser un número positivo o 0")
    private int policy_acceptance;





}
