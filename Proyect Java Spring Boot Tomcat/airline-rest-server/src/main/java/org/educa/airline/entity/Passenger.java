package org.educa.airline.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Passenger {
    private String nif;
    private String flightId;
    private String name;
    private String surname;
    private String email;
    private int seatNumber;
}
