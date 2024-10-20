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

import java.util.Optional;
import java.util.stream.Stream;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {
    private static final String ENDPOINT = "/api/user";
    private static final String ENDPOINT_AUTH = ENDPOINT + "/auth";

    private static final String USER_NAME = "The God";
    private static final String USER_USERNAME = "god";
    private static final String USER_PASSWORD = "123456$";

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

        Mockito.verify(this.repository).findByUsername(USER_USERNAME);
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

        Mockito.verify(this.repository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void shouldSubscribeStatusCreated() throws Exception {
        // given
        UserRequest request = UserRequest.builder()
                .name("Nom d'utilisateur")
                .username("nomutilisateur")
                .password("123456$")
                .hasDrivingLicence(true)
                .mail("nomUtilisateur@gmail.com")
                .telephone("00.01.02.03.04")
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

        Mockito.verify(this.passwordEncoder).encode(USER_PASSWORD);
        Mockito.verify(this.repository).save(Mockito.any());
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

        Mockito.verify(this.passwordEncoder, Mockito.never()).encode(Mockito.any());
        Mockito.verify(this.repository, Mockito.never()).save(Mockito.any());
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
                                .telephone("00.01.02.03.04")
                                .build()
                ),

                Arguments.of(
                        UserRequest.builder()
                                .name(USER_NAME)
                                .username(USER_USERNAME)
                                .password("")
                                .hasDrivingLicence(true)
                                .mail("nomUtilisateur@gmail.com")
                                .telephone("00.01.02.03.04")
                                .build()
                ),

                Arguments.of(
                        UserRequest.builder()
                                .username(USER_USERNAME)
                                .password(USER_PASSWORD)
                                .hasDrivingLicence(true)
                                .mail("nomUtilisateur@gmail.com")
                                .telephone("00.01.02.03.04")
                                .build()
                )
        );
    }
}
