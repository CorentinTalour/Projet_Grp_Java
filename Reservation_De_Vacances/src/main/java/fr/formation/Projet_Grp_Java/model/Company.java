package fr.formation.Projet_Grp_Java.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
}
