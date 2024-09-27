package com.rootanto.airline.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PassengerDTO {
    private String nif;
    private String flightId;
    private String name;
    private String surname;
    private String email;
    private int seatNumber;
}
