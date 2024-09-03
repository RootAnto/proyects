package com.rootanto.airline.api;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rootanto.airline.dto.ProductFlightDTO;

import java.lang.reflect.Type;
import java.util.List;

public class ApiProductFlight extends ApiService {
    private final String URL = super.URL;

    public ProductFlightDTO findFlightById(String flightId) throws Exception {
        String url = URL + "/flight/{flightId}";
        url = url.replace("{flightId}", flightId);
        System.out.println(flightId);
        String body = connection.doGet(url);
        Gson gson = new Gson();
        return gson.fromJson(body, ProductFlightDTO.class);
    }

    public List<ProductFlightDTO> showAllFlight(String origin, String destination) throws Exception {
        String endpoint = URL + "/flight?origin=" + origin + "&destination=" + destination;
        String response = connection.doGet(endpoint);
        Type listType = new TypeToken<List<ProductFlightDTO>>(){}.getType();
        return new Gson().fromJson(response, listType);
    }

    public void create(ProductFlightDTO flightDTO){
        try {
            Gson gson = new Gson();
            String body = gson.toJson(flightDTO);
            connection.doPost(body, URL + "/flight");
        }catch (Exception e){
            e.printStackTrace();
        }

    }
/*
    public void update(ProductFlightDTO flightDTO) throws Exception {
        Gson gson = new Gson();
        String body = gson.toJson(flightDTO);
        connection.doUpdate(body, URL + "/flights" + flightDTO.getId());
    }

    public void delete(String flightId) throws Exception {
        connection.doDelete(URL + "/" + flightId);
    }
 */
}
