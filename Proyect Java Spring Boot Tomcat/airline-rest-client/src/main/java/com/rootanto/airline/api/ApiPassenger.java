package com.rootanto.airline.api;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rootanto.airline.dto.PassengerDTO;
import java.lang.reflect.Type;
import java.util.List;


public class ApiPassenger extends ApiService {
    private final String URL = super.URL;

    public void addPassengerToFlight(PassengerDTO passengerDTO){
        try {
            String body = new Gson().toJson(passengerDTO);
            String endpoint = URL + "/flight/" + passengerDTO.getFlightId() + "/passenger";
            connection.doPost(body, endpoint);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public PassengerDTO getPassengerInFlight(String flightId, String nif) throws Exception {
        String endpoint = URL + "/flight/{flightId}/passenger/{nif}";
        endpoint = endpoint.replace("{flightId}", flightId);
        endpoint = endpoint.replace("{nif}",nif);

        String response = connection.doGet(endpoint);

        return new Gson().fromJson(response, PassengerDTO.class);
    }

    public void updatePassengerInFlight(String flightId, String nif, PassengerDTO passengerDTO){
        try {
            String endpoint = URL + "/flight/{flightId}/passenger/{nif}";
            endpoint = endpoint.replace("{flightId}", flightId);
            endpoint = endpoint.replace("{nif}",nif);

            String body = new Gson().toJson(passengerDTO);

            connection.doUpdate(body, endpoint);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void deletePassengerFromFlight(String flightId, String nif){
        try {
            String endpoint = URL + "/flight/{flightId}/passenger/{nif}";
            endpoint = endpoint.replace("{flightId}", flightId);
            endpoint = endpoint.replace("{nif}", nif);
            connection.doDelete(endpoint);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public List<PassengerDTO> getAllPassengers(String idVuelo) throws Exception {
        String endpoint = URL + "/flight/{flightId}/passenger";
        endpoint = endpoint.replace("{flightId}", idVuelo);
        String response = connection.doGet(endpoint);
        Type listType = new TypeToken<List<PassengerDTO>>() {}.getType();
        return new Gson().fromJson(response, listType);
    }


}


