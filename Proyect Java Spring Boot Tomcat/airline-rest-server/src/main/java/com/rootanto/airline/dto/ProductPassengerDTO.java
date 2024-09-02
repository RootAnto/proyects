package com.rootanto.airline.dto;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/*
@NotNull @NotEmpty Estas anotaciones de validacion (@NotNull y @NotEmpty) se utilizan para asegurar que el campo no
 sea nulo ni estar vacio.
* */
@Data
public class ProductPassengerDTO {
    @NotEmpty
    @NotNull
    private String nif;
    @NotEmpty
    @NotNull
    private String flightId;
    @NotEmpty
    @NotNull
    private String name;
    @NotEmpty
    @NotNull
    private String surname;
    @NotEmpty
    @NotNull
    private String email;
    @NotEmpty
    @NotNull
    private int seatNumber;
}
