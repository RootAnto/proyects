package com.rootanto.airline.dto;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import jakarta.validation.constraints.NotNull;
import java.util.Date;


@Data
public class ProductFlightDTO {

    @NotNull
    @NotEmpty
    private String id;

    @NotNull
    @NotEmpty
    private String origin;

    @NotNull
    @NotEmpty
    private String destination;

    @NotNull
    @NotEmpty
    private Date date;

}

