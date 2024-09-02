package org.educa.api;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.educa.dto.ProductPassengerDTO;
import java.lang.reflect.Type;
import java.util.List;


public class ApiProductPassenger extends ApiService {
    private final String URL = super.URL;

    public void addPassengerToFlight(ProductPassengerDTO productPassengerDTO){
        try {
            String body = new Gson().toJson(productPassengerDTO);
            String endpoint = URL + "/flight/" + productPassengerDTO.getFlightId() + "/passenger";
            connection.doPost(body, endpoint);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ProductPassengerDTO getPassengerInFlight(String flightId, String nif) throws Exception {
        String endpoint = URL + "/flight/{flightId}/passenger/{nif}";
        endpoint = endpoint.replace("{flightId}", flightId);
        endpoint = endpoint.replace("{nif}",nif);

        String response = connection.doGet(endpoint);

        return new Gson().fromJson(response, ProductPassengerDTO.class);
    }

    public void updatePassengerInFlight(String flightId, String nif, ProductPassengerDTO productPassengerDTO){
        try {
            String endpoint = URL + "/flight/{flightId}/passenger/{nif}";
            endpoint = endpoint.replace("{flightId}", flightId);
            endpoint = endpoint.replace("{nif}",nif);

            String body = new Gson().toJson(productPassengerDTO);

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

    public List<ProductPassengerDTO> getAllPassengers(String idVuelo) throws Exception {
        String endpoint = URL + "/flight/{flightId}/passenger";
        endpoint = endpoint.replace("{flightId}", idVuelo);
        String response = connection.doGet(endpoint);
        Type listType = new TypeToken<List<ProductPassengerDTO>>() {}.getType();
        return new Gson().fromJson(response, listType);
    }


}


