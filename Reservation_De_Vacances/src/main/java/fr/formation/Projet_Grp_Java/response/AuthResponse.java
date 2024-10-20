package fr.formation.Projet_Grp_Java.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AuthResponse {
    private boolean success;
    private String token;
}

