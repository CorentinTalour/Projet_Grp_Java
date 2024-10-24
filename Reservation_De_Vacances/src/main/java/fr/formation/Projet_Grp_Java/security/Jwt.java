package fr.formation.Projet_Grp_Java.security;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Builder
public class Jwt {
    private String userId;
    private String username;
    private boolean valid;
}
