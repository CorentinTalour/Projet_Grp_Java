package fr.formation.Projet_Grp_Java.request;

package fr.formation.Projet_Grp_Java.request;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor

public class BookingRequest {

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime dateBegin;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime dateEnd;

    private float price;

}

