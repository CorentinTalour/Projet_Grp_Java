package fr.formation.Projet_Grp_Java.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.util.List;

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

    @OneToMany(mappedBy = "company")
    @JsonIgnore // Cette annotation empêche la sérialisation de la liste `cars`
    private List<Car> cars;

    @OneToMany(mappedBy = "company")
    @JsonIgnore // Cette annotation empêche la sérialisation de la liste `cars`
    private List<Plane> plane;
}
