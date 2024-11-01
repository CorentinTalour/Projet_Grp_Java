package fr.formation.Projet_Grp_Java.response;

import fr.formation.Projet_Grp_Java.model.CarStatus;
import fr.formation.Projet_Grp_Java.model.Company;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@Schema(description = "Représente la réponse contenant les détails d'une voiture")
public class CarResponse {
    @Schema(description = "Identifiant unique de la voiture", example = "123e4567-e89b-12d3-a456-426614174000")
    private String id;

    @Schema(description = "Marque de la voiture", example = "Toyota")
    private String carBrand;

    @Schema(description = "Modèle de la voiture", example = "Corolla")
    private String carModel;

    @Schema(description = "Prix journalier de location", example = "50.0")
    private Double carDailyPrice;

    @Schema(description = "Statut de la voiture")
    private CarStatus carStatus;

    @Schema(description = "Société à laquelle appartient la voiture")
    private Company company;
}