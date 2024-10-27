package fr.formation.Projet_Grp_Java.response;

import fr.formation.Projet_Grp_Java.model.CarStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class CarResponse {
    private String id;
    private String carBrand;
    private String carModel;
    private Double carDailyPrice;
    private CarStatus carStatus;
}
