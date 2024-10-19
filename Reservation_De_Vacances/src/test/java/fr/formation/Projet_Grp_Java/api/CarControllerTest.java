package fr.formation.Projet_Grp_Java.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.formation.Projet_Grp_Java.model.Car;
import fr.formation.Projet_Grp_Java.model.CarStatus;
import fr.formation.Projet_Grp_Java.repo.CarRepository;
import fr.formation.Projet_Grp_Java.repo.CarStatusRepository;
import fr.formation.Projet_Grp_Java.request.CreateOrUpdateCarRequest;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class CarControllerTest {
    private static final String CAR_ID = "car-id-863";
    private static final String CAR_STATUS_ID = "1";
    //private static final String USER_ID = "user-id-117";

    private static final String ENDPOINT = "/api/car";
    private static final String ENDPOINT_BY_ID = ENDPOINT + "/" + CAR_ID;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private CarRepository repository;

    @Mock
    private CarStatusRepository carStatusRepository;

    //private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @InjectMocks
    private CarApiController ctrl;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(this.ctrl).build();
    }

    @Test
    void shouldFindAllStatusOk() throws Exception {
        //given

        //when
        ResultActions result = this.mockMvc.perform(MockMvcRequestBuilders.get(ENDPOINT));

        //then
        result.andExpect(status().isOk());

        Mockito.verify(this.repository).findAll();
    }

    @Test
    void shouldFindByIdStatusOk() throws Exception {
        //given
        Mockito.when(this.repository.findById(CAR_ID)).thenReturn(Optional.of(new Car()));

        //when
        ResultActions result = this.mockMvc.perform(MockMvcRequestBuilders.get(ENDPOINT_BY_ID));

        //then
        result.andExpect(status().isOk());

        Mockito.verify(this.repository).findById(CAR_ID);
    }

    @Test
    void shouldCreateStatusCreated() throws Exception {
        //Given
        CreateOrUpdateCarRequest request = CreateOrUpdateCarRequest.builder()
                .carBrand("TestBrand")
                .carModel("TestModel")
                .carDailyPrice(100.0)
                .carStatusId(CAR_STATUS_ID)
                .build();

        CarStatus carStatus = new CarStatus();
        carStatus.setId(CAR_STATUS_ID);

        Car car = new Car();
        car.setId(CAR_ID);
        car.setCarBrand("TestBrand");
        car.setCarModel("TestModel");
        car.setCarDailyPrice(100.0);
        car.setCarStatus(carStatus);

        Mockito.when(carStatusRepository.findById(CAR_STATUS_ID)).thenReturn(Optional.of(carStatus));

        Mockito.when(repository.save(Mockito.any(Car.class))).thenReturn(car);

        //When
        mockMvc.perform(
                        post(ENDPOINT)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                //Then
                .andExpect(status().isCreated())
                .andExpect(content().string(CAR_ID));

        Mockito.verify(carStatusRepository).findById(CAR_STATUS_ID);
        Mockito.verify(repository).save(Mockito.any(Car.class));
    }

    @Test
    void shouldUpdateStatusOk() throws Exception {
        //given
        Mockito.when(this.repository.findById(CAR_ID)).thenReturn(Optional.of(new Car()));

        CreateOrUpdateCarRequest request = CreateOrUpdateCarRequest.builder()
                .carModel("newCarModel")
                .build();

        //when
        ResultActions result = this.mockMvc.perform(
                MockMvcRequestBuilders
                        .put(ENDPOINT_BY_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        //.content(TestUtil.json(request))
                        //.principal(authentication)
        )
                ;

        //then
        result.andExpect(MockMvcResultMatchers.status().isOk());

        //Mockito.verify(this.userRepository, Mockito.never()).findById(USER_ID);
        Mockito.verify(this.repository).findById(CAR_ID);
        Mockito.verify(this.repository).save(Mockito.any());
    }

    @ParameterizedTest
    @MethodSource("provideBadCreateOrUpdateRequests")
    void shouldUpdateStatusBadRequest(CreateOrUpdateCarRequest request) throws Exception {
        //given

        //when
        ResultActions result = this.mockMvc.perform(
                MockMvcRequestBuilders
                        .put(ENDPOINT_BY_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        //.content(TestUtil.json(request))
                        //.principal(authentication)
        )
                ;

        //then
        result.andExpect(MockMvcResultMatchers.status().isBadRequest());

        //Mockito.verify(this.userRepository, Mockito.never()).findById(Mockito.any());
        Mockito.verify(this.repository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void shouldDeleteByIdStatusOk() throws Exception {
        //given

        //when
        ResultActions result = this.mockMvc.perform(MockMvcRequestBuilders.delete(ENDPOINT_BY_ID));

        //then
        result.andExpect(MockMvcResultMatchers.status().isOk());

        Mockito.verify(this.repository).deleteById(CAR_ID);
    }

    public static Stream<Arguments> provideBadCreateOrUpdateRequests() {
        return Stream.of(
                Arguments.of(
                        CreateOrUpdateCarRequest.builder().build()
                ),

                Arguments.of(
                        CreateOrUpdateCarRequest.builder().carBrand(" ").build()
                ),

                Arguments.of(
                        CreateOrUpdateCarRequest.builder().carBrand("").build()
                )
        );
    }
}
