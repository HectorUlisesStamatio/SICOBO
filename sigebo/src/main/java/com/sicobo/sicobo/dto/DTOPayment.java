package com.sicobo.sicobo.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.util.Date;

@Data
public class DTOPayment {

    @PositiveOrZero(message = "El campo tiene que ser mínimo de 0")
    private Double amount;

    @PositiveOrZero(message = "El campo tiene que ser mínimo de 0")
    private int amountMonths;

    @NotNull(message = "El campo no debe ser nulo")
    private Date paymentDate;

    @NotNull(message = "El campo no debe ser nulo")
    private Date dueDate;

    @PositiveOrZero(message = "Tiene que ser un número positivo o 0")
    private int status;

    @PositiveOrZero(message = "Tiene que ser un número positivo o 0")
    private int beanWarehouse;

    @PositiveOrZero(message = "Tiene que ser un número positivo o 0")
    private long beanUser;

    @NotNull(message = "El campo no debe ser nulo")
    private String paymentId;
}
