package org.educa.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductFlightDTO {
    @NonNull
    private String id;
    @NonNull
    private String origin;
    @NonNull
    private String destination;
    @NonNull
    private String dateString;
}
