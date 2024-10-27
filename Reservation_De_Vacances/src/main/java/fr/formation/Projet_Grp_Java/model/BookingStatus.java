package fr.formation.Projet_Grp_Java.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "bookingStatus")
@Getter @Setter
public class BookingStatus {
    @Id
    @Column(name = "bookingStatus_id")
    private String id;

    @Column(name = "bookingStatus_name", length = 100, nullable = false)
    private String carStatusName;
}
