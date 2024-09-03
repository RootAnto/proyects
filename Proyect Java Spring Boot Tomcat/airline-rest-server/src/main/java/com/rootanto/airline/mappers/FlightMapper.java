
package com.rootanto.airline.mappers;

import com.rootanto.airline.dto.FlightDTO;

import com.rootanto.airline.entity.Flight;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class FlightMapper {

    public Flight toEntity(FlightDTO flightDTO){
        Flight flight = new Flight();
        flight.setId(flightDTO.getId()); // Asignar el ID generado automaticamente del DTO a la entidad
        flight.setOrigin(flightDTO.getOrigin());
        flight.setDestination(flightDTO.getDestination());
        flight.setDate(flightDTO.getDate());
        return flight;
    }

    public FlightDTO toDTO(Flight flight){
        FlightDTO flightDTO = new FlightDTO();
        flightDTO.setId(flight.getId()); // Asignar el ID de la entidad al DTO
        flightDTO.setOrigin(flight.getOrigin());
        flightDTO.setDestination(flight.getDestination());
        flightDTO.setDate(flight.getDate());

        return flightDTO;
    }

    public List<FlightDTO> toDTOs(List<Flight> productEntities){
        List<FlightDTO> fligthDTOs= new ArrayList<>();
        for (Flight product: productEntities) {
            FlightDTO fligthDTO = toDTO(product);
            fligthDTOs.add(fligthDTO);
        }
        return fligthDTOs;
    }

}