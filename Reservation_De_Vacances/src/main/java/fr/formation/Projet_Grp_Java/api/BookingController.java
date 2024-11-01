package fr.formation.Projet_Grp_Java.api;

import java.util.List;

import fr.formation.Projet_Grp_Java.exception.*;
import fr.formation.Projet_Grp_Java.model.*;
import fr.formation.Projet_Grp_Java.repo.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import fr.formation.Projet_Grp_Java.request.BookingRequest;
import fr.formation.Projet_Grp_Java.response.BookingResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
@Log4j2
public class BookingController {

    @Autowired
    private final BookingRepository bookingRepository;

    @Autowired
    private final UtilisateurRepository utilisateurRepository;

    @Autowired
    private final HotelRepository hotelRepository;

    @Autowired
    private final PlaneRepository planeRepository;

    @Autowired
    private final CarRepository carRepository;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Récupérer toutes les réservations",
            description = "Retourne une liste de toutes les réservations dans la base de données.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Récupération réussie des réservations.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BookingResponse.class))),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur.")
    })
    public List<BookingResponse> findAll() {
        log.debug("Finding all videos ...");

        return this.bookingRepository.findAll()
                .stream()
                .map(this::convert)
                .toList();
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Récupérer une réservation par ID",
            description = "Retourne une réservation identifiée par son ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Récupération réussie de la réservation.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BookingResponse.class))),
            @ApiResponse(responseCode = "404", description = "Réservation non trouvée.",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur.")
    })
    public BookingResponse findById(@PathVariable String id) {
        log.debug("Finding booking {} ...", id);

        Booking booking = this.bookingRepository.findById(id)
                .orElseThrow(BookingNotFoundException::new);
        return convert(booking);
    }

    private BookingResponse convert(Booking booking) {
        BookingResponse resp = BookingResponse.builder()
                .dateBegin(booking.getDateBegin())
                .dateEnd(booking.getDateEnd())
                .price(booking.getPrice())
                .user_id(booking.getUser() != null ? booking.getUser().getId() : null)
                .hotel_id(booking.getHotel() != null ? booking.getHotel().getId() : null)
                .plane_id(booking.getPlane() != null ? booking.getPlane().getId() : null)
                .car_id(booking.getCar() != null ? booking.getCar().getId() : null)
                .build();

        return resp;
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Créer une nouvelle réservation",
            description = "Crée une nouvelle réservation dans la base de données.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Réservation créée avec succès.",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Erreur de validation des données d'entrée.",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur.")
    })
    public String createBooking(@Valid @RequestBody BookingRequest bookingRequest, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Validation error: " + bindingResult.getFieldError().getDefaultMessage());
        }

        Booking booking = new Booking();

        Utilisateur user = utilisateurRepository.findById(bookingRequest.getUser_id())
                .orElseThrow(UserNotFoundException::new);

        if (bookingRequest.getHotel_id() != null) {
            Hotel hotel = hotelRepository.findById(bookingRequest.getHotel_id())
                    .orElseThrow(HotelNotFoundException::new);
            booking.setHotel(hotel);
        }
        if (bookingRequest.getPlane_id() != null) {
            Plane plane = planeRepository.findById(bookingRequest.getPlane_id())
                    .orElseThrow(PlaneNotFoundException::new);
            booking.setPlane(plane);
        }

        if (bookingRequest.getCar_id() != null) {
            Car car = carRepository.findById(bookingRequest.getCar_id())
                    .orElseThrow(CarNotFoundException::new);
            booking.setCar(car);
        }

        booking.setDateBegin(bookingRequest.getDateBegin());
        booking.setDateEnd(bookingRequest.getDateEnd());
        booking.setPrice(bookingRequest.getPrice());
        booking.setUser(user);

        bookingRepository.save(booking);
        return "Your booking was successfully created ";
    }

    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Mettre à jour une réservation existante",
            description = "Met à jour une réservation identifiée par son ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Réservation mise à jour avec succès.",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Réservation non trouvée.",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Erreur de validation des données d'entrée.",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur.")
    })
    public String update(@Valid @PathVariable String id, @RequestBody BookingRequest bookingRequest,
            BindingResult bindingResult) {
        log.debug("Updating booking {} ...", id);

        if (bindingResult.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Validation error: " + bindingResult.getFieldError().getDefaultMessage());
        }

        Booking booking = this.bookingRepository.findById(id)
                .orElseThrow(BookingNotFoundException::new);

        booking.setDateBegin(bookingRequest.getDateBegin());
        booking.setDateEnd(bookingRequest.getDateEnd());
        booking.setPrice(bookingRequest.getPrice());

        Utilisateur user = utilisateurRepository.findById(bookingRequest.getUser_id())
                .orElseThrow(UserNotFoundException::new);
        booking.setUser(user);

        if (bookingRequest.getHotel_id() != null) {
            Hotel hotel = hotelRepository.findById(bookingRequest.getHotel_id())
                    .orElseThrow(HotelNotFoundException::new);
            booking.setHotel(hotel);
        } else {
            booking.setHotel(null);
        }
        if (bookingRequest.getPlane_id() != null) {
            Plane plane = planeRepository.findById(bookingRequest.getPlane_id())
                    .orElseThrow(PlaneNotFoundException::new);
            booking.setPlane(plane);
        } else {
            booking.setPlane(null);
        }

        if (bookingRequest.getCar_id() != null) {
            Car car = carRepository.findById(bookingRequest.getCar_id())
                    .orElseThrow(CarNotFoundException::new);
            booking.setCar(car);
        } else {
            booking.setCar(null);
        }

        this.bookingRepository.save(booking);

        log.debug("Booking {} updated!", id);

        return "Booking modified successfully youhou";
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Supprimer une réservation par ID",
            description = "Supprime une réservation identifiée par son ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Réservation supprimée avec succès."),
            @ApiResponse(responseCode = "404", description = "Réservation non trouvée.",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur.")
    })
    public String deleteById(@PathVariable String id) {
        log.debug("Deleting booking {} ...", id);

        this.bookingRepository.deleteById(id);

        log.debug("Booking {} deleted!", id);

        return "Booking deleted !";
    }

}
