package com.sicobo.sicobo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class DTOUser {

    @NotBlank(groups = {Modify.class}, message = "El campo no puede estar vacío")
    private String name;

    @NotBlank(groups = {Modify.class}, message = "El campo no puede estar vacío")
    private String lastname;

    @NotBlank(groups = {Modify.class}, message = "El campo no puede estar vacío")
    private String surname;

    @NotBlank(groups = {Register.class}, message = "El campo no puede estar vacío")
    private String email;

    @Length(groups = {Modify.class}, max = 13)
    @NotBlank(groups = {Modify.class}, message = "El campo no puede estar vacío")
    private String rfc;

    @Length(groups = {Modify.class}, max = 15)
    @NotBlank(groups = {Modify.class}, message = "El campo no puede estar vacío")
    private String phone_number;

    @PositiveOrZero(groups =  {Modify.class},message = "Tiene que ser un número positivo o 0")
    private int enabled;

    @NotBlank(groups = {Register.class}, message = "El campo no puede estar vacío")
    private String username;

    @Length(groups = {Register.class}, min = 12)
    @NotBlank(groups = {Register.class}, message = "El campo no puede estar vacío")
    private String password;

    @NotBlank(groups = {Register.class, Modify.class}, message = "El campo no puede estar vacío")
    private String role;

    private int number_attempts;

    @PositiveOrZero(groups = {Register.class},message = "Tiene que ser un número positivo o 0")
    private int policy_acceptance;


    public interface Register {
    }

    public interface Modify {
    }


}
