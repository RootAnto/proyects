package com.rootanto.airline.controllers;

import jakarta.validation.Valid;
import com.rootanto.airline.entity.Flight;
import com.rootanto.airline.entity.Passenger;
import com.rootanto.airline.services.FlightService;
import com.rootanto.airline.services.PassengerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
public class ControllerPassenger {

    private final FlightService flightService;
    private PassengerService passengerService;

    // MetodoS para inyeccion de FlightService y PassengerService
    @Autowired
    public ControllerPassenger(FlightService flightService) {
        this.flightService = flightService;
    }

    @Autowired
    public void setPassengerService(PassengerService passengerService) {
        this.passengerService = passengerService;
    }

    /*************************************    PASAGEROS    ****************************************************/

    //Crear pasajeros.
    @PostMapping("/flight/{flightId}/passenger")
    public ResponseEntity<Object> addPassengerToFlight(
            @PathVariable("flightId") String flightId,
            @Valid @RequestBody Passenger passenger) { // El pasagero se valida automaticamente.

        System.out.println("Entering flight information.");

        // Verificar si el vuelo existe
        if (flightService.getFlight(flightId, null) == null) {

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The flight with the specified ID does not exist.");
        }

        // Agregar el pasajero al vuelo
        boolean add = passengerService.addPassenger(passenger);

        // Verificar si el pasajero fue agregado exitosamente
        if (add) {
            return ResponseEntity.status(HttpStatus.CREATED).body("The passenger was successfully added to the flight.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The passenger already exists on the flight.");
        }
    }

    //Obtener pasagero por NIF
    @GetMapping("/flight/{flightId}/passenger/{nif}")
    public ResponseEntity<Object> getPassengerInFlight(
            @PathVariable("flightId") String flightId,
            @PathVariable("nif") String nif) {

        System.out.println("Searching for a passenger on a specific flight.");

        // Verificar si el vuelo existe
        Flight flight = flightService.getFlight(flightId, null);
        if (flight == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El vuelo con el ID especificado no existe.");
        }

        // Verificar si el pasajero esta en el vuelo
        Passenger passenger = passengerService.getPassenger(flightId, nif);
        System.out.println(passenger);
        if (passenger != null) {
            return ResponseEntity.ok(passenger);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El pasajero con el NIF especificado no esta en el vuelo.");
        }
    }


    // Actualizar pasajero en el vuelo.
    @PutMapping("/flight/{flightId}/passenger/{nif}")
    public ResponseEntity<Object> updatePassengerInFlight(
            @PathVariable("flightId") String flightId,
            @PathVariable("nif") String nif,
            @Valid @RequestBody Passenger passenger) {

        try {
            // Verificar si el vuelo existe
            if (flightService.getFlight(flightId, null) == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The flight with the specified ID does not exist.");
            }

            // Actualizar el pasajero en el vuelo
            passengerService.updatePassenger(nif, passenger);
            return ResponseEntity.ok("The passenger was successfully updated in the flight.");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating the passenger in the flight.");
        }
    }


    @DeleteMapping("/flight/{flightId}/passenger/{nif}")
    public ResponseEntity<Object> deletePassengerFromFlight(
            @PathVariable("flightId") String flightId,
            @PathVariable("nif") String nif) {
        try {
            System.out.println("Search passanger.");
            // Verificar si el vuelo existe
            if (flightService.getFlight(flightId, null) == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The flight with the ID specified does not exist.");
            }

            // Verificar si el pasajero está en el vuelo
            Passenger passenger = passengerService.getPassenger(flightId, nif);
            if (passenger != null) {
                // Eliminar el pasajero del vuelo
                boolean deleted = passengerService.deletePassenger(flightId, nif);
                if (deleted) {
                    return ResponseEntity.ok("The passenger was successfully removed from the flight.");
                } else {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error removing the passenger from the flight.");
                }
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The passenger with the specified NIF is not on the flight.");
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The flight with the specified ID does not exist.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error removing the passenger from the flight.");
        }
    }


    // Obtener todos los pasajeros.
    @GetMapping("/flight/{flightId}/passenger")
    public ResponseEntity<Object> getPassengersInFlight(@PathVariable("flightId") String flightId) {
        try {
            // Verificar si el vuelo existe
            Flight flight = flightService.getFlight(flightId, null);
            if (flight == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The flight with the specified ID does not exist.");
            }

            // Obtener la lista de pasajeros en el vuelo
            List<Passenger> passengers = passengerService.listPassengers(flightId);
            return ResponseEntity.ok(passengers);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The flight with the specified ID does not exist.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving passengers for the flight.");
        }
    }
}
