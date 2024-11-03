package fr.formation.Projet_Grp_Java.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthRequest {
    @NotBlank
    @Schema(description = "Nom d'utilisateur pour l'authentification", example = "jdupont")
    private String username;

    @NotBlank
    @Schema(description = "Mot de passe de l'utilisateur pour l'authentification", example = "password123")
    private String password;
}
