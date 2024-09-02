package com.rootanto.airline.repository;

import com.rootanto.airline.entity.Flight;

import java.util.List;

public interface FlightRepository {
    List<Flight> list(String origin, String destination);

    Flight getFlight(String flightId);

    boolean add(Flight flight);

    boolean updateFlight(String flightId, Flight flight);

    boolean delete(String flightId);
}
