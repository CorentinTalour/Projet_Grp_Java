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
public class UserRequest {

    @NotBlank
    @Schema(description = "Nom de l'utilisateur", example = "Jean Dupont")
    private String name;

    @NotBlank
    @Schema(description = "Nom d'utilisateur pour la connexion", example = "jdupont")
    private String username;

    @NotBlank @NotNull
    @NotBlank
    @Schema(description = "Mot de passe de l'utilisateur", example = "password123")
    private String password;

    @NotNull
    @Schema(description = "Indique si l'utilisateur a un permis de conduire", example = "true")
    private boolean hasDrivingLicence;

    @NotBlank
    @Schema(description = "Adresse email de l'utilisateur", example = "jean.dupont@example.com")
    private String mail;

    @NotBlank
    @Schema(description = "Numéro de téléphone de l'utilisateur", example = "+33123456789")
    private String phone;

    @NotNull
    @Schema(description = "Indique si l'utilisateur est un administrateur", example = "false")
    private boolean admin;

    @Schema(description = "Identifiant de la société à laquelle appartient l'utilisateur", example = "456e7890-e89b-12d3-a456-426614174000")
    private String companyId;
}
