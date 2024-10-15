package fr.formation.Projet_Grp_Java.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.formation.Projet_Grp_Java.model.Utilisateur;

public interface UtilisateurRepository extends JpaRepository<Utilisateur, String> {

}
