package fr.formation.Projet_Grp_Java.api;

import java.util.List;

import fr.formation.Projet_Grp_Java.exception.CompanyTypeNotFoundException;
import fr.formation.Projet_Grp_Java.model.CompanyType;
import fr.formation.Projet_Grp_Java.repo.CompanyTypeRepository;
import jakarta.annotation.PostConstruct;
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

import fr.formation.Projet_Grp_Java.exception.CompanyNotFoundException;
import fr.formation.Projet_Grp_Java.exception.HotelNotFoundException;
import fr.formation.Projet_Grp_Java.model.Company;
import fr.formation.Projet_Grp_Java.repo.CompanyRepository;
import fr.formation.Projet_Grp_Java.request.CompanyRequest;
import fr.formation.Projet_Grp_Java.response.CompanyResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping("/api/company")
@RequiredArgsConstructor
@Log4j2
public class CompanyController {

    private final CompanyRepository companyRepository;
    private final CompanyTypeRepository companyTypeRepository;

    @PostConstruct
    public void init() {
        // Vérifiez si la table est vide
        if (companyTypeRepository.count() == 0) {
            CompanyType companyTypeHotel = new CompanyType();
            companyTypeHotel.setId("1");
            companyTypeHotel.setCompanyTypeName("Hôtel");
            companyTypeRepository.save(companyTypeHotel);

            CompanyType companyTypePlane = new CompanyType();
            companyTypePlane.setId("2");
            companyTypePlane.setCompanyTypeName("Avion");
            companyTypeRepository.save(companyTypePlane);

            CompanyType companyTypeCar = new CompanyType();
            companyTypeCar.setId("3");
            companyTypeCar.setCompanyTypeName("Voiture");
            companyTypeRepository.save(companyTypeCar);
        }
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public List<CompanyResponse> findAll() {
        log.debug("Finding all companies...");

        return this.companyRepository.findAll()
                .stream()
                .map(this::convert)
                .toList();
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public CompanyResponse findById(@PathVariable String id) {
        log.debug("Finding video {} ...", id);

        Company company = this.companyRepository.findById(id).orElseThrow(CompanyNotFoundException::new);
        CompanyResponse resp = CompanyResponse.builder().build();

        BeanUtils.copyProperties(company, resp);

        return resp;
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.CREATED)

    public String createCompany(@RequestBody CompanyRequest companyRequest) {

        Company company = new Company();

        company.setNameAgency(companyRequest.getNameAgency());

        CompanyType companyType = companyTypeRepository.findById(companyRequest.getCompanyTypeId())
                .orElseThrow(CompanyTypeNotFoundException::new);
        company.setCompanyType(companyType);

        // Sauvegardez l'utilisateur dans la base de données
        companyRepository.save(company);
        return "Company created successfully";
    }

    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public String update(@PathVariable String id, @RequestBody CompanyRequest companyRequest) {
        log.debug("Updating company {} ...", id);

        Company company = this.companyRepository.findById(id).orElseThrow(HotelNotFoundException::new);

        BeanUtils.copyProperties(companyRequest, company);

        if (companyRequest.getCompanyTypeId() != null) {
            log.debug("Looking for CompanyType with ID: {}", companyRequest.getCompanyTypeId());

            CompanyType companyType = companyTypeRepository.findById(companyRequest.getCompanyTypeId())
                    .orElseThrow(CompanyTypeNotFoundException::new);

            company.setCompanyType(companyType);
        }

        this.companyRepository.save(company);

        log.debug("Company {} updated!", id);

        return "Hotel modified successfully";
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public void deleteById(@PathVariable String id) {
        log.debug("Deleting company {} ...", id);

        this.companyRepository.deleteById(id);

        log.debug("Company {} deleted!", id);
    }

    private CompanyResponse convert(Company company) {
        CompanyResponse resp = CompanyResponse.builder().build();

        BeanUtils.copyProperties(company, resp);

        return resp;
    }
}
