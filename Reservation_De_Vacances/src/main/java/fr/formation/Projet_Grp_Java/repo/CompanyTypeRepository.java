package fr.formation.Projet_Grp_Java.repo;

import fr.formation.Projet_Grp_Java.model.CompanyType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyTypeRepository extends JpaRepository<CompanyType, String> {
}
