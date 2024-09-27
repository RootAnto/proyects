package com.rootanto.airline.Service;
import com.rootanto.airline.dto.FlightDTO;
import com.rootanto.airline.api.ApiFlight;
import com.rootanto.airline.execption.NotFoundException;
import java.util.List;

public class FlightService {
    private final ApiFlight apiFlight = new ApiFlight();

    public void findAllFlight(String origin, String destination)  {
        try {
            System.out.println("Searching for flights with origin: " + origin + " and destination: " + destination);
            List<FlightDTO> productFlightDTOList = apiFlight.showAllFlight(origin, destination);
            System.out.println("Flights found:");
            for (FlightDTO flight : productFlightDTOList) {
                System.out.println("ID: " + flight.getId());
                System.out.println("Origin: " + flight.getOrigin());
                System.out.println("Destination: " + flight.getDestination());
                System.out.println("Date: " + flight.getDateString());
            }
        } catch (NotFoundException e) {
            System.out.println("No flights found with the requested data.");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void findFlightById(String id){
        try{
            System.out.println("Searching for flights with ID: " + id);
            FlightDTO flight = apiFlight.findFlightById(id);

            System.out.println("ID: " + flight.getId());
            System.out.println("Origin: " + flight.getOrigin());
            System.out.println("Destination: " + flight.getDestination());
            System.out.println("Date: " + flight.getDateString());

        } catch (RuntimeException e) {
            System.out.println("No flights registered with the specified ID");
            throw new RuntimeException(e);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}