package fr.formation.Projet_Grp_Java.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
    private String id;

    @Column(name = "plane_departure_airport", nullable = false)
    private String departureAirport;

    @Column(name = "plane_arrival_airport", nullable = false)
    private String arrivalAirport;

    @Column(name = "plane_price", columnDefinition = "double precision" , nullable = false)
    private Double planePrice;

    @Column(name = "plane_number_of_seats", nullable = false)
    @Min(1) //S'assure que le nombre de sièges est au moins 1
    private Integer numberOfSeats;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    @JsonBackReference
    private Company company;
}
