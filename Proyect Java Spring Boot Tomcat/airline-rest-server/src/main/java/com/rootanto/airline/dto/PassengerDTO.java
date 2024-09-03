package com.rootanto.airline.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

/**
 * Data Transfer Object (DTO) for representing a passenger's information.
 * This class is used to encapsulate data related to a passenger for transfer between different layers of the application.
 * It includes validation constraints to ensure that the data adheres to expected formats and values.
 *
 * Lombok's @Data annotation generates boilerplate code such as getters, setters, toString(), equals(), and hashCode() methods.
 */
@Data
public class PassengerDTO {

    /**
     * Identifier for the flight associated with the passenger.
     * This field must be a non-empty string containing only numeric characters.
     *
     * @NotEmpty ensures that the flightId field cannot be empty. An empty string or null value is considered invalid.
     * @Pattern ensures that the flightId field only contains numeric characters (0-9). The regular expression "[0-9]"
     *         validates that the string consists of digits only. Note that this regular expression will not enforce a specific
     *         length for the flight ID. If a specific length is required, you should modify the regular expression accordingly.
     *
     * Example:
     * - Valid: "12345"
     * - Invalid: "AB123", "123.45", ""
     */
    @NotEmpty(message = "The field flight id cannot be empty")
    @Pattern(regexp = "[0-9]+", message = "The field flight id only allows numbers")
    private String flightId;


    /**
     * The NIF (Número de Identificación Fiscal) of the passenger.
     * This field must be a non-empty string that consists of 8 digits followed by a single letter.
     *
     * @NotEmpty ensures that the NIF field cannot be empty.
     * @Pattern ensures that the NIF follows the format: 8 digits and 1 letter.
     */
    @NotEmpty(message = "The field NIF cannot be empty")
    @Pattern(regexp = "^[0-9]{8}[A-Za-z]$", message = "The NIF must have 8 digits followed by a letter")
    private String nif;

    /**
     * The first name of the passenger.
     * This field must not be empty.
     *
     * @NotEmpty ensures that the name field cannot be empty.
     */
    @NotEmpty(message = "The field name cannot be empty")
    private String name;

    /**
     * The surname of the passenger.
     * This field must not be empty.
     *
     * @NotEmpty ensures that the surname field cannot be empty.
     */
    @NotEmpty(message = "The field surname cannot be empty")
    private String surname;

    /**
     * The email address of the passenger.
     * This field must not be empty and must be a valid email address format.
     *
     * @NotEmpty ensures that the email field cannot be empty.
     * @Email ensures that the email field contains a valid email address format.
     */
    @NotEmpty(message = "The field email cannot be empty")
    @Email(message = "The entered email is not valid")
    private String email;

    /**
     * The seat number assigned to the passenger on the flight.
     * This field must be an integer between 1 and 120, inclusive.
     *
     * @Min ensures that the seat number is at least 1.
     * @Max ensures that the seat number does not exceed 120.
     */
    @Min(value = 1, message = "The seat number must be at least 1")
    @Max(value = 120, message = "The flight has a maximum of 120 seats")
    private int seatNumber;
}
