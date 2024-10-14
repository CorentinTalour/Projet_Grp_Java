package fr.formation.Projet_Grp_Java.request;

import fr.formation.Projet_Grp_Java.model.CarStatus;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateCarRequest {
    private String carBrand;
    private String carModel;
    private Double carDailyPrice;
    //private CarStatus carStatus;
}
