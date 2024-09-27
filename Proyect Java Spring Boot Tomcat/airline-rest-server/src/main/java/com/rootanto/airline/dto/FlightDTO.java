package com.rootanto.airline.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Data Transfer Object (DTO) for representing flight information.
 * This class encapsulates the details of a flight for transfer between different layers of the application.
 * It includes validation constraints to ensure that the data adheres to expected formats and values.
 * Lombok's @Data annotation generates boilerplate code such as getters, setters, toString(), equals(), and hashCode() methods.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FlightDTO {

    /**
     * Unique identifier for the flight.
     * This field is used to uniquely identify a flight record.
     * It is not validated here and can be an alphanumeric string.
     */
    private String id;

    /**
     * The origin of the flight.
     * This field must be a non-empty string consisting of one or more letters.
     *
     * @NotEmpty ensures that the origin field cannot be empty.
     * @Pattern ensures that the origin field contains only letters (A-Z, a-z).
     *
     * Example:
     * - Valid: "NewYork"
     * - Invalid: "", "1234", "New York" (contains spaces)
     */
    @NotEmpty(message = "The field origin cannot be empty")
    @Pattern(regexp = "[A-Za-z]+", message = "The field origin only allows letters")
    private String origin;

    /**
     * The destination of the flight.
     * This field must be a non-empty string consisting of one or more letters.
     *
     * @NotEmpty ensures that the destination field cannot be empty.
     * @Pattern ensures that the destination field contains only letters (A-Z, a-z).
     *
     * Example:
     * - Valid: "LosAngeles"
     * - Invalid: "", "5678", "Los Angeles" (contains spaces)
     */
    @NotEmpty(message = "The field destination cannot be empty")
    @Pattern(regexp = "[A-Za-z]+", message = "The field destination only allows letters")
    private String destination;

    /**
     * The date of the flight.
     * This field must be a non-empty string formatted as "dd/MM/yyyy".
     * Note: Although the field is of type `Date`, validation for the date format should ideally be done
     * using a `String` with `@Pattern` and then converted to `Date` in the service layer.
     *
     * @NotEmpty ensures that the date field cannot be empty.
     * @Pattern ensures that the date field matches the format "dd/MM/yyyy", where:
     * - dd: Day of the month (01 to 31)
     * - MM: Month (01 to 12)
     * - yyyy: Year (4 digits)
     *
     * Example:
     * - Valid: "01/01/2024"
     * - Invalid: "2024/01/01", "01-01-2024", "01/01/24", ""
     */
    @NotEmpty(message = "The field date cannot be empty")
    @Future
    private Date date;

}

