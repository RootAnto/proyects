package com.rootanto.airline.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlightDTO {
    @NonNull
    private String id;
    @NonNull
    private String origin;
    @NonNull
    private String destination;
    @NonNull
    private String dateString;
}
