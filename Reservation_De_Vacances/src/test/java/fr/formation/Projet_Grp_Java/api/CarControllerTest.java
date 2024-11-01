package fr.formation.Projet_Grp_Java.api;

import fr.formation.Projet_Grp_Java.exception.CarNotFoundException;
import fr.formation.Projet_Grp_Java.exception.CarStatusNotFoundException;
import fr.formation.Projet_Grp_Java.exception.CompanyNotFoundException;
import fr.formation.Projet_Grp_Java.model.Car;
import fr.formation.Projet_Grp_Java.model.CarStatus;
import fr.formation.Projet_Grp_Java.model.Company;
import fr.formation.Projet_Grp_Java.repo.CarRepository;
import fr.formation.Projet_Grp_Java.repo.CarStatusRepository;
import fr.formation.Projet_Grp_Java.repo.CompanyRepository;
import fr.formation.Projet_Grp_Java.request.CreateOrUpdateCarRequest;
import fr.formation.Projet_Grp_Java.response.CarByIdResponse;
import fr.formation.Projet_Grp_Java.response.CarResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.BeanUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CarControllerTest {

        @InjectMocks
        private CarApiController carApiController;

        @Mock
        private CarRepository carRepository;

        @Mock
        private CarStatusRepository carStatusRepository;

        @Mock
        private CompanyRepository companyRepository;

        private Car car;
        private CreateOrUpdateCarRequest createOrUpdateCarRequest;
        private CarStatus carStatus;
        private Company company;

        @BeforeEach
        void setUp() {
                car = new Car();
                car.setId("1");
                car.setCarBrand("Toyota");
                car.setCarModel("Corolla");
                car.setCarDailyPrice(50.0);

                carStatus = new CarStatus();
                carStatus.setId("1");
                carStatus.setCarStatusName("Disponible");

                company = new Company();
                company.setId("company1");

                createOrUpdateCarRequest = new CreateOrUpdateCarRequest();
                createOrUpdateCarRequest.setCarBrand("Toyota");
                createOrUpdateCarRequest.setCarModel("Corolla");
                createOrUpdateCarRequest.setCarDailyPrice(50.0);
                createOrUpdateCarRequest.setCarStatusId("1");
                createOrUpdateCarRequest.setCompanyId("company1");
        }

        @Test
        void shouldFindAllCars() {
                when(carRepository.findAll()).thenReturn(Collections.singletonList(car));

                List<CarResponse> response = carApiController.findAll();

                assertEquals(1, response.size());
                assertEquals("Toyota", response.get(0).getCarBrand());
                verify(carRepository).findAll();
        }

        @Test
        void shouldFindCarById() {
                when(carRepository.findById("1")).thenReturn(Optional.of(car));

                CarByIdResponse response = carApiController.findById("1");

                assertNotNull(response);
                assertEquals("Toyota", response.getCarBrand());
                verify(carRepository).findById("1");
        }

        @Test
        void shouldThrowCarNotFoundExceptionWhenCarDoesNotExist() {
                when(carRepository.findById("1")).thenReturn(Optional.empty());

                assertThrows(CarNotFoundException.class, () -> carApiController.findById("1"));
        }

        @Test
        void shouldCreateCar() {
                when(carStatusRepository.findById("1")).thenReturn(Optional.of(carStatus));
                when(companyRepository.findById("company1")).thenReturn(Optional.of(company));
                when(carRepository.save(any(Car.class))).thenReturn(car);

                String carId = carApiController.create(createOrUpdateCarRequest);

                verify(carStatusRepository).findById("1");
                verify(companyRepository).findById("company1");
                verify(carRepository).save(any(Car.class));
        }

        @Test
        void shouldThrowCarStatusNotFoundExceptionWhenCarStatusDoesNotExist() {
                when(carStatusRepository.findById("1")).thenReturn(Optional.empty());

                assertThrows(CarStatusNotFoundException.class, () -> carApiController.create(createOrUpdateCarRequest));
        }

        @Test
        void shouldThrowCompanyNotFoundExceptionWhenCompanyDoesNotExist() {
                when(carStatusRepository.findById("1")).thenReturn(Optional.of(carStatus));
                when(companyRepository.findById("company1")).thenReturn(Optional.empty());

                assertThrows(CompanyNotFoundException.class, () -> carApiController.create(createOrUpdateCarRequest));
        }

        @Test
        void shouldUpdateCar() {
                when(carRepository.findById("1")).thenReturn(Optional.of(car));
                when(carStatusRepository.findById("1")).thenReturn(Optional.of(carStatus));
                when(companyRepository.findById("company1")).thenReturn(Optional.of(company));
                when(carRepository.save(any(Car.class))).thenReturn(car);

                String updatedCarId = carApiController.update("1", createOrUpdateCarRequest);

                assertEquals("1", updatedCarId);
                verify(carRepository).findById("1");
                verify(carStatusRepository).findById("1");
                verify(companyRepository).findById("company1");
                verify(carRepository).save(any(Car.class));
        }

        @Test
        void shouldThrowCarNotFoundExceptionWhenUpdatingNonexistentCar() {
                when(carRepository.findById("1")).thenReturn(Optional.empty());

                assertThrows(CarNotFoundException.class, () -> carApiController.update("1", createOrUpdateCarRequest));
        }

        @Test
        void shouldDeleteCar() {

                carApiController.deleteById("1");

                verify(carRepository).deleteById("1");
        }

        @Test
        void shouldHandleDeleteException() {
                doThrow(new RuntimeException("Can't delete car!")).when(carRepository).deleteById("1");

                carApiController.deleteById("1");

                verify(carRepository).deleteById("1");
        }
}
