package fr.formation.Projet_Grp_Java.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrUpdatePlaneRequest {
    @NotBlank(message = "Departure airport cannot be blank")
    private String departureAirport;

    @NotBlank(message = "Arrival airport cannot be blank")
    private String arrivalAirport;

    @NotNull(message = "Plane price is required")
    private Double planePrice;

    @NotNull(message = "Number of seats is required")
    private Integer numberOfSeats;

    @NotBlank(message = "CompanyId cannot be blank")
    private String companyId;
}
