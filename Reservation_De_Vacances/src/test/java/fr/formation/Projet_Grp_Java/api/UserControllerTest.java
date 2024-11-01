package fr.formation.Projet_Grp_Java.api;

import fr.formation.Projet_Grp_Java.exception.UserNotFoundException;
import fr.formation.Projet_Grp_Java.model.Company;
import fr.formation.Projet_Grp_Java.model.Utilisateur;
import fr.formation.Projet_Grp_Java.request.AuthRequest;
import fr.formation.Projet_Grp_Java.request.UserRequest;
import fr.formation.Projet_Grp_Java.response.AuthResponse;
import fr.formation.Projet_Grp_Java.response.UserResponse;
import fr.formation.Projet_Grp_Java.repo.CompanyRepository;
import fr.formation.Projet_Grp_Java.repo.UtilisateurRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

        @InjectMocks
        private UserController userController;

        @Mock
        private UtilisateurRepository utilisateurRepository;

        @Mock
        private PasswordEncoder passwordEncoder;

        @Mock
        private AuthenticationManager authenticationManager;

        @Mock
        private CompanyRepository companyRepository;

        private Utilisateur utilisateur;
        private UserResponse userResponse;

        @BeforeEach
        void setUp() {
                // Initialiser les objets Utilisateur et UserResponse pour les tests
                utilisateur = new Utilisateur();
                utilisateur.setId("1");
                utilisateur.setName("Test User");
                utilisateur.setUsername("testuser");
                utilisateur.setMail("testuser@example.com");
                utilisateur.setPhone("1234567890");
                utilisateur.setHasDrivingLicence(true);
                utilisateur.setAdmin(false);

        }

        @Test
        void shouldAuthenticateUser() {
                // given
                AuthRequest authRequest = new AuthRequest();
                authRequest.setUsername("testuser");
                authRequest.setPassword("password");

                when(utilisateurRepository.findByUsername("testuser")).thenReturn(Optional.of(utilisateur));
                when(authenticationManager.authenticate(any())).thenReturn(null); // Simulate successful authentication

                // when
                AuthResponse response = userController.auth(authRequest);

                // then
                assertTrue(response.isSuccess());
                assertNotNull(response.getToken());
                verify(utilisateurRepository).findByUsername("testuser");
        }

        // @Test
        // void shouldReturnAllUsers() {
        // // given
        // when(utilisateurRepository.findAll()).thenReturn(Collections.singletonList(utilisateur));

        // // when
        // List<UserResponse> users = userController.findAll();

        // // then
        // assertEquals(1, users.size());
        // assertEquals("testuser", users.get(0).getUsername());
        // }

        @Test
        void shouldFindUserById() {
                // given
                when(utilisateurRepository.findById("1")).thenReturn(Optional.of(utilisateur));

                // when
                UserResponse response = userController.findById("1");

                // then
                assertNotNull(response);
                assertEquals("testuser", response.getUsername());
                verify(utilisateurRepository).findById("1");
        }

        @Test
        void shouldThrowUserNotFoundExceptionWhenUserDoesNotExist() {
                // given
                when(utilisateurRepository.findById("1")).thenReturn(Optional.empty());

                // when / then
                assertThrows(UserNotFoundException.class, () -> userController.findById("1"));
        }

        @Test
        void shouldCreateUser() {
                // given
                UserRequest userRequest = new UserRequest();
                userRequest.setName("New User");
                userRequest.setUsername("newuser");
                userRequest.setPassword("password");
                userRequest.setMail("newuser@example.com");
                userRequest.setPhone("0987654321");
                userRequest.setHasDrivingLicence(false);
                userRequest.setAdmin(false);
                userRequest.setCompanyId(null);

                // Mock the behavior for password encoding and saving
                when(passwordEncoder.encode("password")).thenReturn("encodedPassword");

                // when
                String result = userController.createUser(userRequest);

                // then
                assertEquals("User created successfully", result);
                verify(utilisateurRepository).save(any(Utilisateur.class));
        }

        @Test
        void shouldUpdateUser() {
                // given
                UserRequest userRequest = new UserRequest();
                userRequest.setName("Updated User");
                userRequest.setUsername("updateduser");
                userRequest.setPassword("newpassword");
                userRequest.setMail("updateduser@example.com");
                userRequest.setPhone("1234567890");
                userRequest.setHasDrivingLicence(true);
                userRequest.setAdmin(true);

                when(utilisateurRepository.findById("1")).thenReturn(Optional.of(utilisateur));

                // when
                String result = userController.update("1", userRequest);

                // then
                assertEquals("1", result);
                verify(utilisateurRepository).save(any(Utilisateur.class));
        }

        @Test
        void shouldDeleteUser() {

                // when
                userController.deleteById("1");

                // then
                verify(utilisateurRepository).deleteById("1");
        }
}
