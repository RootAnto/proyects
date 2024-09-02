package org.educa.airline.repository;

import org.educa.airline.entity.Passenger;

import java.util.List;

public interface PassengerRepository {

    List<Passenger> listPassengers();

    List<Passenger> listPassengers(String flightId);

    Passenger getPassenger(String flightId, String nif);

    boolean existPassenger(String flightId, String nif);

    boolean deletePassenger(String flightId, String nif);

    boolean addPassenger(Passenger passenger);

    void updatePassenger(String nif, Passenger passenger);
}
