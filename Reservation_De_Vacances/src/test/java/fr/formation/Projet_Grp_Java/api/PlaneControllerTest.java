package fr.formation.Projet_Grp_Java.api;

import fr.formation.Projet_Grp_Java.exception.CompanyNotFoundException;
import fr.formation.Projet_Grp_Java.exception.PlaneNotFoundException;
import fr.formation.Projet_Grp_Java.model.Company;
import fr.formation.Projet_Grp_Java.model.Plane;
import fr.formation.Projet_Grp_Java.repo.CompanyRepository;
import fr.formation.Projet_Grp_Java.repo.PlaneRepository;
import fr.formation.Projet_Grp_Java.request.CreateOrUpdatePlaneRequest;
import fr.formation.Projet_Grp_Java.response.PlaneByIdResponse;
import fr.formation.Projet_Grp_Java.response.PlaneResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlaneControllerTest {

        @InjectMocks
        private PlaneApiController planeApiController;

        @Mock
        private PlaneRepository planeRepository;

        @Mock
        private CompanyRepository companyRepository;

        private Plane plane;
        private CreateOrUpdatePlaneRequest createOrUpdatePlaneRequest;
        private Company company;

        @BeforeEach
        void setUp() {
                company = new Company();
                company.setId("company1");

                plane = new Plane();
                plane.setId("1");
                plane.setDepartureAirport("JFK");
                plane.setArrivalAirport("LAX");
                plane.setPlanePrice(500.0);
                plane.setNumberOfSeats(150);
                plane.setCompany(company);

                createOrUpdatePlaneRequest = new CreateOrUpdatePlaneRequest();
                createOrUpdatePlaneRequest.setDepartureAirport("JFK");
                createOrUpdatePlaneRequest.setArrivalAirport("LAX");
                createOrUpdatePlaneRequest.setPlanePrice(500.0);
                createOrUpdatePlaneRequest.setNumberOfSeats(150);
                createOrUpdatePlaneRequest.setCompanyId("company1");
        }

        @Test
        void shouldFindAllPlanes() {
                when(planeRepository.findAll()).thenReturn(Collections.singletonList(plane));
                List<PlaneResponse> response = planeApiController.findAll();
                assertEquals(1, response.size());
                assertEquals("JFK", response.get(0).getDepartureAirport());
                verify(planeRepository).findAll();
        }

        @Test
        void shouldFindPlaneById() {
                when(planeRepository.findById("1")).thenReturn(Optional.of(plane));
                PlaneByIdResponse response = planeApiController.findById("1");
                assertNotNull(response);
                assertEquals("JFK", response.getDepartureAirport());
                verify(planeRepository).findById("1");
        }

        @Test
        void shouldThrowPlaneNotFoundExceptionWhenPlaneDoesNotExist() {
                when(planeRepository.findById("1")).thenReturn(Optional.empty());
                assertThrows(PlaneNotFoundException.class, () -> planeApiController.findById("1"));
        }

        @Test
        void shouldThrowCompanyNotFoundExceptionWhenCreatingPlaneWithInvalidCompany() {
                when(companyRepository.findById("company1")).thenReturn(Optional.empty());
                assertThrows(CompanyNotFoundException.class,
                                () -> planeApiController.create(createOrUpdatePlaneRequest));
        }

        @Test
        void shouldUpdatePlane() {
                when(planeRepository.findById("1")).thenReturn(Optional.of(plane));
                when(companyRepository.findById("company1")).thenReturn(Optional.of(company));
                String updatedPlaneId = planeApiController.update("1", createOrUpdatePlaneRequest);
                assertEquals("1", updatedPlaneId);
                verify(planeRepository).save(any(Plane.class));
        }

        @Test
        void shouldThrowPlaneNotFoundExceptionWhenUpdatingPlane() {
                when(planeRepository.findById("1")).thenReturn(Optional.empty());
                assertThrows(PlaneNotFoundException.class,
                                () -> planeApiController.update("1", createOrUpdatePlaneRequest));
        }

        @Test
        void shouldDeletePlane() {
                planeApiController.deleteById("1");
                verify(planeRepository).deleteById("1");
        }

        @Test
        void shouldNotThrowErrorWhenDeletingNonExistentPlane() {
                doNothing().when(planeRepository).deleteById("1");
                planeApiController.deleteById("1");
                verify(planeRepository).deleteById("1");
        }
}
