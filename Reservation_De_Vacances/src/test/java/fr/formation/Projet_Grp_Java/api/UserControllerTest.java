package fr.formation.Projet_Grp_Java.api;

import fr.formation.Projet_Grp_Java.model.Utilisateur;
import fr.formation.Projet_Grp_Java.repo.UtilisateurRepository;
import fr.formation.Projet_Grp_Java.request.AuthRequest;
import fr.formation.Projet_Grp_Java.request.UserRequest;
import fr.formation.Projet_Grp_Java.security.TestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {
    private static final String ENDPOINT = "/api/user";
    private static final String ENDPOINT_AUTH = ENDPOINT + "/auth";

    private static final String USER_ID = "userId1";
    private static final String USER_NAME = "user1";
    private static final String USER_USERNAME = "userName1";
    private static final String USER_PASSWORD = "123456";
    private static final Boolean USER_HASDRIVINGLICENCE = false;
    private static final String USER_MAIL = "user1@gmail.com";
    private static final String USER_PHONE = "00.01.02.03.04";
    private static final Boolean USER_ADMIN = false;

    private static final String USER_ID2 = "userId2";
    private static final String USER_NAME2 = "user2";
    private static final String USER_USERNAME2 = "userName2";
    private static final String USER_PASSWORD2 = "123456";
    private static final Boolean USER_HASDRIVINGLICENCE2 = false;
    private static final String USER_MAIL2 = "user2@gmail.com";
    private static final String USER_PHONE2 = "00.01.02.03.04";
    private static final Boolean USER_ADMIN2 = false;

    @Mock
    private UtilisateurRepository repository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private UserController ctrl;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(this.ctrl).build();
    }

    @Test
    void shouldAuthStatusOk() throws Exception {
        // given
        AuthRequest request = AuthRequest.builder()
                .username(USER_USERNAME)
                .password(USER_PASSWORD)
                .build()
                ;

        Mockito.when(this.repository.findByUsername(USER_USERNAME)).thenReturn(Optional.of(new Utilisateur()));

        // when
        ResultActions result = this.mockMvc.perform(
                MockMvcRequestBuilders
                        .post(ENDPOINT_AUTH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.json(request))
        );

        // then
        result.andExpect(MockMvcResultMatchers.status().isOk());

        verify(this.repository).findByUsername(USER_USERNAME);
    }

    @Test
    void shouldAuthStatusOkEvenIfBadCredentialsThrown() throws Exception {
        // given
        AuthRequest request = AuthRequest.builder()
                .username(USER_USERNAME)
                .password(USER_PASSWORD)
                .build()
                ;

        Mockito.when(this.authenticationManager.authenticate(Mockito.any())).thenThrow(BadCredentialsException.class);

        // when
        ResultActions result = this.mockMvc.perform(
                MockMvcRequestBuilders
                        .post(ENDPOINT_AUTH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.json(request))
        );

        // then
        result.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @ParameterizedTest
    @MethodSource("provideBadAuthRequests")
    void shouldAuthStatusBadRequest(AuthRequest request) throws Exception {
        // given

        // when
        ResultActions result = this.mockMvc.perform(
                MockMvcRequestBuilders
                        .post(ENDPOINT_AUTH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.json(request))
        );

        // then
        result.andExpect(MockMvcResultMatchers.status().isBadRequest());

        verify(this.repository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void shouldReturnListOfUsers() throws Exception {
        // given
        Utilisateur user1 = new Utilisateur();
        user1.setId(USER_ID);
        user1.setName(USER_NAME);
        user1.setUsername(USER_USERNAME);
        user1.setPassword(USER_PASSWORD);
        user1.setHasDrivingLicence(USER_HASDRIVINGLICENCE);
        user1.setMail(USER_MAIL);
        user1.setPhone(USER_PHONE);
        user1.setAdmin(USER_ADMIN);

        Utilisateur user2 = new Utilisateur();
        user2.setId(USER_ID2);
        user2.setName(USER_NAME2);
        user2.setUsername(USER_USERNAME2);
        user2.setPassword(USER_PASSWORD2);
        user2.setHasDrivingLicence(USER_HASDRIVINGLICENCE2);
        user2.setMail(USER_MAIL2);
        user2.setPhone(USER_PHONE2);
        user2.setAdmin(USER_ADMIN2);

        List<Utilisateur> users = List.of(user1, user2);

        Mockito.when(repository.findAll()).thenReturn(users);

        // when
        ResultActions result = this.mockMvc.perform(
                MockMvcRequestBuilders
                        .get(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        result.andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].username").value("userName1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].username").value("userName2"));

        verify(repository).findAll();
    }

    @Test
    void shouldReturnUserWhenFound() throws Exception {
        // given
        Utilisateur user = new Utilisateur();
        user.setId(USER_ID);
        user.setName(USER_NAME);
        user.setUsername(USER_USERNAME);
        user.setPassword(USER_PASSWORD);
        user.setHasDrivingLicence(USER_HASDRIVINGLICENCE);
        user.setMail(USER_MAIL);
        user.setPhone(USER_PHONE);
        user.setAdmin(USER_ADMIN);

        Mockito.when(repository.findById(USER_ID)).thenReturn(Optional.of(user));

        // when
        ResultActions result = this.mockMvc.perform(
                MockMvcRequestBuilders
                        .get(ENDPOINT + "/" + USER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        result.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(USER_ID))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("user1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("userName1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.mail").value("user1@gmail.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.phone").value("00.01.02.03.04"));

        verify(repository).findById(USER_ID);
    }

    @Test
    void shouldReturnNotFoundWhenUserDoesNotExist() throws Exception {
        // given
        Mockito.when(repository.findById(USER_ID)).thenReturn(Optional.empty());

        // when
        ResultActions result = this.mockMvc.perform(
                MockMvcRequestBuilders
                        .get(ENDPOINT + "/" + USER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        result.andExpect(MockMvcResultMatchers.status().isNotFound());

        verify(repository).findById(USER_ID);
    }

    @Test
    void shouldSubscribeStatusCreated() throws Exception {
        // given
        UserRequest request = UserRequest.builder()
                .name(USER_NAME)
                .username(USER_USERNAME)
                .password(USER_PASSWORD)
                .hasDrivingLicence(USER_HASDRIVINGLICENCE)
                .mail(USER_MAIL)
                .phone(USER_PHONE)
                .build()
                ;

        // when
        ResultActions result = this.mockMvc.perform(
                MockMvcRequestBuilders
                        .post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.json(request))
        );

        // then
        result.andExpect(MockMvcResultMatchers.status().isCreated());

        verify(this.passwordEncoder).encode(USER_PASSWORD);
        verify(this.repository).save(Mockito.any());
    }

    @ParameterizedTest
    @MethodSource("provideBadSubscribeRequests")
    void shouldCreateStatusBadRequest(UserRequest request) throws Exception {
        // given

        // when
        ResultActions result = this.mockMvc.perform(
                MockMvcRequestBuilders
                        .post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.json(request))
        );

        // then
        result.andExpect(MockMvcResultMatchers.status().isBadRequest());

        verify(this.passwordEncoder, Mockito.never()).encode(Mockito.any());
        verify(this.repository, Mockito.never()).save(Mockito.any());
    }



    @Test
    void shouldUpdateUserSuccessfully() throws Exception {
        // given
        UserRequest request = UserRequest.builder()
                .name("Updated Name")
                .username("updatedUsername")
                .mail("updated@example.com")
                .hasDrivingLicence(true)
                .phone("00.01.02.03.04")
                .build();

        Utilisateur existingUser = new Utilisateur();
        existingUser.setId(USER_ID);
        existingUser.setName("John Doe");
        existingUser.setUsername("john.doe");
        existingUser.setMail("john.doe@example.com");
        existingUser.setHasDrivingLicence(false);

        Mockito.when(repository.findById(USER_ID)).thenReturn(Optional.of(existingUser));

        // when
        ResultActions result = mockMvc.perform(
                MockMvcRequestBuilders
                        .put(ENDPOINT + "/" + USER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.json(request))
        );

        // then
        result.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(USER_ID));

        ArgumentCaptor<Utilisateur> captor = ArgumentCaptor.forClass(Utilisateur.class);
        verify(repository).save(captor.capture());

        Utilisateur savedUser = captor.getValue();
        assertEquals(USER_ID, savedUser.getId());
        assertEquals("Updated Name", savedUser.getName());
        assertEquals("updatedUsername", savedUser.getUsername());
        assertEquals("updated@example.com", savedUser.getMail());
        assertTrue(savedUser.isHasDrivingLicence());
    }


    @Test
    void shouldReturnNotFoundWhenUserToUpdateDoesNotExist() throws Exception {
        // given
        String userId = "nonExistentId";
        UserRequest request = UserRequest.builder()
                .name("Updated Name")
                .username("updatedUsername")
                .password("newPassword")
                .hasDrivingLicence(false)
                .mail("updated@example.com")
                .phone("1234567890")
                .build();

        Mockito.when(repository.findById(userId)).thenReturn(Optional.empty());

        // when
        ResultActions result = this.mockMvc.perform(
                MockMvcRequestBuilders
                        .put(ENDPOINT + "/" + userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.json(request))
        );

        // then
        result.andExpect(MockMvcResultMatchers.status().isNotFound());

        verify(repository).findById(userId);
        verify(repository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void shouldDeleteUserSuccessfully() throws Exception {
        // when
        mockMvc.perform(MockMvcRequestBuilders
                        .delete(ENDPOINT + "/" + USER_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());

        // then
        verify(repository).deleteById(USER_ID);
    }


    public static Stream<Arguments> provideBadAuthRequests() {
        return Stream.of(
                Arguments.of(
                        AuthRequest.builder().build()
                ),

                Arguments.of(
                        AuthRequest.builder()
                                .username(" ")
                                .password(USER_PASSWORD)
                                .build()
                ),

                Arguments.of(
                        AuthRequest.builder()
                                .username(USER_USERNAME)
                                .password("")
                                .build()
                ),

                Arguments.of(
                        AuthRequest.builder()
                                .username(USER_USERNAME)
                                .build()
                )
        );
    }

    public static Stream<Arguments> provideBadSubscribeRequests() {
        return Stream.of(
                Arguments.of(
                        UserRequest.builder().build()
                ),

                Arguments.of(
                        UserRequest.builder()
                                .name(USER_NAME)
                                .username(" ")
                                .password(USER_PASSWORD)
                                .hasDrivingLicence(true)
                                .mail("nomUtilisateur@gmail.com")
                                .phone("00.01.02.03.04")
                                .build()
                ),

                Arguments.of(
                        UserRequest.builder()
                                .name(USER_NAME)
                                .username(USER_USERNAME)
                                .password("")
                                .hasDrivingLicence(true)
                                .mail("nomUtilisateur@gmail.com")
                                .phone("00.01.02.03.04")
                                .build()
                ),

                Arguments.of(
                        UserRequest.builder()
                                .name("")
                                .username(USER_USERNAME)
                                .password(USER_PASSWORD)
                                .hasDrivingLicence(true)
                                .mail("nomUtilisateur@gmail.com")
                                .phone("00.01.02.03.04")
                                .build()
                ),
                Arguments.of(
                        UserRequest.builder()
                                .name(USER_NAME)
                                .username(USER_USERNAME)
                                .password(USER_PASSWORD)
                                .hasDrivingLicence(true)
                                .mail("")
                                .phone("00.01.02.03.04")
                                .build()
                ),
                Arguments.of(
                        UserRequest.builder()
                                .name(USER_NAME)
                                .username(USER_USERNAME)
                                .password(USER_PASSWORD)
                                .hasDrivingLicence(true)
                                .mail("nomUtilisateur@gmail.com")
                                .phone("")
                                .build()
                )
        );
    }
}
