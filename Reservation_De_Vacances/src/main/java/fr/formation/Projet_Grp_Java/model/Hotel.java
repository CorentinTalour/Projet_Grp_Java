package fr.formation.Projet_Grp_Java.model;

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
    private String id;

    private String nom;

    @Column(nullable = false, unique = true)
    private String mail;

    private String telephone;

    private String ville;

    private String adresse;

    @JoinColumn(name = "company_id", nullable = false)
    @ManyToOne
    private Company company;
}
