package fr.formation.Projet_Grp_Java.response;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class BookingResponse {

    @Schema(description = "Identifiant unique de la réservation", example = "123e4567-e89b-12d3-a456-426614174000 (Non nullable)")
    private String id;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    @Schema(description = "Date et heure de début de la réservation", example = "01/11/2024 10:00 (Non nullable)")
    private LocalDateTime dateBegin;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    @Schema(description = "Date et heure de fin de la réservation", example = "02/11/2024 10:00 (Non nullable)")
    private LocalDateTime dateEnd;

    @Schema(description = "Prix total de la réservation", example = "150.0 (Non nullable)")
    private float price;

    @Schema(description = "Identifiant de l'utilisateur ayant effectué la réservation", example = "123e4567-e89b-12d3-a456-426614174000 (Non nullable)")
    private String user_id;

    @Schema(description = "Identifiant de l'hôtel associé à la réservation", example = "456e7890-e89b-12d3-a456-426614174000 (Nullable)")
    private String hotel_id;

    @Schema(description = "Identifiant du vol associé à la réservation", example = "789e1234-e89b-12d3-a456-426614174000 (Nullable)")
    private String plane_id;

    @Schema(description = "Identifiant de la voiture associée à la réservation", example = "321e4567-e89b-12d3-a456-426614174000 (Nullable)")
    private String car_id;
}
