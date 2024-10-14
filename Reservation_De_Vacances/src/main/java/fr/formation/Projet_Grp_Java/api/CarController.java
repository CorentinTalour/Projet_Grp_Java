package fr.formation.Projet_Grp_Java.api;

import fr.formation.Projet_Grp_Java.exception.CarNotFoundException;
import fr.formation.Projet_Grp_Java.model.Car;
import fr.formation.Projet_Grp_Java.repo.CarRepository;
import fr.formation.Projet_Grp_Java.request.CreateCarRequest;
import fr.formation.Projet_Grp_Java.response.CarByIdResponse;
import fr.formation.Projet_Grp_Java.response.CarResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;


import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/car")
@RequiredArgsConstructor
@Log4j2
public class CarController {
    private final CarRepository repository;

    @GetMapping
    public List<CarResponse> findAll() {
        log.debug("Finding all car ...");

        return this.repository.findAll()
                .stream()
                .map(this::convert)
                .toList();
    }

    @GetMapping("/{id}")
        public CarByIdResponse findById(@PathVariable String id) {
        log.debug("Finding car {} ...", id);

        Car car = this.repository.findById(id).orElseThrow(CarNotFoundException::new);
        CarByIdResponse resp = CarByIdResponse.builder().build();

        BeanUtils.copyProperties(car, resp);

        return resp;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String create(@Valid @RequestBody CreateCarRequest request) {
        log.debug("Creating video ...");

        Car car = new Car();

        BeanUtils.copyProperties(request, car);

        this.repository.save(car);

        log.debug("Video {} created!", car.getId());

        return car.getId();
    }

    private CarResponse convert(Car car) {
        CarResponse resp = CarResponse.builder().build();

        BeanUtils.copyProperties(car, resp);

        return resp;
    }
}