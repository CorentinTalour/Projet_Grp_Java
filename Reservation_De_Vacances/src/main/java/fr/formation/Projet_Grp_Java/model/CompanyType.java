package fr.formation.Projet_Grp_Java.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "companyType")
@Getter @Setter
public class CompanyType {
    @Id
    @Column(name = "companyType_id")
    private String id;

    @Column(name = "companyType_name", length = 100, nullable = false)
    private String companyTypeName;
}
