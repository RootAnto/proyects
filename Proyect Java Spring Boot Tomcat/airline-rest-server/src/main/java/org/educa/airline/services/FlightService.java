package org.educa.airline.services;

import org.educa.airline.entity.Flight;
import org.educa.airline.repository.FlightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FlightService  {

    private final FlightRepository flightRepository;

    @Autowired
    public FlightService(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    public List<Flight> listFlights(String origin, String destination) {
        return flightRepository.list(origin, destination);
    }

    public Flight getFlight(String flightId, String date) {
        return flightRepository.getFlight(flightId);
    }

    public boolean addFlight(Flight flight) {
        System.out.println("Flight: " + flight);
        return flightRepository.add(flight);
    }

    public boolean updateFlight(String flightId, Flight flight) {
        return flightRepository.updateFlight(flightId, flight);
    }

    public boolean deleteFlight(String flightId) {
        return flightRepository.delete(flightId);
    }

}