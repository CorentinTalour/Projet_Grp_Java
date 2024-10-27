package fr.formation.Projet_Grp_Java.repo;

import fr.formation.Projet_Grp_Java.model.Plane;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaneRepository extends JpaRepository<Plane, String> {
}
