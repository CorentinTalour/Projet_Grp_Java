package fr.formation.Projet_Grp_Java.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserRequest {

    private String name;

    private String surname;

    private boolean hasDrivingLicence;

    private String mail;

    private String telephone;

}
