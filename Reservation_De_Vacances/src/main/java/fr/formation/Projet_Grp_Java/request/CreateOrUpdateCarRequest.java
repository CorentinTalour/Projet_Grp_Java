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
@Schema(description = "Requête pour créer ou mettre à jour une voiture")
public class CreateOrUpdateCarRequest {
    @NotBlank(message = "Car brand cannot be blank")
    @Schema(description = "Marque de la voiture", example = "Toyota")
    private String carBrand;

    @NotBlank(message = "Car model cannot be blank")
    @Schema(description = "Modèle de la voiture", example = "Corolla")
    private String carModel;

    @NotNull(message = "Daily price is required")
    @Schema(description = "Prix journalier de location", example = "50.0")
    private Double carDailyPrice;

    @NotBlank(message = "Car status ID cannot be blank")
    @Schema(description = "Identifiant du statut de la voiture", example = "1")
    private String carStatusId;

    @NotBlank(message = "CompanyId cannot be blank")
    @Schema(description = "Identifiant de la société", example = "123e4567-e89b-12d3-a456-426614174000")
    private String companyId;
}
