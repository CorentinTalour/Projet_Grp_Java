package fr.formation.Projet_Grp_Java.model;

import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "carStatus")
@Getter @Setter
public class CarStatus {
    @Id
    @UuidGenerator
    @Column(name = "carStatus_id")
    private String id;

    @Column(name = "carStatus_name", length = 100, nullable = false)
    private String carStatusName;

    //Besoin d'uniquement d'une relation unidirectionnelle
    //@OneToOne
    //@JoinColumn(name = "carStatus_car_id")
    //private Car car;
}
