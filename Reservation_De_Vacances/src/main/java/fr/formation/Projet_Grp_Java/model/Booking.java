package fr.formation.Projet_Grp_Java.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.AssertTrue;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

import org.hibernate.annotations.UuidGenerator;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "booking")
@Getter
@Setter
public class Booking {

    @Id
    @UuidGenerator
    @Column(name = "id")
    private String id;

    @JsonFormat(pattern = "dd/MM/yyyy")
    @Column(name = "booking_begin", nullable = false)
    private LocalDateTime dateBegin;

    @JsonFormat(pattern = "dd/MM/yyyy")
    @Column(name = "booking_end", nullable = false)
    private LocalDateTime dateEnd;

    private float price;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Utilisateur user;

    @ManyToOne
    @JoinColumn(name = "hotel_id", nullable = true)
    private Hotel hotel;

    @ManyToOne
    @JoinColumn(name = "plane_id", nullable = true)
    private Plane plane;

    @ManyToOne
    @JoinColumn(name = "car_id", nullable = true)
    private Car car;

    // Méthode de validation pour vérifier qu'au moins une relation est présente
    @AssertTrue(message = "At least one of hotel, plane, or car must be provided.")
    private boolean isAtLeastOneRelationPresent() {
        return hotel != null || plane != null || car != null;
    }
}
