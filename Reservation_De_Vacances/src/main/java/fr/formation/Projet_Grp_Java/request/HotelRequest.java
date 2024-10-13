package fr.formation.Projet_Grp_Java.request;

import org.hibernate.annotations.UuidGenerator;

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
    // agenceId pourrait être ajouté si tu souhaites inclure une relation vers une
    // agence
}