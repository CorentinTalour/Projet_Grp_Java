package fr.formation.Projet_Grp_Java.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

import org.hibernate.annotations.ManyToAny;
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
    @JoinColumn(name = "booking_bookingStatus_id", nullable = false)
    private BookingStatus bookingStatus;
}
