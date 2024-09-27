package com.rootanto.airline.core;
import com.rootanto.airline.api.ApiPassenger;
import com.rootanto.airline.dto.FlightDTO;
import com.rootanto.airline.dto.PassengerDTO;
import com.rootanto.airline.Service.FlightService;
import com.rootanto.airline.Service.PasangerService;
import com.rootanto.airline.api.ApiFlight;
import com.rootanto.airline.helper.Metod;
import java.util.Scanner;

import static com.rootanto.airline.helper.Metod.confirmation;
import static com.rootanto.airline.helper.Metod.stringValidation;


public class Client {
    private final FlightService flightService = new FlightService();
    private final PasangerService pasangerService = new PasangerService();
    private final ApiFlight apiFlight = new ApiFlight();
    private final ApiPassenger apiPassenger = new ApiPassenger();

    public void run() {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Welcome to Airline Rest");
            boolean exit = false;
            do {
                System.out.println("1.Flight\n2.Passengers\n0.Exit");
                String opcion = scanner.nextLine();
                switch (opcion) {
                    case "1":
                        menuFlight(scanner);
                        break;
                    case "2":
                        menuPassengers(scanner);
                        break;
                    case "0":
                        exit = true;
                        System.out.println("Exit to Airline Rest");
                        break;
                    default:
                        System.out.println("Invalid option");
                }
            }while (!exit);
        }
    }

    private void menuFlight(Scanner scanner) {
        System.out.println("Menu flights");
        try {
            boolean exit = false;
            do {
                String opcion = stringValidation("1.Create flyght\n2.Look for a flight\n3.Show all flights\n" +
                        "0.Exit", scanner, "\\d+");
                switch (opcion) {
                    case "1":
                        createFlight(scanner);
                        break;
                    case "2":
                        showFlightId(scanner);
                        break;
                    case "3":
                        showAllFlights(scanner);
                        break;
                    case "0":
                        exit = true;
                        System.out.println("Exit menu flight");
                        break;
                    default:
                        System.out.println("Invalid opcion");
                }
            } while (!exit);
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
    }





    private void menuPassengers(Scanner scanner) {
        System.out.println("Menu passengers");
        boolean exit = false;
        try{
            do {
                System.out.println();
                String opcion = stringValidation("1.Assign passenger flight\n2.Passenger in flyght\n" +
                        "3.Update passenger\n4.Delete Passenger\n5.Show all passengers\n0.Exit", scanner, "\\d+");

                switch (opcion) {
                    case "1":
                        createPassangerToFly(scanner);
                        break;
                    case "2":
                        searchPassanger(scanner);
                        break;
                    case "3":
                        updatePassenger(scanner);
                        break;
                    case "4":
                        deletePassenger(scanner);
                        break;
                    case "5":
                        showAllPassengers(scanner);
                        break;
                    case "0":
                        exit = true;
                        System.out.println("Exit menu passenger");
                        break;
                    default:
                        System.out.println("Invalid option");
                }
            }while (!exit);
        }catch (RuntimeException e){
            System.out.println(e.getMessage());
        }
    }



    /*--------------------------------------------- VUELOS ----------------------------------------------*/
    private void createFlight(Scanner scanner)  {
        System.out.println("Registration flights");

        System.out.println("Enter the origin of the flight(France):");
        String origin = scanner.nextLine();

        String destination = stringValidation("Enter the destination of the flight(Rome):",scanner,"([A-Z][a-z]*)+");
        String date = stringValidation("Enter date(DD/MM/YYYY):",scanner,"^\\d{2}/\\d{2}/\\d{4}$");
        date.formatted(date);
        apiFlight.create(new FlightDTO("", origin, destination, date));
    }


    private void showFlightId(Scanner scanner) {
        System.out.println("Search for a flight");
        String id = stringValidation("Enter the flight id", scanner, "\\d+");
        flightService.findFlightById(id);
    }


    private void showAllFlights(Scanner scanner) {
        System.out.println("Show all flights with a specific origin and destination.");
        String origin = stringValidation("Enter the origin of the flight(France):", scanner, "([A-Z][a-z]*)+");
        String destination = stringValidation("Enter the destination of the flight(Rome):",scanner,"([A-Z][a-z]*)+");
        flightService.findAllFlight(origin, destination);
    }

    /*--------------------------------------------- PASAGEROS ----------------------------------------------*/
    private void createPassangerToFly(Scanner scanner)  {

        System.out.println("Add passenger to flyght");
        String nif = Metod.stringValidation("Enter NIF (00000000R):", scanner, "\\d{8}[A-HJ-NP-TV-Z]");
        String id = stringValidation("Enter the flight id", scanner, "\\d+");
        String name = stringValidation("Enter name(Antonio):", scanner, "([A-Z][a-z]*)+");
        String surname = stringValidation("Enter surname(Llorente):", scanner, "([A-Z][a-z]*)+");
        String eMail = stringValidation("Enter e-mail passenger",scanner,"[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}");
        String seat = stringValidation("Enter seat number plane",scanner,"\\d+");


        int seatNumber = Integer.parseInt(seat);
        apiPassenger.addPassengerToFlight(new PassengerDTO(nif,id,name,surname,eMail,seatNumber));

    }

    private PassengerDTO searchPassanger(Scanner scanner) {
        System.out.println("Search passenger");

        PassengerDTO passengerDTO;

        String id = stringValidation("Enter id flyght", scanner,"\\d+");
        String nif = Metod.stringValidation("Enter NIF passenger (00000000R):", scanner, "\\d{8}[A-HJ-NP-TV-Z]");

        passengerDTO = pasangerService.getPassenger(id, nif);

        return passengerDTO;
    }

    private void deletePassenger(Scanner scanner) {
        System.out.println("Delete pasanger");

        PassengerDTO passengerDTO = searchPassanger(scanner);
        if (passengerDTO!=null){
            if(confirmation("Sure delete passager?", scanner)){
                apiPassenger.deletePassengerFromFlight(passengerDTO.getFlightId(),passengerDTO.getNif());
            }
        }
    }

    private void updatePassenger(Scanner scanner) {
        System.out.println("Update data passenger");

        PassengerDTO passengerDTO = searchPassanger(scanner);

        if (passengerDTO != null) {
            boolean exit = false;
            do {
                String flightId = passengerDTO.getFlightId();
                String nif = passengerDTO.getNif();

                System.out.println("1.NIF\n2.Flight id\n3.Name\n4.Surname\n5.Email\n6.Seat number\n0.exit");
                String option = scanner.nextLine();
                switch (option) {
                    case "1":
                        String newNif = Metod.stringValidation("Enter NIF (00000000R):", scanner, "\\d{8}[A-HJ-NP-TV-Z]");
                        passengerDTO.setNif(newNif);
                        if (confirmation("Sure to modify the passanger?",scanner)){
                            updateFact(flightId, nif, passengerDTO);
                        }
                        break;
                    case "2":
                        String newFlightId = stringValidation("Enter the flight id", scanner, "\\d+");
                        passengerDTO.setFlightId(newFlightId);
                        if (confirmation("Sure to modify the passanger?",scanner)){
                            updateFact(flightId, nif, passengerDTO);
                        }
                        break;
                    case "3":
                        String newName = stringValidation("Enter name(Antonio):", scanner, "([A-Z][a-z]*)+");
                        passengerDTO.setName(newName);
                        if (confirmation("Sure to modify the passanger?",scanner)){
                            updateFact(flightId, nif, passengerDTO);
                        }

                        break;
                    case "4":
                        String newSurname = stringValidation("Enter surname(Llorente):", scanner, "([A-Z][a-z]*)+");
                        passengerDTO.setSurname(newSurname);
                        if (confirmation("Sure to modify the passenger?",scanner)){
                            updateFact(flightId, nif, passengerDTO);
                        }

                        break;
                    case "5":
                        String newEmail = stringValidation("Enter e-mail passenger",scanner,"[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}");
                        passengerDTO.setEmail(newEmail);
                        if (confirmation("Sure to modify the passenger?",scanner)){
                            updateFact(flightId, nif, passengerDTO);
                        }
                        break;
                    case "6":
                        String newSeatNumber = stringValidation("Enter seat number plane",scanner,"\\d+");
                        int nSN = Integer.parseInt(newSeatNumber);
                        passengerDTO.setSeatNumber(nSN);
                        if(confirmation("Sure to modify the passenger?",scanner)){
                            updateFact(flightId, nif, passengerDTO);
                        }
                        break;
                    case "0":
                        System.out.println("Exit update data");
                        exit = true;
                        break;
                    default:
                        System.out.println("Invalid option");
                }
            } while (!exit);
        } else {
            System.out.println("Passenger not found, cannot update.");
        }
    }


    private void updateFact(String flyghtId, String nif, PassengerDTO passengerDTO) {
        apiPassenger.updatePassengerInFlight(flyghtId,nif,passengerDTO);
    }


    private void showAllPassengers(Scanner scanner) {
        String id = stringValidation("Enter id flyght", scanner,"\\d+");
        pasangerService.getAllPassenger(id);
    }

}
