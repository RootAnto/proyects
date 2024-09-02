package org.educa.airline.repository;

import org.educa.airline.entity.Luggage;

import java.util.List;

public interface LuggageRepository {
    List<Luggage> listLuggage(String flightId, String nif);

    boolean existLuggage(String flightId, String nif, int luggageId);

    boolean addLuggage(String flightId, String nif, Luggage luggage);

    Luggage getLuggage(String flightId, String nif, int luggageId);

    boolean updateLuggage(String flightNumber, String nif, Luggage luggage);
}
