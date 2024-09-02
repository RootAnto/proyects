package com.rootanto.airline.dto;


import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import jakarta.validation.constraints.NotNull;
import java.util.concurrent.atomic.AtomicInteger;

@Data
public class ProductLuggageDTO {


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
