package fr.formation.Projet_Grp_Java.model;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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

    @Column(name = "user_name")
    private String name;

    private String surname;

    @Column(name = "has_driving_licence")
    private boolean hasDrivingLicence;

    @Column(nullable = false)
    private String mail;

    private String telephone;

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
