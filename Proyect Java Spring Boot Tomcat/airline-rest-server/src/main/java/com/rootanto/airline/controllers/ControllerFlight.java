package com.rootanto.airline.controllers;

import com.rootanto.airline.dto.FlightDTO;
import com.rootanto.airline.services.FlightService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.rootanto.airline.entity.Flight;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
public class ControllerFlight {
    private final FlightService flightService;
    private int count = 1;

    // MetodoS para inyeccion de FlightService y PassengerService
    @Autowired
    public ControllerFlight(FlightService flightService) {
        this.flightService = flightService;
    }

    // Metodo para introducir vuelos
    @PostMapping("/flight")
    public ResponseEntity<Object> createFlight(@Valid @RequestBody FlightDTO flight) {

        System.out.println("Entering flight information.");
        System.out.println("origen: " + flight.getOrigin());
        System.out.println("destino: " + flight.getDestination());
        System.out.println("fecha: " + flight.getDate());

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

        // Crear nuevo vuelo con los datos de RequestBody
        boolean created = flightService.addFlight(new Flight(
                        flight.getId(),
                        flight.getOrigin(),
                        flight.getDestination(),
                        flight.getDate())
                );
        count++;
        if (created) {
            return ResponseEntity.status(HttpStatus.CREATED).body("The flight was created successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error creating the flight.");
        }
    }

    //Consulta de vuelos por origen y destino.
    @GetMapping("/flight")
    public ResponseEntity<Object> getFlights(
            @Valid
            @RequestParam("origin") String origin,
            @RequestParam("destination") String destination) {

        System.out.println("Searching for flights by origin and destination.");

        // Buscar vuelos con el origen y destino dados
        List<Flight> flights = flightService.listFlights(origin, destination);

        // Si se encuentran vuelos, devolver una respuesta con los vuelos encontrados
        if (!flights.isEmpty()) {
            return ResponseEntity.ok(flights);
        } else {
            // Si no se encuentran vuelos, devolver una respuesta HTTP 404 Not Found
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No flights found for the specified criteria.");
        }
    }

    //Consulta de un vuelo por id
    @GetMapping("/flight/{flightId}")
    public ResponseEntity<Object> getFlight(
            @PathVariable("flightId") String flightId,
            @RequestParam(value = "date", required = false) String date) {


        System.out.println("Search flight by ID.");

        // Busqueda del vuelo por ID y fecha
        Flight flight = flightService.getFlight(flightId, date);

        // Si se encuentra el vuelo, devolver una respuesta con el vuelo encontrado
        if (flight != null) {
            return ResponseEntity.ok(flight);
        } else {
            // Si no se encuentra el vuelo, devolver una respuesta HTTP 404 Not Found
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Flight with the specified ID not found.");
        }
    }
}
