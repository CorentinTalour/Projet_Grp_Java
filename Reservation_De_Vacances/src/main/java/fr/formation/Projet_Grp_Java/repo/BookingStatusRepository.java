package fr.formation.Projet_Grp_Java.repo;

import fr.formation.Projet_Grp_Java.model.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingStatusRepository extends JpaRepository<BookingStatus, String> {
}
