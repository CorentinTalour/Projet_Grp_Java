package fr.formation.Projet_Grp_Java.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import fr.formation.Projet_Grp_Java.model.Hotel;

public interface HotelRepository extends JpaRepository<Hotel, String> {
    // JpaRepository fournit déjà toutes les méthodes CRUD standard
}