package fr.formation.Projet_Grp_Java.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CompanyRequest {

    @Schema(description = "Nom de l'agence", example = "Agence de voyage ABC")
    private String nameAgency;

    @Schema(description = "Identifiant du type de société", example = "79fa913c-f06d-49af-bfe3-5d581a0a9f2d")
    private String companyTypeId;
}
