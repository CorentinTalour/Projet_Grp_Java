package fr.formation.Projet_Grp_Java.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.formation.Projet_Grp_Java.model.User;

public interface UtilisateurRepository extends JpaRepository<User, String> {

}
