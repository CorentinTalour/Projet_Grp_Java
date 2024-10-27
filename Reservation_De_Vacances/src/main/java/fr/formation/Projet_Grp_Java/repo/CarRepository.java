package fr.formation.Projet_Grp_Java.repo;

import fr.formation.Projet_Grp_Java.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarRepository extends JpaRepository<Car, String> {
}
