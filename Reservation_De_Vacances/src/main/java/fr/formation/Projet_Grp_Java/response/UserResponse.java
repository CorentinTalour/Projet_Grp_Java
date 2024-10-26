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
    private String surname;
    private String mail;
    private String telephone;
    private boolean has_driving_licence;
    private String companyId;
}
