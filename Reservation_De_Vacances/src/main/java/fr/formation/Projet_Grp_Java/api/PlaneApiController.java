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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/plane")
@RequiredArgsConstructor
@Log4j2
public class PlaneApiController {


    private final PlaneRepository repository;
    private final CompanyRepository companyRepository;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Récupérer tous les avions",
            description = "Retourne une liste de tous les avions dans la base de données.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Récupération réussie des avions",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PlaneResponse.class))),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public List<PlaneResponse> findAll() {
        log.debug("Finding all plane ...");

        return this.repository.findAll()
                .stream()
                .map(this::convert)
                .toList();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Récupérer un avion par ID",
            description = "Retourne un avion identifié par son ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Récupération réussie de l'avion",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PlaneByIdResponse.class))),
            @ApiResponse(responseCode = "404", description = "Avion non trouvé",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public PlaneByIdResponse findById(@PathVariable String id) {
        log.debug("Finding plane {} ...", id);

        Plane plane = this.repository.findById(id).orElseThrow(PlaneNotFoundException::new);
        PlaneByIdResponse resp = PlaneByIdResponse.builder().build();

        BeanUtils.copyProperties(plane, resp);

        return resp;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Créer un nouvel avion",
            description = "Crée un nouvel avion dans la base de données.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Avion créé avec succès",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Données d'entrée invalides",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public String create(@Valid @RequestBody CreateOrUpdatePlaneRequest request) {
        log.debug("Creating plane ...");

        Plane plane = new Plane();
        BeanUtils.copyProperties(request, plane);

        Company companyId = companyRepository.findById(request.getCompanyId())
                .orElseThrow(CompanyNotFoundException::new);
        plane.setCompany(companyId);

        this.repository.save(plane);

        log.debug("Plane {} created!", plane.getId());

        return plane.getId();
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Mettre à jour un avion existant",
            description = "Met à jour un avion identifié par son ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Avion mis à jour avec succès",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Avion non trouvé",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Données d'entrée invalides",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public String update(@PathVariable String id, @Valid @RequestBody CreateOrUpdatePlaneRequest request) {
        log.debug("Updating Plane {} ...", id);

        Plane plane = this.repository.findById(id).orElseThrow(PlaneNotFoundException::new);

        BeanUtils.copyProperties(request, plane);

        Company companyId = companyRepository.findById(request.getCompanyId())
                .orElseThrow(CompanyNotFoundException::new);
        plane.setCompany(companyId);

        this.repository.save(plane);

        log.debug("Plane {} updated!", id);

        return plane.getId();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Supprimer un avion par ID",
            description = "Supprime un avion identifié par son ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Avion supprimé avec succès"),
            @ApiResponse(responseCode = "404", description = "Avion non trouvé",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
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