package fr.formation.Projet_Grp_Java.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.formation.Projet_Grp_Java.model.Utilisateur;

import java.util.Optional;

public interface UtilisateurRepository extends JpaRepository<Utilisateur, String> {
    public Optional<Utilisateur> findByUsername(String username);

}
