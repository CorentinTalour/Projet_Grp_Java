package fr.formation.Projet_Grp_Java.response;

import fr.formation.Projet_Grp_Java.model.CompanyType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@Schema(description = "Représente la réponse contenant les détails d'une société")
public class CompanyResponse {
    @Schema(description = "Nom de l'agence", example = "Agence de voyage ABC")
    private String nameAgency;

    @Schema(description = "Identifiant unique de la société", example = "123e4567-e89b-12d3-a456-426614174000")
    private String Id;

    @Schema(description = "Type de société", example = "Hôtel")
    private CompanyType companyType;
}
