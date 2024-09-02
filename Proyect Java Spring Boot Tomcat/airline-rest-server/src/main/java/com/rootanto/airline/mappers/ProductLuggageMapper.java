package com.rootanto.airline.mappers;

import com.rootanto.airline.dto.ProductLuggageDTO;
import com.rootanto.airline.entity.Luggage;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProductLuggageMapper {
    public Luggage toEntity(ProductLuggageDTO productLuggageDTO){
        Luggage luggage = new Luggage();
        luggage.setId(productLuggageDTO.getId()); // Asignar el ID generado automaticamente del DTO a la entidad
        luggage.setNif(productLuggageDTO.getNif());
        luggage.setFlightId(productLuggageDTO.getFlightId());
        luggage.setDescription(productLuggageDTO.getDescription());
        return luggage;
    }

    public ProductLuggageDTO toDTO(Luggage luggage){
        ProductLuggageDTO luggageDTO = new ProductLuggageDTO();
        luggageDTO.setId(luggage.getId()); // Asignar el ID de la entidad al DTO
        luggageDTO.setNif(luggage.getNif());
        luggageDTO.setFlightId(luggage.getFlightId());
        luggageDTO.setDescription(luggage.getDescription());

        return luggageDTO;
    }

    public List<ProductLuggageDTO> toDTOs(List<Luggage> productEntities){
        List<ProductLuggageDTO> luggageDTOs= new ArrayList<>();
        for (Luggage product: productEntities) {
            ProductLuggageDTO lugaggeDTO = toDTO(product);
            luggageDTOs.add(lugaggeDTO);
        }
        return luggageDTOs;
    }
}
