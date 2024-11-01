package fr.formation.Projet_Grp_Java.model;

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
    private String id;

    @Column(name = "user_name", nullable = false)
    private String name;

    @Column(name = "user_username", nullable = false)
    private String username;

    @Column(name = "user_password", nullable = false)
    private String password;

    @Column(name = "user_has_driving_licence", nullable = false)
    private boolean hasDrivingLicence;

    @Column(name = "user_mail", nullable = false, unique = false)
    private String mail;

    @Column(name = "user_phone", nullable = false, unique = false)
    private String phone;

    @Column(name = "usr_admin", nullable = false)
    private boolean admin;

    // Relation avec l'agence (Many-to-One)
    @ManyToOne
    @JoinColumn(name = "company_id", nullable = true)
    private Company company;

    // Relation avec les réservations de voitures (One-to-Many)
    /*
     * @OneToMany(mappedBy = "user")
     * private List<ReservationVoiture> reservationVoitures;
     * 
     * // Relation avec les réservations de vols (One-to-Many)
     * 
     * @OneToMany(mappedBy = "user")
     * private List<ReservationVol> reservationVols;
     * 
     * // Relation avec les réservations d'hôtels (One-to-Many)
     * 
     * @OneToMany(mappedBy = "user")
     * private List<ReservationHotel> reservationHotels;
     * 
     * // Relation avec l'agence (Many-to-One)
     * 
     * @ManyToOne
     * 
     * @JoinColumn(name = "agence_id")
     * private Agence agence;
     * 
     * // Autres réservations (One-to-Many)
     * 
     * @OneToMany(mappedBy = "user")
     * private List<Reservation> reservations;
     */
}
