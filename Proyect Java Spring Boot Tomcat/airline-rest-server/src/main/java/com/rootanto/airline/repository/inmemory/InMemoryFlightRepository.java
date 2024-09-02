package com.rootanto.airline.repository.inmemory;

import com.rootanto.airline.entity.Flight;
import com.rootanto.airline.repository.FlightRepository;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class InMemoryFlightRepository implements FlightRepository {
    private final Map<String, Flight> flights = new HashMap<>();

    @Override
    public List<Flight> list(String origin, String destination) {
        return
                flights
                        .values()
                        .stream()
                        .filter(f -> origin == null || origin.equals(f.getOrigin()))
                        .filter(f -> destination == null || destination.equals(f.getDestination()))
                        .collect(Collectors.toList());
    }

    @Override
    public Flight getFlight(String flightId) {
        return flights.get(flightId);
    }

    @Override
    public boolean add(Flight flight) {
        if (!flights.containsKey(flight.getId())) {
            flights.put(flight.getId(), flight);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean updateFlight(
            String flightId,
            Flight flight
    ) {
        // 3 -> Madrid - Londres
        // 4 -> 1 Madrid - Berlin
        if (flights.containsKey(flightId)) {
            flights.remove(flightId);
            flights.put(flight.getId(), flight);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean delete(String flightId) {
        if (flights.containsKey(flightId)) {
            flights.remove(flightId);
            return true;
        } else {
            return false;
        }
    }
}
