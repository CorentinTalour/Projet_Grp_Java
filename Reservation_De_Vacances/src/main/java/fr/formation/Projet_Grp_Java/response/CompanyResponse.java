package fr.formation.Projet_Grp_Java.response;

import fr.formation.Projet_Grp_Java.model.CompanyType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder

public class CompanyResponse {

    private String nameAgency;
    private String Id;
    private CompanyType companyType;
}
