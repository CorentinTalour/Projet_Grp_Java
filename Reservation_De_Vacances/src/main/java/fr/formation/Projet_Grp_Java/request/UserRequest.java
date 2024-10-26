package fr.formation.Projet_Grp_Java.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter @Setter
@Builder
@NoArgsConstructor @AllArgsConstructor
public class UserRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @NotNull
    private boolean hasDrivingLicence;

    @NotBlank
    private String mail;

    @NotBlank
    private String phone;

    @NotNull
    private boolean admin;
}
