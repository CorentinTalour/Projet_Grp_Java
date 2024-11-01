package fr.formation.Projet_Grp_Java.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.util.List;

@Entity
@Table(name = "company")
@Getter
@Setter
@Schema(description = "Représente une société dans la BDD")
public class Company {
    @Id
    @UuidGenerator
    @Column(name = "id")
    @Schema(description = "Identifiant unique de la société", example = "123e4567-e89b-12d3-a456-426614174000")
    private String id;

    @Column(name = "name", length = 50, nullable = false)
    @Schema(description = "Nom de l'agence de la société", example = "Agence de voyage ABC")
    private String nameAgency;

    @ManyToOne
    @JoinColumn(name = "company_companyType_id", nullable = false)
    @Schema(description = "Type de la société")
    private CompanyType companyType;

    @OneToMany(mappedBy = "company")
    @JsonIgnore // Cette annotation empêche la sérialisation de la liste `cars`
    @Schema(description = "Liste des voitures associées à la société")
    private List<Car> cars;

    @OneToMany(mappedBy = "company")
    @JsonIgnore // Cette annotation empêche la sérialisation de la liste `cars`
    @Schema(description = "Liste des avions associés à la société")
    private List<Plane> plane;
}
