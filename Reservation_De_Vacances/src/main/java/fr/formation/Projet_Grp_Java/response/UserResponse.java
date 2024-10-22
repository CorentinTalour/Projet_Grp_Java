package fr.formation.Projet_Grp_Java.response;

import lombok.Setter;
import lombok.Getter;
import lombok.Builder;

@Getter
@Setter
@Builder
public class UserResponse {

    private String id;
    private String name;
    private String username;
    private String mail;
    private String phone;
    private boolean has_driving_licence;

}
