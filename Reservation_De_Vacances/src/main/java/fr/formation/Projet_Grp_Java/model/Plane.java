package fr.formation.Projet_Grp_Java.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Table(name = "plane")
@Getter
@Setter
public class Plane {
    @Id
    @UuidGenerator
    @Column(name = "plane_id")
    @Schema(description = "Identifiant unique de l'avion", example = "123e4567-e89b-12d3-a456-426614174000")
    private String id;

    @Column(name = "plane_departure_airport", nullable = false)
    @Schema(description = "Aéroport de départ de l'avion", example = "CDG")
    private String departureAirport;

    @Column(name = "plane_arrival_airport", nullable = false)
    @Schema(description = "Aéroport d'arrivée de l'avion", example = "LAX")
    private String arrivalAirport;

    @Column(name = "plane_price", columnDefinition = "double precision" , nullable = false)
    @Schema(description = "Prix du billet de l'avion", example = "299.99")
    private Double planePrice;

    @Column(name = "plane_number_of_seats", nullable = false)
    @Min(1) //S'assure que le nombre de sièges est au moins 1
    @Schema(description = "Nombre de sièges disponibles dans l'avion", example = "150")
    private Integer numberOfSeats;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    @Schema(description = "Société à laquelle appartient l'avion")
    @JsonBackReference
    private Company company;
}
