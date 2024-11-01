package fr.formation.Projet_Grp_Java.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class HotelResponse {
    @Schema(description = "Identifiant unique de l'hôtel", example = "123e4567-e89b-12d3-a456-426614174000")
    private String id;

    @Schema(description = "Nom de l'hôtel", example = "Hôtel de Paris")
    private String nom;

    @Schema(description = "Adresse email de l'hôtel", example = "contact@hotelparis.com")
    private String mail;

    @Schema(description = "Numéro de téléphone de l'hôtel", example = "+33 1 23 45 67 89")
    private String telephone;

    @Schema(description = "Ville où se trouve l'hôtel", example = "Paris")
    private String ville;

    @Schema(description = "Adresse de l'hôtel", example = "123 Rue de Rivoli, 75001 Paris, France")
    private String adresse;

    @Schema(description = "Identifiant de la société à laquelle appartient l'hôtel", example = "456e7890-e89b-12d3-a456-426614174000")
    private String companyId;
}