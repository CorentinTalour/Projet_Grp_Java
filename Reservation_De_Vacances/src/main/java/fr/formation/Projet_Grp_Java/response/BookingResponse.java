package fr.formation.Projet_Grp_Java.response;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class BookingResponse {

    private String id;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime dateBegin;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime dateEnd;

    private float price;

    private String user_id;

    private String hotel_id;

    private String plane_id;

    private String car_id;
}
