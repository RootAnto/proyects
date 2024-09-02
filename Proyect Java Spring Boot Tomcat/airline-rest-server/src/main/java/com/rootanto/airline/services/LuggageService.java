package com.rootanto.airline.services;

import com.rootanto.airline.entity.Luggage;
import com.rootanto.airline.repository.LuggageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LuggageService {

    private final LuggageRepository luggageRepository;

    @Autowired
    public LuggageService(LuggageRepository luggageRepository) {
        this.luggageRepository = luggageRepository;
    }

    public List<Luggage> listLuggage(String flightId, String nif) {
        return luggageRepository.listLuggage(flightId, nif);
    }

    public boolean existLuggage(String flightId, String nif, int luggageId) {
        return luggageRepository.existLuggage(flightId, nif, luggageId);
    }

    public boolean addLuggage(String flightId, String nif, Luggage luggage) {
        return luggageRepository.addLuggage(flightId, nif, luggage);
    }

    public Luggage getLuggage(String flightId, String nif, int luggageId) {
        return luggageRepository.getLuggage(flightId, nif, luggageId);
    }

    public boolean updateLuggage(String flightId, String nif, Luggage luggage) {
        return luggageRepository.updateLuggage(flightId, nif, luggage);
    }
}
