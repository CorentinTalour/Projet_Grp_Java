package fr.formation.Projet_Grp_Java.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
public class HotelResponse {
    private String id; // Inclure l'ID généré
    private String nom;
    private String mail;
    private String telephone;
    private String ville;
    private String adresse;
    // Tu peux également ajouter des informations supplémentaires ici
}
