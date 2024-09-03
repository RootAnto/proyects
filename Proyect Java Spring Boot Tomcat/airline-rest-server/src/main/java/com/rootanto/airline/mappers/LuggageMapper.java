package com.rootanto.airline.mappers;

import com.rootanto.airline.dto.LuggageDTO;
import com.rootanto.airline.entity.Luggage;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class LuggageMapper {
    public Luggage toEntity(LuggageDTO productLuggageDTO){
        Luggage luggage = new Luggage();
        luggage.setId(productLuggageDTO.getId()); // Asignar el ID generado automaticamente del DTO a la entidad
        luggage.setNif(productLuggageDTO.getNif());
        luggage.setFlightId(productLuggageDTO.getFlightId());
        luggage.setDescription(productLuggageDTO.getDescription());
        return luggage;
    }

    public LuggageDTO toDTO(Luggage luggage){
        LuggageDTO luggageDTO = new LuggageDTO();
        luggageDTO.setId(luggage.getId()); // Asignar el ID de la entidad al DTO
        luggageDTO.setNif(luggage.getNif());
        luggageDTO.setFlightId(luggage.getFlightId());
        luggageDTO.setDescription(luggage.getDescription());

        return luggageDTO;
    }

    public List<LuggageDTO> toDTOs(List<Luggage> productEntities){
        List<LuggageDTO> luggageDTOs= new ArrayList<>();
        for (Luggage product: productEntities) {
            LuggageDTO lugaggeDTO = toDTO(product);
            luggageDTOs.add(lugaggeDTO);
        }
        return luggageDTOs;
    }
}
