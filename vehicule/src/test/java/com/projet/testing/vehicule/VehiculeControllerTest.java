package com.projet.testing.vehicule;
import com.projet.testing.vehicule.controller.VehiculeController;
import com.projet.testing.vehicule.dto.VehiculeDto;
import com.projet.testing.vehicule.service.VehiculeService;
import com.projet.testing.vehicule.exception.DuplicateRegistrationNumberException; // Importez l'exception

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq; // Pour matcher un argument spécifique

@WebMvcTest(VehiculeController.class)
public class VehiculeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VehiculeService vehiculeService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testCreateVehiculeSuccess() throws Exception {
        VehiculeDto vehicule = new VehiculeDto();
        vehicule.setId(UUID.randomUUID());
        vehicule.setRegistrationNumber("ABC123");
        vehicule.setMake("Toyota");
        vehicule.setModel("Corolla");
        vehicule.setYear(2022);
        vehicule.setRentalPrice(300.0);

        Mockito.when(vehiculeService.createVehicule(any(VehiculeDto.class)))
                .thenReturn(vehicule);

        mockMvc.perform(post("/vehicule/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(vehicule))
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.registrationNumber").value("ABC123"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void createVehicule_invalidInput_emptyRegistrationNumber_shouldReturnBadRequest() throws Exception {
        VehiculeDto invalidVehiculeDto = new VehiculeDto();
        invalidVehiculeDto.setRegistrationNumber(""); // Numéro d'immatriculation vide
        invalidVehiculeDto.setMake("Renault");
        invalidVehiculeDto.setModel("Clio");
        invalidVehiculeDto.setYear(2023);
        invalidVehiculeDto.setRentalPrice(40.0);

        // On ne s'attend pas à ce que le service soit appelé car la validation doit échouer avant
        Mockito.verifyNoInteractions(vehiculeService);

        mockMvc.perform(post("/vehicule/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidVehiculeDto))
                        .with(csrf()))
                .andExpect(status().isBadRequest()) // Attendre un statut HTTP 400 Bad Request
                // Vérifier la présence d'erreurs de validation dans la réponse JSON
                // La structure par défaut est un tableau d'objets d'erreur
                .andExpect(jsonPath("$").isArray()) // Vérifier que la réponse est un tableau JSON
                .andExpect(jsonPath("$.length()").value(1)) // S'assurer qu'il y a une erreur
                .andExpect(jsonPath("$.[0].field").value("registrationNumber")) // Vérifier le champ d'erreur
                .andExpect(jsonPath("$.[0].defaultMessage").value("registration number cannot be empty")); // Vérifier le message d'erreur
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void createVehicule_invalidInput_nullRegistrationNumber_shouldReturnBadRequest() throws Exception {
        VehiculeDto invalidVehiculeDto = new VehiculeDto();
        invalidVehiculeDto.setRegistrationNumber(null); // Numéro d'immatriculation null
        invalidVehiculeDto.setMake("Peugeot");
        invalidVehiculeDto.setModel("308");
        invalidVehiculeDto.setYear(2021);
        invalidVehiculeDto.setRentalPrice(55.0);

        Mockito.verifyNoInteractions(vehiculeService);

        mockMvc.perform(post("/vehicule/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidVehiculeDto))
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$.[0].field").value("registrationNumber"))
                .andExpect(jsonPath("$.[0].defaultMessage").value("registration number is mandatory")); // Message par défaut de @NotNull
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void createVehicule_invalidInput_nullMake_shouldReturnBadRequest() throws Exception {
        VehiculeDto invalidVehiculeDto = new VehiculeDto();
        invalidVehiculeDto.setRegistrationNumber("DEF456");
        invalidVehiculeDto.setMake(null); // Marque null
        invalidVehiculeDto.setModel("Corsa");
        invalidVehiculeDto.setYear(2020);
        invalidVehiculeDto.setRentalPrice(35.0);

        Mockito.verifyNoInteractions(vehiculeService);

        mockMvc.perform(post("/vehicule/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidVehiculeDto))
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$.[0].field").value("make"))
                .andExpect(jsonPath("$.[0].defaultMessage").value("make is mandatory")); // Message par défaut de @NotNull
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void createVehicule_invalidInput_negativeRentalPrice_shouldReturnBadRequest() throws Exception {
        VehiculeDto invalidVehiculeDto = new VehiculeDto();
        invalidVehiculeDto.setRegistrationNumber("GHI789");
        invalidVehiculeDto.setMake("BMW");
        invalidVehiculeDto.setModel("X5");
        invalidVehiculeDto.setYear(2024);
        invalidVehiculeDto.setRentalPrice(-20.0); // Prix de location négatif

        Mockito.verifyNoInteractions(vehiculeService); // Le service ne doit pas être appelé

        mockMvc.perform(post("/vehicule/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidVehiculeDto))
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$.[0].field").value("rentalPrice"))
                .andExpect(jsonPath("$.[0].defaultMessage").value("Rental price cannot be negative")); // Message de notre @Min
    }

    // --- V3: Échec création doublon registrationNumber ---
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void createVehicule_duplicateRegistrationNumber_shouldReturnBadRequest() throws Exception {
        VehiculeDto vehiculeDto = new VehiculeDto();
        vehiculeDto.setRegistrationNumber("DUPLI456");
        vehiculeDto.setMake("Audi");
        vehiculeDto.setModel("A4");
        vehiculeDto.setYear(2020);
        vehiculeDto.setRentalPrice(120.0);

        // Simuler le service qui lance l'exception de doublon
        Mockito.when(vehiculeService.createVehicule(any(VehiculeDto.class)))
                .thenThrow(new DuplicateRegistrationNumberException("A vehicle with registration number DUPLI456 already exists."));

        mockMvc.perform(post("/vehicule/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(vehiculeDto))
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("A vehicle with registration number DUPLI456 already exists."));
    }

    // --- V4: Succès GET /vehicule/id/{id} ---
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void getVehiculeById_success_shouldReturnVehicule() throws Exception {
        UUID vehiculeId = UUID.randomUUID();
        VehiculeDto vehicule = new VehiculeDto();
        vehicule.setId(vehiculeId);
        vehicule.setRegistrationNumber("GET123");
        vehicule.setMake("Volkswagen");
        vehicule.setModel("Golf");
        vehicule.setYear(2018);
        vehicule.setRentalPrice(75.0);

        Mockito.when(vehiculeService.getVehiculeById(vehiculeId))
                .thenReturn(vehicule);

        mockMvc.perform(get("/vehicule/id/{id}", vehiculeId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(vehiculeId.toString()))
                .andExpect(jsonPath("$.registrationNumber").value("GET123"));
    }

    // --- V5: Succès suppression /vehicule/{id} ---
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void deleteVehicule_success_shouldReturnSuccessMessage() throws Exception {
        UUID vehiculeId = UUID.randomUUID();

        Mockito.when(vehiculeService.deleteVehicule(vehiculeId))
                .thenReturn(true);

        mockMvc.perform(delete("/vehicule/{id}", vehiculeId)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"message\" : \"vehicule is deleted successfully\"}"));
    }

    // --- V6: Échec suppression : id inconnu ---
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void deleteVehicule_notFound_shouldReturnNotFoundMessage() throws Exception {
        UUID nonExistentId = UUID.randomUUID();

        Mockito.when(vehiculeService.deleteVehicule(nonExistentId))
                .thenReturn(false);

        mockMvc.perform(delete("/vehicule/{id}", nonExistentId)
                        .with(csrf()))
                .andExpect(status().isNotFound())
                .andExpect(content().json("{\"message\" : \"vehicule doesn't exists\"}"));
    }

    // --- V7: Échec récupération par ID inconnu ---
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void getVehiculeById_notFound_shouldReturnNotFound() throws Exception {
        UUID nonExistentId = UUID.randomUUID();

        Mockito.when(vehiculeService.getVehiculeById(nonExistentId))
                .thenReturn(null);

        mockMvc.perform(get("/vehicule/id/{id}", nonExistentId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("")); // Pas de corps de réponse pour 404 dans votre contrôleur
    }

    // --- V8: Succès /vehicule/search-by-price ---
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void getAllByPrice_success_shouldReturnFilteredList() throws Exception {
        double searchPrice = 50.0;
        VehiculeDto vehicule1 = new VehiculeDto();
        vehicule1.setId(UUID.randomUUID());
        vehicule1.setRegistrationNumber("PRICE001");
        vehicule1.setMake("Honda");
        vehicule1.setRentalPrice(50.0);

        VehiculeDto vehicule2 = new VehiculeDto();
        vehicule2.setId(UUID.randomUUID());
        vehicule2.setRegistrationNumber("PRICE002");
        vehicule2.setMake("Hyundai");
        vehicule2.setRentalPrice(50.0);

        List<VehiculeDto> filteredList = Arrays.asList(vehicule1, vehicule2);

        Mockito.when(vehiculeService.getVehicule(searchPrice))
                .thenReturn(filteredList);

        mockMvc.perform(get("/vehicule/search-by-price")
                        .param("rentalPrice", String.valueOf(searchPrice))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$.[0].registrationNumber").value("PRICE001"));
    }

    // --- V9: Succès /vehicule/number/{registerNum} ---
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void getVehiculeByNumber_success_shouldReturnVehicule() throws Exception {
        String registrationNum = "NUMTEST123";
        VehiculeDto vehicule = new VehiculeDto();
        vehicule.setId(UUID.randomUUID());
        vehicule.setRegistrationNumber(registrationNum);
        vehicule.setMake("Mercedes");
        vehicule.setModel("C-Class");
        vehicule.setYear(2022);
        vehicule.setRentalPrice(200.0);

        Mockito.when(vehiculeService.getVehiculeByNumber(registrationNum))
                .thenReturn(vehicule);

        mockMvc.perform(get("/vehicule/number/{registerNum}", registrationNum)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.registrationNumber").value(registrationNum))
                .andExpect(jsonPath("$.make").value("Mercedes"));
    }

    // --- V10: Échec /vehicule/number/{registerNum} registre inconnu ---
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void getVehiculeByNumber_notFound_shouldReturnNotFound() throws Exception {
        String nonExistentRegistrationNum = "UNKNOWN404";

        Mockito.when(vehiculeService.getVehiculeByNumber(nonExistentRegistrationNum))
                .thenReturn(null);

        mockMvc.perform(get("/vehicule/number/{registerNum}", nonExistentRegistrationNum)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("")); // Pas de corps de réponse
    }

    // --- V11: Succès update /vehicule/{id} ---
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void updateVehicule_success_shouldReturnUpdatedVehicule() throws Exception {
        UUID vehiculeId = UUID.randomUUID();
        VehiculeDto updatedVehicule = new VehiculeDto();
        updatedVehicule.setId(vehiculeId); // L'ID doit correspondre
        updatedVehicule.setRegistrationNumber("NEWXYZ"); // Champ mis à jour
        updatedVehicule.setMake("NewMake");
        updatedVehicule.setModel("NewModel");
        updatedVehicule.setYear(2023);
        updatedVehicule.setRentalPrice(150.0);

        Mockito.when(vehiculeService.updateVehicule(any(VehiculeDto.class), eq(vehiculeId)))
                .thenReturn(updatedVehicule);

        mockMvc.perform(put("/vehicule/{id}", vehiculeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedVehicule))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(vehiculeId.toString()))
                .andExpect(jsonPath("$.registrationNumber").value("NEWXYZ"))
                .andExpect(jsonPath("$.make").value("NewMake"));
    }

    // --- V11 - Scénario d'échec : ID inexistant pour la mise à jour (Bonne pratique) ---
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void updateVehicule_notFound_shouldReturnNotFound() throws Exception {
        UUID nonExistentId = UUID.randomUUID();
        VehiculeDto updateDto = new VehiculeDto();
        updateDto.setRegistrationNumber("NONEXIST");
        updateDto.setMake("Test");
        updateDto.setRentalPrice(10.0);

        Mockito.when(vehiculeService.updateVehicule(any(VehiculeDto.class), eq(nonExistentId)))
                .thenReturn(null);

        mockMvc.perform(put("/vehicule/{id}", nonExistentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto))
                        .with(csrf()))
                .andExpect(status().isNotFound())
                .andExpect(content().string(""));
    }

    // --- V11 - Scénario d'échec : Validation du DTO de mise à jour (Bonne pratique) ---
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void updateVehicule_invalidInput_shouldReturnBadRequest() throws Exception {
        UUID vehiculeId = UUID.randomUUID();
        VehiculeDto invalidUpdateDto = new VehiculeDto();
        invalidUpdateDto.setRegistrationNumber("VALIDREG");
        invalidUpdateDto.setMake(null); // Invalide
        invalidUpdateDto.setRentalPrice(10.0);

        Mockito.verifyNoInteractions(vehiculeService);

        mockMvc.perform(put("/vehicule/{id}", vehiculeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidUpdateDto))
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.[0].field").value("make"))
                .andExpect(jsonPath("$.[0].defaultMessage").value("make is mandatory"));
    }
}

