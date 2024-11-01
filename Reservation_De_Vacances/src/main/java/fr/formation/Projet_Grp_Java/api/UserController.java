package fr.formation.Projet_Grp_Java.api;

import java.util.List;

import fr.formation.Projet_Grp_Java.request.AuthRequest;
import fr.formation.Projet_Grp_Java.response.AuthResponse;
import fr.formation.Projet_Grp_Java.security.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import fr.formation.Projet_Grp_Java.exception.UserNotFoundException;
import fr.formation.Projet_Grp_Java.model.Company;
import fr.formation.Projet_Grp_Java.model.Utilisateur;
import fr.formation.Projet_Grp_Java.repo.CompanyRepository;
import fr.formation.Projet_Grp_Java.repo.UtilisateurRepository;
import fr.formation.Projet_Grp_Java.request.UserRequest;
import fr.formation.Projet_Grp_Java.response.UserResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
//Pokeball
//shrek
//Fusée
//POkeball trou au milleu
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Log4j2
public class UserController {

    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final CompanyRepository companyRepository;

    @PostMapping("/auth")
    @Operation(summary = "Authentifier un utilisateur",
            description = "Authentifie l'utilisateur avec les informations d'identification fournies.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Authentification réussie",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AuthResponse.class))),
            @ApiResponse(responseCode = "401", description = "Échec de l'authentification en raison de mauvaises informations d'identification",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public AuthResponse auth(@Valid @RequestBody AuthRequest request) {
        log.debug("Authenticating requested for user {} ...", request.getUsername());

        try {
            Authentication authentication = new UsernamePasswordAuthenticationToken(request.getUsername(),
                    request.getPassword());

            authentication = this.authenticationManager.authenticate(authentication);

            SecurityContextHolder.getContext().setAuthentication(authentication);

            log.debug("User {} authenticated! Generating token ...", request.getUsername());

            String token = JwtUtil.generate(this.utilisateurRepository.findByUsername(request.getUsername())
                    .orElseThrow(UserNotFoundException::new));

            log.debug("Token *** generated!");

            return AuthResponse.builder()
                    .success(true)
                    .token(token)
                    .build();
        }

        catch (BadCredentialsException e) {
            return AuthResponse.builder()
                    .success(false)
                    .build();
        }
    }

    @GetMapping
    @Operation(summary = "Récupérer tous les utilisateurs",
            description = "Retourne une liste de tous les utilisateurs dans la base de données.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Récupération réussie des utilisateurs",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public List<UserResponse> findAll() {
        log.debug("Finding all users ...");

        return this.utilisateurRepository.findAll()
                .stream()
                .map(this::convert)
                .toList();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer un utilisateur par ID",
            description = "Retourne un utilisateur identifié par son ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Récupération réussie de l'utilisateur",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "404", description = "Utilisateur non trouvé",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public UserResponse findById(@PathVariable String id) {
        log.debug("Finding user {} ...", id);

        Utilisateur user = this.utilisateurRepository.findById(id).orElseThrow(UserNotFoundException::new);
        UserResponse resp = UserResponse.builder().build();

        BeanUtils.copyProperties(user, resp);

        return resp;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Créer un nouvel utilisateur",
            description = "Crée un nouvel utilisateur dans la base de données.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Utilisateur créé avec succès",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Données d'entrée invalides",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public String createUser(@Valid @RequestBody UserRequest userRequest) {

        Company company = companyRepository.findById(userRequest.getCompanyId())
                .orElseThrow(() -> new RuntimeException("Company not found"));
        Utilisateur user = new Utilisateur();

        user.setName(userRequest.getName());
        user.setUsername(userRequest.getUsername());
        user.setPassword(this.passwordEncoder.encode(userRequest.getPassword()));
        user.setMail(userRequest.getMail());
        user.setPhone(userRequest.getPhone());
        user.setHasDrivingLicence(userRequest.isHasDrivingLicence());
        user.setAdmin(userRequest.isAdmin());
        user.setCompany(company);
        user.setAdmin(userRequest.isAdmin());

        utilisateurRepository.save(user);
        return "User created successfully";
    }

    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Mettre à jour un utilisateur existant",
            description = "Met à jour un utilisateur identifié par son ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Utilisateur mis à jour avec succès",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Utilisateur non trouvé",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Données d'entrée invalides",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public String update(@PathVariable String id, @RequestBody UserRequest request) {
        log.debug("Updating video {} ...", id);

        Utilisateur user = this.utilisateurRepository.findById(id).orElseThrow(UserNotFoundException::new);

        Company company = companyRepository.findById(request.getCompanyId())
                .orElseThrow(() -> new RuntimeException("Company not found"));

        user.setName(request.getName());
        user.setUsername(request.getUsername());
        user.setPassword(this.passwordEncoder.encode(request.getPassword()));
        user.setMail(request.getMail());
        user.setPhone(request.getPhone());
        user.setHasDrivingLicence(request.isHasDrivingLicence());
        user.setAdmin(request.isAdmin());
        user.setCompany(company);

        this.utilisateurRepository.save(user);

        log.debug("Utilisateur {} updated!", id);

        return user.getId();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Supprimer un utilisateur par ID",
            description = "Supprime un utilisateur identifié par son ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Utilisateur supprimé avec succès"),
            @ApiResponse(responseCode = "404", description = "Utilisateur non trouvé",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public void deleteById(@PathVariable String id) {
        log.debug("Deleting utilisateur {} ...", id);

        this.utilisateurRepository.deleteById(id);

        log.debug("Utilisateur {} deleted!", id);
    }

    private UserResponse convert(Utilisateur utilisateur) {
        UserResponse resp = UserResponse.builder().build();

        BeanUtils.copyProperties(utilisateur, resp);

        return resp;
    }
}
