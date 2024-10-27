package fr.formation.Projet_Grp_Java.response;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class PlaneByIdResponse {
    private String departureAirport;
    private String arrivalAirport;
    private Double planePrice;
    private Integer numberOfSeats;
}
