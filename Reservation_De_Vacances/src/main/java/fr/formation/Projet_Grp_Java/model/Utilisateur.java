package fr.formation.Projet_Grp_Java.model;

import io.swagger.v3.oas.annotations.media.Schema;
import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Table(name = "utilisateur")
@Entity
public class Utilisateur {

    @Id
    @UuidGenerator
    @Column(name = "user_id")
    @Schema(description = "Identifiant unique de l'utilisateur", example = "123e4567-e89b-12d3-a456-426614174000")
    private String id;

    @Column(name = "user_name", nullable = false)
    @Schema(description = "Nom de l'utilisateur", example = "Jean Dupont")
    private String name;

    @Column(name = "user_username", nullable = false)
    @Schema(description = "Nom d'utilisateur pour la connexion", example = "jdupont")
    private String username;

    @Column(name = "user_password", nullable = false)
    @Schema(description = "Mot de passe de l'utilisateur", example = "password123")
    private String password;

    @Column(name = "user_has_driving_licence", nullable = false)
    @Schema(description = "Indique si l'utilisateur a un permis de conduire", example = "true")
    private boolean hasDrivingLicence;

    @Column(name = "user_mail", nullable = false, unique = false)
    @Schema(description = "Adresse email de l'utilisateur", example = "jean.dupont@example.com")
    private String mail;

    @Column(name = "user_phone", nullable = false, unique = false)
    @Schema(description = "Numéro de téléphone de l'utilisateur", example = "+33123456789")
    private String phone;

    @Column(name = "usr_admin", nullable = false)
    @Schema(description = "Indique si l'utilisateur est un administrateur", example = "false")
    private boolean admin;

    // Relation avec l'agence (Many-to-One)
    @ManyToOne
    @JoinColumn(name = "company_id", nullable = true)
    @Schema(description = "Société à laquelle appartient l'utilisateur")
    private Company company;

}
