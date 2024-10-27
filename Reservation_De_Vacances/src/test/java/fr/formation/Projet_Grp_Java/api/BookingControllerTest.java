package fr.formation.Projet_Grp_Java.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.formation.Projet_Grp_Java.api.BookingController;
import fr.formation.Projet_Grp_Java.exception.BookingNotFoundException;
import fr.formation.Projet_Grp_Java.model.Booking;
import fr.formation.Projet_Grp_Java.model.Hotel;
import fr.formation.Projet_Grp_Java.model.Utilisateur;
import fr.formation.Projet_Grp_Java.repo.BookingRepository;
import fr.formation.Projet_Grp_Java.repo.HotelRepository;
import fr.formation.Projet_Grp_Java.repo.UtilisateurRepository;
import fr.formation.Projet_Grp_Java.request.BookingRequest;
import fr.formation.Projet_Grp_Java.response.BookingResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class BookingControllerTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private UtilisateurRepository utilisateurRepository;

    @Mock
    private HotelRepository hotelRepository;

    @InjectMocks
    private BookingController bookingController;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(this.bookingController).build();
        this.objectMapper = new ObjectMapper();
    }

    @Test
    void shouldFindAllStatusOk() throws Exception {
        // given
        Booking booking1 = new Booking();
        Booking booking2 = new Booking();
        List<Booking> bookings = List.of(booking1, booking2);

        Mockito.when(bookingRepository.findAll()).thenReturn(bookings);

        // when - then
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/bookings"))
                .andExpect(MockMvcResultMatchers.status().isOk());

        Mockito.verify(bookingRepository).findAll();
    }

    @Test
    void shouldFindByIdStatusOk() throws Exception {
        // given
        Booking booking = new Booking();
        booking.setId("booking-id");
        Mockito.when(bookingRepository.findById("booking-id")).thenReturn(Optional.of(booking));

        // when - then
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/bookings/booking-id"))
                .andExpect(MockMvcResultMatchers.status().isOk());

        Mockito.verify(bookingRepository).findById("booking-id");
    }

    @Test
    void shouldFindByIdStatusNotFound() throws Exception {
        // given
        Mockito.when(bookingRepository.findById("invalid-id")).thenReturn(Optional.empty());

        // when - then
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/bookings/invalid-id"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        Mockito.verify(bookingRepository).findById("invalid-id");
    }

    @Test
    void shouldCreateBookingStatusCreated() throws Exception {
        // given
        BookingRequest request = new BookingRequest();
        request.setUser_id("user1");
        request.setHotel_id("hotel1");

        Utilisateur user = new Utilisateur();
        user.setId("user1");
        Hotel hotel = new Hotel();
        hotel.setId("hotel1");

        Mockito.when(utilisateurRepository.findById("user1")).thenReturn(Optional.of(user));
        Mockito.when(hotelRepository.findById("hotel1")).thenReturn(Optional.of(hotel));

        String content = objectMapper.writeValueAsString(request);

        // when - then
        this.mockMvc.perform(
                MockMvcRequestBuilders.post("/api/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        Mockito.verify(bookingRepository).save(Mockito.any());
    }

    @Test
    void shouldUpdateBookingStatusOk() throws Exception {
        // given
        Booking booking = new Booking();
        booking.setId("booking-id");
        BookingRequest updateRequest = new BookingRequest();
        updateRequest.setPrice(150.0f);

        Mockito.when(bookingRepository.findById("booking-id")).thenReturn(Optional.of(booking));

        String content = objectMapper.writeValueAsString(updateRequest);

        // when - then
        this.mockMvc.perform(
                MockMvcRequestBuilders.put("/api/bookings/booking-id")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(MockMvcResultMatchers.status().isOk());

        Mockito.verify(bookingRepository).save(Mockito.any());
    }

    @Test
    void shouldUpdateBookingStatusNotFound() throws Exception {
        // given
        BookingRequest updateRequest = new BookingRequest();
        updateRequest.setPrice(150.0f);

        Mockito.when(bookingRepository.findById("invalid-id")).thenReturn(Optional.empty());

        String content = objectMapper.writeValueAsString(updateRequest);

        // when - then
        this.mockMvc.perform(
                MockMvcRequestBuilders.put("/api/bookings/invalid-id")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        Mockito.verify(bookingRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void shouldDeleteBookingStatusOk() throws Exception {
        // given

        // when - then
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/api/bookings/booking-id"))
                .andExpect(MockMvcResultMatchers.status().isOk());

        Mockito.verify(bookingRepository).deleteById("booking-id");
    }

}
