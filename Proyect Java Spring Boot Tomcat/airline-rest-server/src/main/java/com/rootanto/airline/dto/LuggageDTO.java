package com.rootanto.airline.dto;


import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import jakarta.validation.constraints.NotNull;

@Data
public class LuggageDTO {


    @NotNull
    private int id ;

    @NotNull
    @NotEmpty
    private String nif;

    @NotNull
    @NotEmpty
    private String flightId;

    @NotNull
    @NotEmpty
    private String description;
}
