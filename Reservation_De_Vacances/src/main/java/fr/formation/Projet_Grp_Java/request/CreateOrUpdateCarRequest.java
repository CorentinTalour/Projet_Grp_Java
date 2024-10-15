package fr.formation.Projet_Grp_Java.request;

import fr.formation.Projet_Grp_Java.model.CarStatus;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrUpdateCarRequest {
    private String carBrand;
    private String carModel;
    private Double carDailyPrice;
    private String carStatusId;
}
