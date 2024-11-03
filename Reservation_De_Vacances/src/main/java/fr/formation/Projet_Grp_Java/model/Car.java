package fr.formation.Projet_Grp_Java.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "car")
@Getter @Setter
@Schema(description = "Représente une voiture dans le système")
public class Car {
    @Id
    @UuidGenerator
    @Column(name = "car_id")
    @Schema(description = "Identifiant unique de la voiture", example = "123e4567-e89b-12d3-a456-426614174000")
    private String id;

    @Column(name = "car_brand", length = 100, nullable = false)
    @Schema(description = "Marque de la voiture", example = "Toyota")
    private String carBrand;

    @Column(name = "car_model", length = 100, nullable = false)
    @Schema(description = "Modèle de la voiture", example = "Corolla")
    private String carModel;

    @Column(name = "car_daily_price", columnDefinition = "double precision" , nullable = false)
    @Schema(description = "Prix journalier de location", example = "50.0")
    private Double carDailyPrice;

    @ManyToOne
    @JoinColumn(name = "car_carStatus_id" , nullable = false)
    @Schema(description = "Statut de la voiture")
    private CarStatus carStatus;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    @Schema(description = "Société à laquelle appartient la voiture")
    @JsonBackReference
    private Company company;
}
