package com.rootanto.airline.Service;

import com.rootanto.airline.api.ApiProductPassenger;
import com.rootanto.airline.dto.ProductPassengerDTO;

import java.util.List;

public class PasangerService {

    private final ApiProductPassenger apiProductPassenger = new ApiProductPassenger();
    public ProductPassengerDTO getPassenger(String nif, String id) {
        try {
            System.out.println("Searching for passenger by NIF: " + nif + " with flight id " + id);
            ProductPassengerDTO passenger = apiProductPassenger.getPassengerInFlight(nif, id);

            if (passenger != null) {
                System.out.println("NIF: " + passenger.getNif());
                System.out.println("Vuelo: " + passenger.getFlightId());
                System.out.println("Name: " + passenger.getName());
                System.out.println("Surname: " + passenger.getSurname());
                System.out.println("E-mail: " + passenger.getEmail());
                System.out.println("Seat number: " + passenger.getSeatNumber());
            } else {
                System.out.println("Passenger with NIF " + nif + " and flight ID " + id + " not found.");
            }

            return passenger;
        } catch (Exception e) {
            System.out.println("An error occurred while retrieving passenger: " + e.getMessage());
            return null;
        }
    }

    public void getAllPassenger(String idVuelo)  {
        try {
            System.out.println("Watch all passengers");
            List<ProductPassengerDTO> productPassengerList = apiProductPassenger.getAllPassengers(idVuelo);
            System.out.println("Passengers found:");
            for (ProductPassengerDTO passenger : productPassengerList) {
                System.out.println("Passenger:");
                System.out.println("NIF: " + passenger.getNif());
                System.out.println("Flight ID: " + passenger.getFlightId());
                System.out.println("Name: " + passenger.getName());
                System.out.println("Surname: " + passenger.getSurname());
                System.out.println("Email: " + passenger.getEmail());
                System.out.println("Seat: " + passenger.getSeatNumber());
                System.out.println("------------------------------------------");
            }
        } catch (Exception e) {
            System.err.println("An error occurred while retrieving passengers: " + e.getMessage());
            e.printStackTrace();
        }

    }
}
