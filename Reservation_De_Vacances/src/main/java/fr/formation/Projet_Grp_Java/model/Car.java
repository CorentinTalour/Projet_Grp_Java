package fr.formation.Projet_Grp_Java.model;

import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "car")
@Getter @Setter
public class Car {
    @Id
    @UuidGenerator
    @Column(name = "car_id")
    private String id;

    @Column(name = "car_brand", length = 100, nullable = false)
    private String carBrand;

    @Column(name = "car_model", length = 100, nullable = false)
    private String carModel;

    @Column(name = "car_daily_price", columnDefinition = "double precision" , nullable = false)
    private Double carDailyPrice;

    @ManyToOne
    @JoinColumn(name = "car_carStatus_id" , nullable = false)
    private CarStatus carStatus;
}
