package com.rootanto.airline.Service;
import com.rootanto.airline.dto.ProductFlightDTO;
import com.rootanto.airline.api.ApiProductFlight;
import com.rootanto.airline.execption.NotFoundException;
import java.util.List;

public class FlightService {
    private final ApiProductFlight apiProductFlight = new ApiProductFlight();

    public void findAllFlight(String origin, String destination)  {
        try {
            System.out.println("Searching for flights with origin: " + origin + " and destination: " + destination);
            List<ProductFlightDTO> productFlightDTOList = apiProductFlight.showAllFlight(origin, destination);
            System.out.println("Flights found:");
            for (ProductFlightDTO flight : productFlightDTOList) {
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
            ProductFlightDTO flight = apiProductFlight.findFlightById(id);

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