package fr.formation.Projet_Grp_Java.security;

import fr.formation.Projet_Grp_Java.model.Utilisateur;
import fr.formation.Projet_Grp_Java.repo.UtilisateurRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class JpaUserDetailsService implements UserDetailsService {
    private final UtilisateurRepository repository;

    public JpaUserDetailsService(UtilisateurRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Utilisateur user = this.repository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return org.springframework.security.core.userdetails.User
                .withUsername(username)
                .password(user.getPassword())
                .build()
                ;
    }
}
