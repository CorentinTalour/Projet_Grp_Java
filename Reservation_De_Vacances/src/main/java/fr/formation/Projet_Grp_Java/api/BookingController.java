package fr.formation.Projet_Grp_Java.api;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import fr.formation.Projet_Grp_Java.exception.BookingNotFoundException;
import fr.formation.Projet_Grp_Java.model.Booking;

import fr.formation.Projet_Grp_Java.model.Hotel;
import fr.formation.Projet_Grp_Java.model.Utilisateur;
import fr.formation.Projet_Grp_Java.repo.BookingRepository;

import fr.formation.Projet_Grp_Java.repo.HotelRepository;
import fr.formation.Projet_Grp_Java.repo.UtilisateurRepository;
import fr.formation.Projet_Grp_Java.request.BookingRequest;
import fr.formation.Projet_Grp_Java.response.BookingResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

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

    @GetMapping
    public List<BookingResponse> findAll() {
        log.debug("Finding all videos ...");

        return this.bookingRepository.findAll()
                .stream()
                .map(this::convert)
                .toList();
    }

    @GetMapping("/{id}")
    public BookingResponse findById(@PathVariable String id) {
        log.debug("Finding booking {} ...", id);

        Booking booking = this.bookingRepository.findById(id).orElseThrow(BookingNotFoundException::new);
        BookingResponse resp = BookingResponse.builder().build();

        BeanUtils.copyProperties(booking, resp);

        return resp;
    }

    private BookingResponse convert(Booking booking) {
        BookingResponse resp = BookingResponse.builder().build();

        BeanUtils.copyProperties(booking, resp);

        return resp;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)

    public String createBooking(@RequestBody BookingRequest bookingRequest) {

        Utilisateur user = utilisateurRepository.findById(bookingRequest.getUser_id())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Hotel hotel = hotelRepository.findById(bookingRequest.getHotel_id())
                .orElseThrow(() -> new RuntimeException("Hotel not found"));

        Booking booking = new Booking();

        booking.setDateBegin(bookingRequest.getDateBegin());
        booking.setDateEnd(bookingRequest.getDateEnd());
        booking.setPrice(bookingRequest.getPrice());
        booking.setUser(user);
        booking.setHotel(hotel);
        // Sauvegardez l'utilisateur dans la base de données
        bookingRepository.save(booking);
        return "Your booking was successfully created ";
    }

    @PutMapping("/{id}")
    public String update(@PathVariable String id, @RequestBody BookingRequest bookingRequest) {
        log.debug("Updating booking {} ...", id);

        Booking booking = this.bookingRepository.findById(id).orElseThrow(BookingNotFoundException::new);

        BeanUtils.copyProperties(bookingRequest, booking);

        this.bookingRepository.save(booking);

        log.debug("Booking {} updated!", id);

        return "Booking modified successfully youhou";
    }

    @DeleteMapping("/{id}")
    public String deleteById(@PathVariable String id) {
        log.debug("Deleting booking {} ...", id);

        this.bookingRepository.deleteById(id);

        log.debug("Booking {} deleted!", id);

        return "Booking deleted !";
    }

}
