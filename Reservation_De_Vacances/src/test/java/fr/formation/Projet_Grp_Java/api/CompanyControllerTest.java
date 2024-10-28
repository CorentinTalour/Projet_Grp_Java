package fr.formation.Projet_Grp_Java.api;

import com.fasterxml.jackson.databind.ObjectMapper;

import fr.formation.Projet_Grp_Java.model.Company;
import fr.formation.Projet_Grp_Java.model.CompanyType;
import fr.formation.Projet_Grp_Java.repo.CompanyRepository;
import fr.formation.Projet_Grp_Java.repo.CompanyTypeRepository;
import fr.formation.Projet_Grp_Java.request.CompanyRequest;
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class CompanyControllerTest {

    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private CompanyTypeRepository companyTypeRepository;

    @InjectMocks
    private CompanyController companyController;

    private MockMvc mockMvc;

    private ObjectMapper mapper;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(companyController).build();
        this.mapper = new ObjectMapper();
    }

    @Test
    void shouldFindAllStatusOk() throws Exception {
        // given
        Company company1 = new Company();
        Company company2 = new Company();
        Mockito.when(companyRepository.findAll()).thenReturn(List.of(company1, company2));

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/company"))
                .andExpect(MockMvcResultMatchers.status().isOk());

        Mockito.verify(companyRepository).findAll();
    }

    @Test
    void shouldFindByIdStatusOk() throws Exception {
        // given
        Company company = new Company();
        company.setId("123");
        company.setNameAgency("Test Agency");
        Mockito.when(companyRepository.findById("123")).thenReturn(Optional.of(company));

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/company/123"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.nameAgency").value("Test Agency"));

        Mockito.verify(companyRepository).findById("123");
    }

    @Test
    void shouldFindByIdStatusNotFound() throws Exception {
        // given
        Mockito.when(companyRepository.findById("999")).thenReturn(Optional.empty());

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/company/999"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        Mockito.verify(companyRepository).findById("999");
    }

    @Test
    void shouldCreateCompanyStatusCreated() throws Exception {
        // given
        CompanyRequest request = new CompanyRequest();
        request.setNameAgency("New Agency");
        request.setCompanyTypeId("2");
        String content = mapper.writeValueAsString(request);

        CompanyType mockCompanyType = new CompanyType();
        mockCompanyType.setId("2");
        mockCompanyType.setCompanyTypeName("Type Test");
        Mockito.when(companyTypeRepository.findById("2")).thenReturn(Optional.of(mockCompanyType));

        ArgumentCaptor<Company> captor = ArgumentCaptor.forClass(Company.class);

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/company")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        Mockito.verify(companyRepository, Mockito.times(1)).save(captor.capture());
        assertEquals("New Agency", captor.getValue().getNameAgency());
    }

    @Test
    void shouldUpdateCompanyStatusOk() throws Exception {
        // given
        Company existingCompany = new Company();
        existingCompany.setId("123");
        existingCompany.setNameAgency("Old Agency");
        Mockito.when(companyRepository.findById("123")).thenReturn(Optional.of(existingCompany));

        CompanyRequest request = new CompanyRequest();
        request.setNameAgency("Updated Agency");
        String content = mapper.writeValueAsString(request);

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.put("/api/company/123")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(MockMvcResultMatchers.status().isOk());

        Mockito.verify(companyRepository).save(existingCompany);
        assertEquals("Updated Agency", existingCompany.getNameAgency());
    }

    @Test
    void shouldUpdateCompanyStatusNotFound() throws Exception {
        // given
        Mockito.when(companyRepository.findById("999")).thenReturn(Optional.empty());

        CompanyRequest request = new CompanyRequest();
        request.setNameAgency("Agency not found");
        String content = mapper.writeValueAsString(request);

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.put("/api/company/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        Mockito.verify(companyRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void shouldDeleteCompanyStatusNoContent() throws Exception {
        // given
        Company company = new Company();
        company.setId("123");

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/company/123"))
                .andExpect(MockMvcResultMatchers.status().isOk());

        Mockito.verify(companyRepository).deleteById("123");
    }

}
