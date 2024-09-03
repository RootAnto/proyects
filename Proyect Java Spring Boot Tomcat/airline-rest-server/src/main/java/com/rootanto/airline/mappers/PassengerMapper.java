package com.rootanto.airline.mappers;


import com.rootanto.airline.dto.PassengerDTO;
import com.rootanto.airline.entity.Passenger;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PassengerMapper {
    public Passenger toEntity(PassengerDTO passengerDTO){
        Passenger passenger = new Passenger();
        passenger.setNif(passengerDTO.getNif());
        passenger.setFlightId(passengerDTO.getFlightId());
        passenger.setName(passengerDTO.getName());
        passenger.setSurname(passengerDTO.getSurname());
        passenger.setEmail(passengerDTO.getEmail());
        passenger.setSeatNumber(passengerDTO.getSeatNumber());
        return passenger;
    }

    public PassengerDTO toDTO(Passenger passenger){
        PassengerDTO passengerDTO = new PassengerDTO();
        passengerDTO.setNif(passenger.getNif());
        passengerDTO.setFlightId(passenger.getFlightId());
        passengerDTO.setName(passenger.getName());
        passengerDTO.setSurname(passenger.getSurname());
        passengerDTO.setEmail(passenger.getEmail());
        passengerDTO.setSeatNumber(passenger.getSeatNumber());

        return passengerDTO;
    }

    public List<PassengerDTO> toDTOs(List<Passenger> productEntities){
        List<PassengerDTO> passengerDTOs= new ArrayList<>();
        for (Passenger product: productEntities) {
            PassengerDTO passengerDTO = toDTO(product);
            passengerDTOs.add(passengerDTO);
        }
        return passengerDTOs;
    }

}
