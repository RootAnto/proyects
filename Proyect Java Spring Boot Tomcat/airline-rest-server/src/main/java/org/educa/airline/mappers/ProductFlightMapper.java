
package org.educa.airline.mappers;

import org.educa.airline.dto.ProductFlightDTO;

import org.educa.airline.entity.Flight;
import org.educa.airline.entity.FlightString;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class ProductFlightMapper {

    private final SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

    public Flight toEntity(FlightString flightString) throws ParseException {
        Date date = formatter.parse(flightString.getDateString());

        return new Flight(
                flightString.getId(),
                flightString.getOrigin(),
                flightString.getDestination(),
                date
        );
    }

    public ProductFlightDTO toDTO(Flight flight){
        ProductFlightDTO flightDTO = new ProductFlightDTO();
        flightDTO.setId(flight.getId()); // Asignar el ID de la entidad al DTO
        flightDTO.setOrigin(flight.getOrigin());
        flightDTO.setDestination(flight.getDestination());
        flightDTO.setDate(flight.getDate());

        return flightDTO;
    }

    public List<ProductFlightDTO> toDTOs(List<Flight> productEntities){
        List<ProductFlightDTO> fligthDTOs= new ArrayList<>();
        for (Flight product: productEntities) {
            ProductFlightDTO fligthDTO = toDTO(product);
            fligthDTOs.add(fligthDTO);
        }
        return fligthDTOs;
    }

}