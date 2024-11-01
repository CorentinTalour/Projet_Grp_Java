package fr.formation.Projet_Grp_Java.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Setter;
import lombok.Getter;
import lombok.Builder;

@Getter
@Setter
@Builder
public class UserResponse {
    @Schema(description = "Identifiant unique de l'utilisateur", example = "123e4567-e89b-12d3-a456-426614174000")
    private String id;

    @Schema(description = "Nom de l'utilisateur", example = "Jean Dupont")
    private String name;

    @Schema(description = "Nom d'utilisateur pour la connexion", example = "jdupont")
    private String username;

    @Schema(description = "Adresse email de l'utilisateur", example = "jean.dupont@example.com")
    private String mail;

    @Schema(description = "Numéro de téléphone de l'utilisateur", example = "+33 1 23 45 67 89")
    private String phone;

    @Schema(description = "Indique si l'utilisateur possède un permis de conduire", example = "true")
    private boolean has_driving_licence;

    @Schema(description = "Indique si l'utilisateur a des privilèges d'administrateur", example = "false")
    private boolean admin;

    @Schema(description = "Identifiant de la société à laquelle appartient l'utilisateur", example = "456e7890-e89b-12d3-a456-426614174000")
    private String companyId;
}
