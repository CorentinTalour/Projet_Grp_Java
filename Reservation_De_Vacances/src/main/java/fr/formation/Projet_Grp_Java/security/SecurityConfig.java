package fr.formation.Projet_Grp_Java.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {
    @Bean
        // Configuration des accès (Authorization)
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorize -> {
            // Remplacé par les annotations @PreAuthorize ...

            // authorize.requestMatchers(HttpMethod.DELETE, "/api/comment/**").hasRole("ADMIN");

            // authorize
            //     .requestMatchers(HttpMethod.POST, "/api/comment/**", "/api/video/**")
            //     .authenticated();

            // authorize
            //     .requestMatchers(HttpMethod.PUT, "/api/video/**")
            //     .authenticated();

            // On autorise tout le monde par défaut partout
            authorize.requestMatchers("/**").permitAll();
        });

        http.csrf(csrf -> csrf.disable());

        return http.build();
    }
}
