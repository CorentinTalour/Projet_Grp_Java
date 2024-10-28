package fr.formation.Projet_Grp_Java.request;

import fr.formation.Projet_Grp_Java.model.CarStatus;
import fr.formation.Projet_Grp_Java.model.Company;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrUpdateCarRequest {
    @NotBlank(message = "Car brand cannot be blank")
    private String carBrand;

    @NotBlank(message = "Car model cannot be blank")
    private String carModel;

    @NotNull(message = "Daily price is required")
    private Double carDailyPrice;

    @NotBlank(message = "Car status ID cannot be blank")
    private String carStatusId;

    @NotBlank(message = "CompanyId cannot be blank")
    private String companyId;
}
