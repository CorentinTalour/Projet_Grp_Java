package fr.formation.Projet_Grp_Java.api;

import java.util.List;

import fr.formation.Projet_Grp_Java.request.AuthRequest;
import fr.formation.Projet_Grp_Java.response.AuthResponse;
import fr.formation.Projet_Grp_Java.security.JwtUtil;
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
    public List<UserResponse> findAll() {
        log.debug("Finding all users ...");

        return this.utilisateurRepository.findAll()
                .stream()
                .map(this::convert)
                .toList();
    }

    @GetMapping("/{id}")
    public UserResponse findById(@PathVariable String id) {
        log.debug("Finding user {} ...", id);

        Utilisateur user = this.utilisateurRepository.findById(id).orElseThrow(UserNotFoundException::new);
        UserResponse resp = UserResponse.builder().build();

        BeanUtils.copyProperties(user, resp);

        return resp;
    }

    @GetMapping("/isAdmin")
    public boolean isAdmin() {
        var request = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        return request;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String createUser(@Valid @RequestBody UserRequest userRequest) {
        Utilisateur user = new Utilisateur();

        if (userRequest.getCompanyId() != null) {
            Company company = companyRepository.findById(userRequest.getCompanyId())
                    .orElseThrow(() -> new RuntimeException("Company not found"));
            user.setCompany(company);
        }

        user.setName(userRequest.getName());
        user.setUsername(userRequest.getUsername());
        user.setPassword(this.passwordEncoder.encode(userRequest.getPassword()));
        user.setMail(userRequest.getMail());
        user.setPhone(userRequest.getPhone());
        user.setHasDrivingLicence(userRequest.isHasDrivingLicence());
        user.setAdmin(userRequest.isAdmin());

        utilisateurRepository.save(user);
        return "User created successfully";
    }

    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public String update(@PathVariable String id, @RequestBody UserRequest request) {
        log.debug("Updating User {} ...", id);

        Utilisateur user = this.utilisateurRepository.findById(id).orElseThrow(UserNotFoundException::new);

        if (request.getCompanyId() != null) {
            Company company = companyRepository.findById(request.getCompanyId())
                    .orElseThrow(() -> new RuntimeException("Company not found"));
            user.setCompany(company);
        }

        if (request.getPassword() == null || request.getPassword().isEmpty()) {
            throw new RuntimeException("Le mot de passe ne peut pas être vide");
        }

        user.setName(request.getName());
        user.setUsername(request.getUsername());
        user.setPassword(this.passwordEncoder.encode(request.getPassword()));
        user.setMail(request.getMail());
        user.setPhone(request.getPhone());
        user.setHasDrivingLicence(request.isHasDrivingLicence());
        user.setAdmin(request.isAdmin());

        this.utilisateurRepository.save(user);

        log.debug("Utilisateur {} updated!", id);

        return user.getId();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
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
