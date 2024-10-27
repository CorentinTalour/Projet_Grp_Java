package fr.formation.Projet_Grp_Java.repo;

import fr.formation.Projet_Grp_Java.model.CarStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarStatusRepository extends JpaRepository<CarStatus, String> {
}
