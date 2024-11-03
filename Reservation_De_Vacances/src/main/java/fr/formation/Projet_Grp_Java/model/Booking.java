package fr.formation.Projet_Grp_Java.model;

import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(description = "Identifiant unique de la réservation", example = "123e4567-e89b-12d3-a456-426614174000 (Non nullable)")
    private String id;

    @JsonFormat(pattern = "dd/MM/yyyy")
    @Column(name = "booking_begin", nullable = false)
    @Schema(description = "Date et heure de début de la réservation", example = "01/11/2024 10:00 (Non nullable)")
    private LocalDateTime dateBegin;

    @JsonFormat(pattern = "dd/MM/yyyy")
    @Column(name = "booking_end", nullable = false)
    @Schema(description = "Date et heure de fin de la réservation", example = "02/11/2024 10:00 (Non nullable)")
    private LocalDateTime dateEnd;

    @Schema(description = "Prix total de la réservation", example = "150.0 (Non nullable)")
    private float price;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @Schema(description = "Utilisateur ayant effectué la réservation (Non nullable)")
    private Utilisateur user;

    @ManyToOne
    @JoinColumn(name = "hotel_id", nullable = true)
    @Schema(description = "Hôtel associé à la réservation (Nullable)")
    private Hotel hotel;

    @ManyToOne
    @JoinColumn(name = "plane_id", nullable = true)
    @Schema(description = "Vol associé à la réservation (Nullable)")
    private Plane plane;

    @ManyToOne
    @JoinColumn(name = "car_id", nullable = true)
    @Schema(description = "Voiture associée à la réservation (Nullable)")
    private Car car;

    // Méthode de validation pour vérifier qu'au moins une relation est présente
    @AssertTrue(message = "At least one of hotel, plane, or car must be provided.")
    @Schema(description = "Vérifie qu'au moins une des relations (hôtel, vol ou voiture) est présente")
    private boolean isAtLeastOneRelationPresent() {
        return hotel != null || plane != null || car != null;
    }
}
