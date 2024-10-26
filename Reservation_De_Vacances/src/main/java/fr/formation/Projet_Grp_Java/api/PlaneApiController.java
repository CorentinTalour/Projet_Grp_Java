package fr.formation.Projet_Grp_Java.api;

import fr.formation.Projet_Grp_Java.exception.PlaneNotFoundException;
import fr.formation.Projet_Grp_Java.model.Plane;
import fr.formation.Projet_Grp_Java.repo.PlaneRepository;
import fr.formation.Projet_Grp_Java.request.CreateOrUpdatePlaneRequest;
import fr.formation.Projet_Grp_Java.response.PlaneByIdResponse;
import fr.formation.Projet_Grp_Java.response.PlaneResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/plane")
@RequiredArgsConstructor
@Log4j2
public class PlaneApiController {


    private final PlaneRepository repository;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<PlaneResponse> findAll() {
        log.debug("Finding all plane ...");

        return this.repository.findAll()
                .stream()
                .map(this::convert)
                .toList();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PlaneByIdResponse findById(@PathVariable String id) {
        log.debug("Finding plane {} ...", id);

        Plane plane = this.repository.findById(id).orElseThrow(PlaneNotFoundException::new);
        PlaneByIdResponse resp = PlaneByIdResponse.builder().build();

        BeanUtils.copyProperties(plane, resp);

        return resp;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String create(@Valid @RequestBody CreateOrUpdatePlaneRequest request) {
        log.debug("Creating plane ...");

        Plane plane = new Plane();
        BeanUtils.copyProperties(request, plane);

        this.repository.save(plane);

        log.debug("Plane {} created!", plane.getId());

        return plane.getId();
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    //@PreAuthorize("isAuthenticated()")
    public String update(@PathVariable String id, @Valid @RequestBody CreateOrUpdatePlaneRequest request) {
        log.debug("Updating Plane {} ...", id);

        Plane plane = this.repository.findById(id).orElseThrow(PlaneNotFoundException::new);

        BeanUtils.copyProperties(request, plane);

        this.repository.save(plane);

        log.debug("Plane {} updated!", id);

        return plane.getId();
    }

    @DeleteMapping("/{id}")
    //@PreAuthorize("isAuthenticated()")
    public void deleteById(@PathVariable String id) {
        log.debug("Deleting plane {} ...", id);

        try {
            this.repository.deleteById(id);
        }

        catch (Exception ex) {
            log.error("Can't delete plane {}! Comments may exist!", id);
        }

        log.debug("plane {} deleted!", id);
    }

    private PlaneResponse convert(Plane plane) {
        PlaneResponse resp = PlaneResponse.builder().build();

        BeanUtils.copyProperties(plane, resp);

        return resp;
    }
}