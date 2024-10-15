package fr.formation.Projet_Grp_Java.api;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import fr.formation.Projet_Grp_Java.exception.HotelNotFoundException;
import fr.formation.Projet_Grp_Java.exception.UserNotFoundException;
import fr.formation.Projet_Grp_Java.model.Hotel;
import fr.formation.Projet_Grp_Java.model.Utilisateur;
import fr.formation.Projet_Grp_Java.repo.UtilisateurRepository;
import fr.formation.Projet_Grp_Java.request.UserRequest;
import fr.formation.Projet_Grp_Java.response.HotelResponse;
import fr.formation.Projet_Grp_Java.response.UserResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Log4j2
public class UserController {

    private final UtilisateurRepository utilisateurRepository;

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

    @PostMapping
    // @ResponseStatus(HttpStatus.CREATED)

    public String createUser(@RequestBody UserRequest userRequest) {

        Utilisateur user = new Utilisateur();

        user.setName(userRequest.getName());
        user.setSurname(userRequest.getSurname());
        user.setMail(userRequest.getMail());
        user.setTelephone(userRequest.getTelephone());
        user.setHasDrivingLicence(userRequest.isHasDrivingLicence());

        utilisateurRepository.save(user);
        return "User created successfully";
    }

    private UserResponse convert(Utilisateur utilisateur) {
        UserResponse resp = UserResponse.builder().build();

        BeanUtils.copyProperties(utilisateur, resp);

        return resp;
    }
}
