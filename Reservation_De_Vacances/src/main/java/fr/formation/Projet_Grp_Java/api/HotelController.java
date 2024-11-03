package fr.formation.Projet_Grp_Java.api;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import fr.formation.Projet_Grp_Java.exception.HotelNotFoundException;
import fr.formation.Projet_Grp_Java.model.Company;
import fr.formation.Projet_Grp_Java.model.Hotel;
import fr.formation.Projet_Grp_Java.repo.CompanyRepository;
import fr.formation.Projet_Grp_Java.repo.HotelRepository;
import fr.formation.Projet_Grp_Java.request.HotelRequest;
import fr.formation.Projet_Grp_Java.response.HotelResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping("/api/hotel")
@RequiredArgsConstructor
@Log4j2
public class HotelController {

    private final HotelRepository hotelRepository;
    private final CompanyRepository companyRepository;

    @GetMapping
    @Operation(summary = "Récupérer tous les hôtels",
            description = "Retourne une liste de tous les hôtels dans la base de données.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Récupération réussie des hôtels",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = HotelResponse.class))),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public List<HotelResponse> findAll() {
        log.debug("Finding all hotel ...");

        return this.hotelRepository.findAll()
                .stream()
                .map(this::convert)
                .toList();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer un hôtel par ID",
            description = "Retourne un hôtel identifié par son ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Récupération réussie de l'hôtel",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = HotelResponse.class))),
            @ApiResponse(responseCode = "404", description = "Hôtel non trouvé",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public HotelResponse findById(@PathVariable String id) {
        log.debug("Finding Hotel {} ...", id);

        Hotel hotel = this.hotelRepository.findById(id).orElseThrow(HotelNotFoundException::new);
        HotelResponse resp = HotelResponse.builder().build();

        BeanUtils.copyProperties(hotel, resp);

        return resp;
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Créer un nouvel hôtel",
            description = "Crée un nouvel hôtel dans la base de données.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Hôtel créé avec succès",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Données d'entrée invalides",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public String createHotel(@RequestBody HotelRequest hotelRequest) {

        Company company = companyRepository.findById(hotelRequest.getCompanyId())
                .orElseThrow(() -> new RuntimeException("Company not found"));
        Hotel hotel = new Hotel();

        hotel.setNom(hotelRequest.getNom());
        hotel.setMail(hotelRequest.getMail());
        hotel.setTelephone(hotelRequest.getTelephone());
        hotel.setVille(hotelRequest.getVille());
        hotel.setAdresse(hotelRequest.getAdresse());
        hotel.setCompany(company);

        // Sauvegardez l'utilisateur dans la base de données
        hotelRepository.save(hotel);
        return "Hotel created successfully";
    }

    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Mettre à jour un hôtel existant",
            description = "Met à jour un hôtel identifié par son ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Hôtel mis à jour avec succès",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Hôtel non trouvé",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Données d'entrée invalides",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public String update(@PathVariable String id, @RequestBody HotelRequest hotelRequest) {
        log.debug("Updating video {} ...", id);

        Hotel hotel = this.hotelRepository.findById(id).orElseThrow(HotelNotFoundException::new);

        BeanUtils.copyProperties(hotelRequest, hotel);

        this.hotelRepository.save(hotel);

        log.debug("Video {} updated!", id);

        return "Hotel modified successfully";
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Supprimer un hôtel par ID",
            description = "Supprime un hôtel identifié par son ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Hôtel supprimé avec succès"),
            @ApiResponse(responseCode = "404", description = "Hôtel non trouvé",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public void deleteById(@PathVariable String id) {
        log.debug("Deleting video {} ...", id);

        this.hotelRepository.deleteById(id);

        log.debug("Video {} deleted!", id);
    }

    private HotelResponse convert(Hotel hotel) {
        HotelResponse resp = HotelResponse.builder().build();

        BeanUtils.copyProperties(hotel, resp);

        return resp;
    }
}