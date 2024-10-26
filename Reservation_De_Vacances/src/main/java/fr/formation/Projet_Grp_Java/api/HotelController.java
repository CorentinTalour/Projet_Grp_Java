package fr.formation.Projet_Grp_Java.api;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
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
    public List<HotelResponse> findAll() {
        log.debug("Finding all videos ...");

        return this.hotelRepository.findAll()
                .stream()
                .map(this::convert)
                .toList();
    }

    @GetMapping("/{id}")
    public HotelResponse findById(@PathVariable String id) {
        log.debug("Finding video {} ...", id);

        Hotel hotel = this.hotelRepository.findById(id).orElseThrow(HotelNotFoundException::new);
        HotelResponse resp = HotelResponse.builder().build();

        BeanUtils.copyProperties(hotel, resp);

        return resp;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)

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
    public String update(@PathVariable String id, @RequestBody HotelRequest hotelRequest) {
        log.debug("Updating video {} ...", id);

        Hotel hotel = this.hotelRepository.findById(id).orElseThrow(HotelNotFoundException::new);

        BeanUtils.copyProperties(hotelRequest, hotel);

        this.hotelRepository.save(hotel);

        log.debug("Video {} updated!", id);

        return "Hotel modified successfully";
    }

    @DeleteMapping("/{id}")
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