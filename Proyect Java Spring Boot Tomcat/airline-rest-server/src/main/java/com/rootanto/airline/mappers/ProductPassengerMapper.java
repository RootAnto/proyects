package com.rootanto.airline.mappers;


import com.rootanto.airline.dto.ProductPassengerDTO;
import com.rootanto.airline.entity.Passenger;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProductPassengerMapper {
    public Passenger toEntity(ProductPassengerDTO productPassengerDTO){
        Passenger passenger = new Passenger();
        passenger.setNif(productPassengerDTO.getNif());
        passenger.setFlightId(productPassengerDTO.getFlightId());
        passenger.setName(productPassengerDTO.getName());
        passenger.setSurname(productPassengerDTO.getSurname());
        passenger.setEmail(productPassengerDTO.getEmail());
        passenger.setSeatNumber(productPassengerDTO.getSeatNumber());
        return passenger;
    }

    public ProductPassengerDTO toDTO(Passenger passenger){
        ProductPassengerDTO passengerDTO = new ProductPassengerDTO();
        passengerDTO.setNif(passenger.getNif());
        passengerDTO.setFlightId(passenger.getFlightId());
        passengerDTO.setName(passenger.getName());
        passengerDTO.setSurname(passenger.getSurname());
        passengerDTO.setEmail(passenger.getEmail());
        passengerDTO.setSeatNumber(passenger.getSeatNumber());

        return passengerDTO;
    }

    public List<ProductPassengerDTO> toDTOs(List<Passenger> productEntities){
        List<ProductPassengerDTO> passengerDTOs= new ArrayList<>();
        for (Passenger product: productEntities) {
            ProductPassengerDTO passengerDTO = toDTO(product);
            passengerDTOs.add(passengerDTO);
        }
        return passengerDTOs;
    }

}
