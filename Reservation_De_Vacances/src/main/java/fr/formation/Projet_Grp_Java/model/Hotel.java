package fr.formation.Projet_Grp_Java.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
@Table(name = "hotel")
public class Hotel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Schema(description = "Identifiant unique de l'hôtel", example = "123e4567-e89b-12d3-a456-426614174000")
    private String id;

    @Schema(description = "Nom de l'hôtel", example = "Hôtel de Paris")
    private String nom;

    @Schema(description = "Adresse email de l'hôtel", example = "contact@hotelparis.com")
    @Column(nullable = false, unique = true)
    private String mail;

    @Schema(description = "Numéro de téléphone de l'hôtel", example = "+33 1 23 45 67 89")
    private String telephone;

    @Schema(description = "Ville où se trouve l'hôtel", example = "Paris")
    private String ville;

    @Schema(description = "Adresse de l'hôtel", example = "123 Rue de Rivoli, 75001 Paris, France")
    private String adresse;

    @JoinColumn(name = "company_id", nullable = false)
    @ManyToOne
    @Schema(description = "Société à laquelle appartient l'hôtel")
    private Company company;
}
