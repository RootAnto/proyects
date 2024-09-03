package com.rootanto.airline.controllers;
/*
import jakarta.validation.Valid;
import entity.com.rootanto.airline.Luggage;
import services.com.rootanto.airline.LuggageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/luggage")
public class LuggageController {

    private final LuggageService luggageService;

    @Autowired
    public LuggageController(LuggageService luggageService) {
        this.luggageService = luggageService;
    }

    @PostMapping("/add")
    public ResponseEntity<Object> addLuggage(@RequestBody String id_vuelo, String nif, @Valid @RequestBody Luggage luggage) {
        boolean added = luggageService.addLuggage(id_vuelo,nif,luggage);
        if (added) {
            return ResponseEntity.status(HttpStatus.CREATED).body("El equipaje fue agregado correctamente.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al agregar el equipaje.");
        }
    }

    @GetMapping("/{flightId}/{nif}/{luggageId}")
    public ResponseEntity<Object> getLuggage(@PathVariable String flightId, @PathVariable String nif, @PathVariable int luggageId) {
        Luggage luggage = luggageService.getLuggage(flightId, nif, luggageId);
        if (luggage != null) {
            return ResponseEntity.ok(luggage);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró equipaje para los criterios especificados.");
        }
    }

    @PutMapping("/update")
    public ResponseEntity<Object> updateLuggage(@RequestParam String flightNumber, @RequestParam String nif, @Valid @RequestBody Luggage luggage) {
        boolean exist = luggageService.existLuggage(flightNumber, nif, luggage.getId());
        if (exist) {
            boolean updated = luggageService.updateLuggage(flightNumber, nif, luggage);
            if (updated) {
                return ResponseEntity.ok("El equipaje fue actualizado correctamente.");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al actualizar el equipaje.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró el equipaje a actualizar.");
        }
    }
}
*/