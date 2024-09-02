package org.educa.airline.controllers;

import jakarta.validation.Valid;
import org.educa.airline.dto.ProductFlightDTO;
import org.educa.airline.entity.FlightString;
import org.educa.airline.entity.Passenger;
import org.educa.airline.mappers.ProductFlightMapper;
import org.educa.airline.mappers.ProductPassengerMapper;
import org.educa.airline.services.PassengerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.educa.airline.entity.Flight;
import org.educa.airline.services.FlightService;
import java.text.ParseException;
import org.educa.airline.dto.ProductPassengerDTO;
import java.util.List;



@RestController
public class FlightController {
    private final FlightService flightService;
    private PassengerService passengerService;
    private ProductFlightMapper productFlightMapper;
    private ProductPassengerMapper productPassengerMapper;

    private int count = 1;

    // MetodoS para inyeccion de FlightService y PassengerService
    @Autowired
    public FlightController(FlightService flightService, ProductFlightMapper productFlightMapper, ProductPassengerMapper productPassengerMapper) {
        this.flightService = flightService;
        this.productFlightMapper = productFlightMapper;
        this.productPassengerMapper = productPassengerMapper;
    }
    @Autowired
    public void setPassengerService(PassengerService passengerService) {
        this.passengerService = passengerService;
    }


    /********************************************  VUELOS *****************************************************/
    // Metodo para introducir vuelos
    @PostMapping("/flight")
    public ResponseEntity<Object> createFlight(@Valid @RequestBody FlightString flightString)  {
        System.out.println("Creando");
        try {

            // Utilizar el Mapper para convertir el DTO de cadena a un objeto Flight
            Flight flight = productFlightMapper.toEntity(flightString);

            boolean created = flightService.addFlight(flight);
            if (created) {
                System.out.println("Data reciber flight "+flight);
                return ResponseEntity.status(HttpStatus.CREATED).body("The flight was created successfully.");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error creating the flight.");
            }

        } catch (ParseException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid date format.");
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

        // Buscar el vuelo por ID y fecha utilizando el servicio de vuelo
        Flight flight = flightService.getFlight(flightId, date);

        // Si se encuentra el vuelo, convertirlo a DTO usando el mapper
        if (flight != null) {
            ProductFlightDTO flightDTO = productFlightMapper.toDTO(flight);
            return ResponseEntity.ok(flightDTO);
        } else {
            // Si no se encuentra el vuelo, devolver una respuesta HTTP 404 Not Found
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Flight with the specified ID not found.");
        }

    }




    /*************************************    PASAGEROS    ****************************************************/
    //Crear pasajeros.
    @PostMapping("/flight/{flightId}/passenger")
    public ResponseEntity<Object> addPassengerToFlight(
            @PathVariable("flightId") String flightId,
            @Valid @RequestBody Passenger passenger){

        // Verificar si el vuelo existe
        if (flightService.getFlight(flightId, null) == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The flight with the specified ID does not exist.");
        }

        ProductPassengerDTO passengerDTO = productPassengerMapper.toDTO(passenger);

        // Agregar el pasajero al vuelo
        boolean add = passengerService.addPassenger(passenger);

        // Verificar si el pasajero fue agregado exitosamente
        if (add) {
            return ResponseEntity.status(HttpStatus.CREATED).body("The passenger was successfully added to the flight.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The passenger already exists on the flight.");
        }
    }


    @GetMapping("/flight/{flightId}/passenger/{nif}")
    public ResponseEntity<Object> getPassengerInFlight(
            @PathVariable("flightId") String flightId,
            @PathVariable("nif") String nif) {

        // Check if the flight exists
        Flight flight = flightService.getFlight(flightId, null);
        if (flight == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The flight with the specified ID does not exist.");
        }

        // Check if the passenger is on the flight
        Passenger passenger = passengerService.getPassenger(flightId, nif);
        if (passenger != null) {
            // Convert the passenger to a DTO using the mapper
            ProductPassengerDTO passengerDTO = productPassengerMapper.toDTO(passenger);
            return ResponseEntity.ok(passengerDTO);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The passenger with the specified NIF is not on the flight.");
        }
    }



    @PutMapping("/flight/{flightId}/passenger/{nif}")
    public ResponseEntity<Object> updatePassengerInFlight(
            @PathVariable("flightId") String flightId,
            @PathVariable("nif") String nif,
            @Valid @RequestBody ProductPassengerDTO passengerDTO) {

        try {
            // Check if the flight exists
            if (flightService.getFlight(flightId, null) == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The flight with the specified ID does not exist.");
            }

            // Convert the DTO to a passenger entity using the mapper
            Passenger passenger = productPassengerMapper.toEntity(passengerDTO);

            // Update the passenger in the flight
            passengerService.updatePassenger(nif, passenger);
            return ResponseEntity.ok("The passenger was successfully updated in the flight.");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating the passenger in the flight.");
        }
    }



    @DeleteMapping("/flight/{flightId}/passenger/{nif}")
    public ResponseEntity<Object> removePassengerFromFlight(
            @PathVariable("flightId") String flightId,
            @PathVariable("nif") String nif) {

        try {
            // Verificar si el vuelo existe
            Flight flight = flightService.getFlight(flightId, null);
            if (flight == null) {
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

    //Obtenner todos los vuelos
    @GetMapping("/flight/{flightId}/passenger")
    public ResponseEntity<Object> getPassengersInFlight(
            @PathVariable("flightId") String flightId) {

        try {
            // Verificar si el vuelo existe
            Flight flight = flightService.getFlight(flightId, null);
            if (flight == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The flight with the specified ID does not exist.");
            }

            // Obtener la lista de pasajeros en el vuelo
            List<Passenger> passengers = passengerService.listPassengers(flightId);

            // Convertir la lista de pasajeros a DTOs usando el mapper
            List<ProductPassengerDTO> passengerDTOs = productPassengerMapper.toDTOs(passengers);

            return ResponseEntity.ok(passengerDTOs);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The flight with the specified ID does not exist.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving passengers for the flight.");
        }

    }
}
