package fr.formation.Projet_Grp_Java.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import org.hibernate.annotations.UuidGenerator;

@Entity
@Table(name = "book")
@Getter
@Setter
public class Book {

    @Id
    @UuidGenerator
    @Column(name = "id")
    private String id;
}
