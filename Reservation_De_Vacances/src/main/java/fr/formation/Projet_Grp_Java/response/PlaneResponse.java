package fr.formation.Projet_Grp_Java.response;

import fr.formation.Projet_Grp_Java.model.Company;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
@Getter
@Setter
@SuperBuilder
public class PlaneResponse {
    @Schema(description = "Identifiant unique de l'avion", example = "123e4567-e89b-12d3-a456-426614174000")
    private String id;

    @Schema(description = "Aéroport de départ de l'avion", example = "CDG")
    private String departureAirport;

    @Schema(description = "Aéroport d'arrivée de l'avion", example = "LAX")
    private String arrivalAirport;

    @Schema(description = "Prix de l'avion", example = "299.99")
    private Double planePrice;

    @Schema(description = "Nombre de sièges disponibles dans l'avion", example = "150")
    private Integer numberOfSeats;

    @Schema(description = "Société à laquelle appartient l'avion")
    private Company company;
}