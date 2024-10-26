package fr.formation.Projet_Grp_Java.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class HotelRequest {

    private String nom;
    private String mail;
    private String telephone;
    private String ville;
    private String adresse;
    private String companyId;
}