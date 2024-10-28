package fr.formation.Projet_Grp_Java.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.formation.Projet_Grp_Java.model.Plane;
import fr.formation.Projet_Grp_Java.repo.PlaneRepository;
import fr.formation.Projet_Grp_Java.request.CreateOrUpdatePlaneRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class PlaneControllerTest {
        private static final String PLANE_ID1 = "plane-id-863";
        private static final String PLANE_DEPARTUREAIRPORT1 = "Londres";
        private static final String PLANE_ARRIVALAIRPORT1 = "Berlin";
        private static final Double PLANE_PLANEPRICE1 = 500.50;
        private static final Integer PLANE_NUMBEROFSEATS1 = 300;

        private static final String ENDPOINT = "/api/plane";
        private static final String ENDPOINT_BY_ID = ENDPOINT + "/" + PLANE_ID1;

        private final ObjectMapper objectMapper = new ObjectMapper();

        @Mock
        private PlaneRepository repository;

        private MockMvc mockMvc;

        @InjectMocks
        private PlaneApiController ctrl;

        @BeforeEach
        public void setup() {
                this.mockMvc = MockMvcBuilders.standaloneSetup(this.ctrl).build();
        }

        @Test
        public void testFindAll() throws Exception {
                // when
                ResultActions result = this.mockMvc.perform(MockMvcRequestBuilders.get(ENDPOINT));

                // then
                result.andExpect(status().isOk());

                verify(this.repository).findAll();
        }

        @Test
        public void testFindById_PlaneExists() throws Exception {
                Plane plane = new Plane();
                plane.setId(PLANE_ID1);
                plane.setDepartureAirport(PLANE_DEPARTUREAIRPORT1);
                plane.setArrivalAirport(PLANE_ARRIVALAIRPORT1);
                plane.setPlanePrice(PLANE_PLANEPRICE1);
                plane.setNumberOfSeats(PLANE_NUMBEROFSEATS1);

                when(repository.findById(PLANE_ID1)).thenReturn(Optional.of(plane));

                // Exécution de la requête GET
                ResultActions result = this.mockMvc.perform(MockMvcRequestBuilders.get(ENDPOINT + "/" + PLANE_ID1)
                                .contentType(MediaType.APPLICATION_JSON));

                result.andExpect(status().isOk())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(content().json("{" +
                                                "\"departureAirport\":\"" + PLANE_DEPARTUREAIRPORT1 + "\"," +
                                                "\"arrivalAirport\":\"" + PLANE_ARRIVALAIRPORT1 + "\"," +
                                                "\"planePrice\":" + PLANE_PLANEPRICE1 + "," +
                                                "\"numberOfSeats\":" + PLANE_NUMBEROFSEATS1 +
                                                "}"));
        }

        @Test
        public void testFindById_PlaneNotFound() throws Exception {
                when(repository.findById(PLANE_ID1)).thenReturn(Optional.empty());

                ResultActions result = this.mockMvc.perform(MockMvcRequestBuilders.get(ENDPOINT + "/" + PLANE_ID1)
                                .contentType(MediaType.APPLICATION_JSON));

                result.andExpect(status().isNotFound());
        }

        @Test
        void shouldCreatePlaneWithStatusCreated() throws Exception {
                // Given
                CreateOrUpdatePlaneRequest request = CreateOrUpdatePlaneRequest.builder()
                                .departureAirport(PLANE_DEPARTUREAIRPORT1)
                                .arrivalAirport(PLANE_ARRIVALAIRPORT1)
                                .planePrice(550.50)
                                .numberOfSeats(200)
                                .build();

                when(repository.save(any(Plane.class))).thenAnswer(invocation -> {
                        Plane savedPlane = invocation.getArgument(0);
                        savedPlane.setId(PLANE_ID1);
                        return savedPlane;
                });

                // When
                mockMvc.perform(
                                MockMvcRequestBuilders.post(ENDPOINT)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(objectMapper.writeValueAsString(request)))
                                // Then
                                .andExpect(status().isCreated())
                                .andExpect(content().string(PLANE_ID1));

                verify(repository).save(any(Plane.class));
        }

        @Test
        void shouldUpdateStatusOk() throws Exception {
                // Given
                Plane existingPlane = new Plane();
                existingPlane.setId(PLANE_ID1);
                existingPlane.setArrivalAirport("TestDépart");
                existingPlane.setDepartureAirport("TestArrivé");
                existingPlane.setPlanePrice(550.00);
                existingPlane.setNumberOfSeats(200);

                Mockito.when(repository.findById(PLANE_ID1)).thenReturn(Optional.of(existingPlane));

                CreateOrUpdatePlaneRequest request = CreateOrUpdatePlaneRequest.builder()
                                .departureAirport("UpdatedDeparture")
                                .arrivalAirport("UpdatedArrival")
                                .planePrice(999.99)
                                .numberOfSeats(300)
                                .build();

                // When
                ResultActions result = mockMvc.perform(
                                MockMvcRequestBuilders
                                                .put(ENDPOINT_BY_ID)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(objectMapper.writeValueAsString(request)));

                // Then
                result.andExpect(status().isOk())
                                .andExpect(content().string(PLANE_ID1));

                Mockito.verify(repository).findById(PLANE_ID1);
                Mockito.verify(repository).save(any(Plane.class));
        }

        @Test
        void shouldDeleteByIdStatusOk() throws Exception {
                // Given
                doNothing().when(repository).deleteById(PLANE_ID1);

                // When
                ResultActions result = mockMvc.perform(MockMvcRequestBuilders
                                .delete(ENDPOINT_BY_ID)
                                .contentType(MediaType.APPLICATION_JSON));

                // Then
                result.andExpect(status().isOk());

                verify(repository).deleteById(PLANE_ID1);
        }

        @Test
        void shouldHandleExceptionDuringDelete() throws Exception {
                // Given
                doThrow(new RuntimeException("Database error")).when(repository).deleteById(PLANE_ID1);

                // When
                ResultActions result = mockMvc.perform(MockMvcRequestBuilders
                                .delete(ENDPOINT_BY_ID)
                                .contentType(MediaType.APPLICATION_JSON));

                // Then
                result.andExpect(status().isOk());

                verify(repository).deleteById(PLANE_ID1);
        }
}
