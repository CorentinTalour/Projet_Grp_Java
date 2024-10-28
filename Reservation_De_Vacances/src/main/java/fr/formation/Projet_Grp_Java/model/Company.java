package fr.formation.Projet_Grp_Java.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Table(name = "company")
@Getter
@Setter
public class Company {
    @Id
    @UuidGenerator
    @Column(name = "id")
    private String id;

    @Column(name = "name", length = 50, nullable = false)
    private String nameAgency;

    @ManyToOne
    @JoinColumn(name = "company_companyType_id", nullable = false)
    private CompanyType companyType;
}
