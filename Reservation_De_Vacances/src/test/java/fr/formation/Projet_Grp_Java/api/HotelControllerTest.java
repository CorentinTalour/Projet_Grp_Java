package fr.formation.Projet_Grp_Java.api;

import fr.formation.Projet_Grp_Java.model.Hotel;
import fr.formation.Projet_Grp_Java.repo.HotelRepository;

import fr.formation.Projet_Grp_Java.request.HotelRequest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import org.springframework.http.MediaType;

import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@Sql(scripts = "classpath:/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class HotelControllerTest {
    private static final String HOTEL_ID = "hotel-id-865";
    // private static final String USER_ID = "user-id-117";

    private static final String ENDPOINT = "/api/hotel";
    private static final String ENDPOINT_BY_ID = ENDPOINT + "/" + HOTEL_ID;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private HotelRepository repository;

    private MockMvc mockMvc;

    @InjectMocks
    private HotelController ctrl;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(this.ctrl).build();
    }

    @Test
    void shouldFindAllStatusOk() throws Exception {
        // given

        // when
        ResultActions result = this.mockMvc.perform(MockMvcRequestBuilders.get(ENDPOINT));

        // then
        result.andExpect(status().isOk());

        Mockito.verify(this.repository).findAll();
    }

    @Test
    void shouldFindByIdStatusOk() throws Exception {
        // given
        Mockito.when(this.repository.findById(HOTEL_ID)).thenReturn(Optional.of(new Hotel()));

        // when
        ResultActions result = this.mockMvc.perform(MockMvcRequestBuilders.get(ENDPOINT_BY_ID));

        // then
        result.andExpect(status().isOk());

        Mockito.verify(this.repository).findById(HOTEL_ID);
    }

    @Test
    void shouldUpdateStatusOk() throws Exception {
        // given
        Mockito.when(this.repository.findById(HOTEL_ID)).thenReturn(Optional.of(new Hotel()));

        HotelRequest request = HotelRequest.builder()
                .nom("nom modifié")
                .build();

        // when
        ResultActions result = this.mockMvc.perform(
                MockMvcRequestBuilders
                        .put(ENDPOINT_BY_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)));

        // then
        result.andExpect(MockMvcResultMatchers.status().isOk());

        // Mockito.verify(this.userRepository, Mockito.never()).findById(USER_ID);
        Mockito.verify(this.repository).findById(HOTEL_ID);
        Mockito.verify(this.repository).save(Mockito.any());
    }

    @Test
    void shouldUpdateCarStatusOk() throws Exception {
        // Given
        HotelRequest request = HotelRequest.builder()
                .nom("hotel_test")
                .mail("florent_test@live.fr")
                .telephone("06 44 55 77 88")
                .ville("Laval")
                .adresse("bonne_adresse")
                .companyId(ENDPOINT)
                .build();

        Hotel existingHotel = new Hotel();
        existingHotel.setId(HOTEL_ID);
        existingHotel.setNom("test_nom");
        existingHotel.setMail("test_nom");
        existingHotel.setTelephone("test_nom");
        existingHotel.setVille("test_nom");
        existingHotel.setAdresse("test_nom");

        // Mock repository responses
        Mockito.when(this.repository.findById(HOTEL_ID)).thenReturn(Optional.of(existingHotel));
        Mockito.when(this.repository.save(Mockito.any(Hotel.class))).thenReturn(existingHotel);

        // When
        ResultActions result = mockMvc.perform(
                MockMvcRequestBuilders
                        .put(ENDPOINT_BY_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)));

        // Then
        result.andExpect(status().isOk());
        result.andExpect(content().string("Hotel modified successfully"));

        Mockito.verify(this.repository).findById(HOTEL_ID);

    }

    @Test
    void shouldReturnNotFoundWhenHotelDoesNotExist() throws Exception {
        // given
        Mockito.when(repository.findById(HOTEL_ID)).thenReturn(Optional.empty());

        // when
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get(ENDPOINT_BY_ID));

        // then
        result.andExpect(status().isNotFound());
        verify(repository).findById(HOTEL_ID);
    }

    @Test
    void shouldUpdateHotelStatusNotFoundIfHotelDoesNotExist() throws Exception {
        // given
        HotelRequest request = HotelRequest.builder()
                .nom("New Name")
                .build();

        Mockito.when(repository.findById(HOTEL_ID)).thenReturn(Optional.empty());

        // when
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.put(ENDPOINT_BY_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // then
        result.andExpect(status().isNotFound());
        verify(repository).findById(HOTEL_ID);
    }

    @Test
    void shouldDeleteHotelStatusNoContent() throws Exception {

        // when
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.delete(ENDPOINT_BY_ID));

        // then
        result.andExpect(status().isOk()); // On attend un statut 204
        verify(repository).deleteById(HOTEL_ID); // Vérifie que deleteById est appelé
    }

}
