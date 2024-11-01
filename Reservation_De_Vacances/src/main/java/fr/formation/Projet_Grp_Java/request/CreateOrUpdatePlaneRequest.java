package fr.formation.Projet_Grp_Java.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Requête pour créer ou mettre à jour un avion")
public class CreateOrUpdatePlaneRequest {
    @NotBlank(message = "Departure airport cannot be blank")
    @Schema(description = "Aéroport de départ de l'avion", example = "CDG")
    private String departureAirport;

    @NotBlank(message = "Arrival airport cannot be blank")
    @Schema(description = "Aéroport d'arrivée de l'avion", example = "LAX")
    private String arrivalAirport;

    @NotNull(message = "Plane price is required")
    @Schema(description = "Prix de l'avion", example = "299.99")
    private Double planePrice;

    @NotNull(message = "Number of seats is required")
    @Schema(description = "Nombre de sièges disponibles dans l'avion", example = "150")
    private Integer numberOfSeats;

    @NotBlank(message = "CompanyId cannot be blank")
    @Schema(description = "Identifiant de la société à laquelle appartient l'avion", example = "123e4567-e89b-12d3-a456-426614174000")
    private String companyId;
}
