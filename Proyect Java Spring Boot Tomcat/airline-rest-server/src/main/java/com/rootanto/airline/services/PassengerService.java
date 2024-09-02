package com.rootanto.airline.services;

import com.rootanto.airline.entity.Passenger;
import com.rootanto.airline.repository.PassengerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PassengerService {

    private final PassengerRepository passengerRepository;

    @Autowired
    public PassengerService(PassengerRepository passengerRepository) {
        this.passengerRepository = passengerRepository;
    }

    public List<Passenger> listPassengers() {
        return passengerRepository.listPassengers();
    }

    public List<Passenger> listPassengers(String flightId) {
        return passengerRepository.listPassengers(flightId);
    }

    public Passenger getPassenger(String flightId, String nif) {
        return passengerRepository.getPassenger(flightId, nif);
    }

    public boolean existPassenger(String flightId, String nif) {
        return passengerRepository.existPassenger(flightId, nif);
    }

    public boolean deletePassenger(String flightId, String nif) {
        return passengerRepository.deletePassenger(flightId, nif);
    }

    public boolean addPassenger(Passenger passenger) {
        System.out.println("Passenger: "+ passenger);
        return passengerRepository.addPassenger(passenger);
    }

    public void updatePassenger(String nif, Passenger passenger) {
        passengerRepository.updatePassenger(nif, passenger);
    }
}
