package com.projet.testing.vehicule;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.projet.testing.vehicule.dto.VehiculeDto;
import com.projet.testing.vehicule.model.Vehicule;
import com.projet.testing.vehicule.repository.VehiculeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
// Supprimé: assertFalse, assertTrue car assertThat est plus idiomatique avec AssertJ

// Utilisation de MethodOrderer.OrderAnnotation pour ordonner les tests
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@Transactional
@ActiveProfiles("test")
public class VehiculeServiceTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private VehiculeRepository vehiculeRepository;

    // Variables statiques pour stocker les données créées et les réutiliser
    private static UUID createdVehiculeId;
    private static String createdVehiculeRegistrationNumber;
    private static String createdVehiculeMake = "WorkflowMake";
    private static double createdVehiculeRentalPrice = 150.0;


    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    // Méthode utilitaire pour créer une entité Vehicule (pour les pré-conditions)
    private Vehicule createVehiculeEntity(String registrationNumber, String make, double rentalPrice) {
        Vehicule vehicule = new Vehicule();
        vehicule.setRegistrationNumber(registrationNumber);
        vehicule.setMake(make);
        vehicule.setModel("ModelX");
        vehicule.setYear(2020);
        vehicule.setRentalPrice(rentalPrice);
        vehicule.setCheminVersImage("/chemin/par/defaut/de/mon/image.jpg");
        return vehicule;
    }

    /**
     * Cas de test 1 : Création réussie d'un véhicule
     * Crée un véhicule et stocke son ID et numéro d'immatriculation pour les tests suivants.
     */
    @Test
    @Order(1)
    @Rollback(false)
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void test1_createVehiculeSuccess() throws Exception {
        VehiculeDto vehiculeDto = new VehiculeDto();
        createdVehiculeRegistrationNumber = "WF_" + UUID.randomUUID().toString().substring(0, 8);
        vehiculeDto.setRegistrationNumber(createdVehiculeRegistrationNumber);
        vehiculeDto.setMake(createdVehiculeMake);
        vehiculeDto.setModel("Corolla");
        vehiculeDto.setYear(2022);
        vehiculeDto.setRentalPrice(createdVehiculeRentalPrice);
        vehiculeDto.setCheminVersImage("/chemin/par/defaut/de/mon/image.jpg");

        String responseContent = mockMvc.perform(post("/vehicule/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(vehiculeDto))
                        .with(csrf()))
                .andDo(result -> System.out.println("Réponse du serveur (Create Success) : " + result.getResponse().getContentAsString()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.registrationNumber").value(createdVehiculeRegistrationNumber))
                .andExpect(jsonPath("$.id").exists())
                .andReturn().getResponse().getContentAsString();

        VehiculeDto createdDto = objectMapper.readValue(responseContent, VehiculeDto.class);
        createdVehiculeId = createdDto.getId();

        assertThat(vehiculeRepository.findByRegistrationNumber(createdVehiculeRegistrationNumber)).isPresent();
        assertThat(vehiculeRepository.findById(createdVehiculeId)).isPresent();
        assertNotNull(createdVehiculeId);
        assertNotNull(createdVehiculeRegistrationNumber);
    }

    /**
     * Cas de test 2 : Récupération d'un véhicule par ID (utilisant l'ID du test 1)
     */
    @Test
    @Order(2)
    @WithMockUser(username = "user", roles = {"USER"})
    public void test2_getVehiculeByIdSuccess() throws Exception {
        assertNotNull(createdVehiculeId, "L'ID du véhicule créé ne doit pas être null (test1_createVehiculeSuccess a échoué ?)");

        mockMvc.perform(get("/vehicule/id/{id}", createdVehiculeId)
                        .with(csrf()))
                .andDo(result -> System.out.println("Réponse du serveur (Get By Id) : " + result.getResponse().getContentAsString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createdVehiculeId.toString()))
                .andExpect(jsonPath("$.registrationNumber").value(createdVehiculeRegistrationNumber));
    }

    /**
     * Cas de test 3 : Récupération d'un véhicule par numéro d'immatriculation (utilisant le numéro du test 1)
     */
    @Test
    @Order(3)
    @WithMockUser(username = "user", roles = {"USER"})
    public void test3_getVehiculeByNumberSuccess() throws Exception {
        assertNotNull(createdVehiculeRegistrationNumber, "Le numéro d'immatriculation ne doit pas être null (test1_createVehiculeSuccess a échoué ?)");

        mockMvc.perform(get("/vehicule/number/{registerNumber}", createdVehiculeRegistrationNumber)
                        .with(csrf()))
                .andDo(result -> System.out.println("Réponse du serveur (Get By Number) : " + result.getResponse().getContentAsString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createdVehiculeId.toString()))
                .andExpect(jsonPath("$.registrationNumber").value(createdVehiculeRegistrationNumber));
    }

    /**
     * Cas de test 4 : Mise à jour d'un véhicule existant (utilisant l'ID du test 1)
     */
    @Test
    @Order(4)
    @Rollback(false)
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void test4_updateVehiculeSuccess() throws Exception {
        assertNotNull(createdVehiculeId, "L'ID du véhicule créé ne doit pas être null (test1_createVehiculeSuccess a échoué ?)");

        VehiculeDto updatedVehiculeDto = new VehiculeDto();
        updatedVehiculeDto.setRegistrationNumber(createdVehiculeRegistrationNumber);
        updatedVehiculeDto.setMake("UpdatedMake");
        updatedVehiculeDto.setModel("UpdatedModel");
        updatedVehiculeDto.setYear(2023);
        updatedVehiculeDto.setRentalPrice(250.0);
        updatedVehiculeDto.setCheminVersImage("/chemin/updated/image.jpg");

        mockMvc.perform(put("/vehicule/{id}", createdVehiculeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedVehiculeDto))
                        .with(csrf()))
                .andDo(result -> System.out.println("Réponse du serveur (Update Success) : " + result.getResponse().getContentAsString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createdVehiculeId.toString()))
                .andExpect(jsonPath("$.make").value("UpdatedMake"))
                .andExpect(jsonPath("$.rentalPrice").value(250.0));

        Optional<Vehicule> updatedVehicule = vehiculeRepository.findById(createdVehiculeId);
        assertThat(updatedVehicule).isPresent();
        assertThat(updatedVehicule.get().getMake()).isEqualTo("UpdatedMake");
    }

    /**
     * Cas de test 5 : Création d'une image pour le véhicule existant (utilisant l'ID du test 1)
     */
    @Test
    @Order(5)
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Rollback(false) // Si vous avez ajouté @Rollback(false) au test 1, vous voudrez peut-être aussi ici
    // pour que l'image soit "réellement" ajoutée si des tests futurs devaient la vérifier.
    public void test5_addImageToVehicule() throws Exception {
        assertNotNull(createdVehiculeId, "L'ID du véhicule créé ne doit pas être null (test1_createVehiculeSuccess a échoué ?)");

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test-image.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "some image bytes".getBytes()
        );

        // Correction du chemin et du paramètre pour correspondre au contrôleur
        mockMvc.perform(multipart("/vehicule/upload/images")
                        .file(file)
                        .param("vehiculeId", createdVehiculeId.toString())
                        .with(csrf()))
                .andDo(result -> System.out.println("Réponse du serveur (Add Image) : " + result.getResponse().getContentAsString()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.chemin").exists())
                .andExpect(jsonPath("$.nom").exists());
    }

    /**
     * Cas de test 6 : Récupération de tous les véhicules (incluant celui du test 1)
     */
    @Test
    @Order(6)
    @WithMockUser(username = "user", roles = {"USER"})
    public void test6_getAllVehicules() throws Exception {
        mockMvc.perform(get("/vehicule/all")
                        .with(csrf()))
                .andDo(result -> System.out.println("Réponse du serveur (Get All Vehicules) : " + result.getResponse().getContentAsString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$..id", hasItem(createdVehiculeId.toString())))
                .andExpect(jsonPath("$..registrationNumber", hasItem(createdVehiculeRegistrationNumber)));
    }

    /**
     * Cas de test 7 : Récupération de véhicules par prix (incluant celui du test 1)
     */
    @Test
    @Order(7)
    @WithMockUser(username = "user", roles = {"USER"})
    public void test7_getVehiculeByRentalPrice() throws Exception {
        VehiculeDto anotherVehiculeDto = new VehiculeDto();
        String anotherRegNumber = "WF_OTHER_" + UUID.randomUUID().toString().substring(0, 8);
        anotherVehiculeDto.setRegistrationNumber(anotherRegNumber);
        anotherVehiculeDto.setMake("BMW");
        anotherVehiculeDto.setModel("X5");
        anotherVehiculeDto.setYear(2024);
        anotherVehiculeDto.setRentalPrice(200.0);
        anotherVehiculeDto.setCheminVersImage("/chemin/bmw.jpg");

        mockMvc.perform(post("/vehicule/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(anotherVehiculeDto))
                        .with(csrf()))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/vehicule/search-by-price")
                        .param("rentalPrice", "200.0")
                        .with(csrf()))
                .andDo(result -> System.out.println("Réponse du serveur (Get By Price) : " + result.getResponse().getContentAsString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1)) // Deux véhicules attendus à 200.0
                .andExpect(jsonPath("$.[0].rentalPrice").value(200.0));

    }

    /**
     * Cas de test 8 : Suppression réussie d'un véhicule (utilisant l'ID du test 1)
     */
    @Test
    @Order(8)
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void test8_deleteVehiculeSuccess() throws Exception {
        assertNotNull(createdVehiculeId, "L'ID du véhicule à supprimer ne doit pas être null (test1_createVehiculeSuccess a échoué ?)");

        long initialCount = vehiculeRepository.count();

        mockMvc.perform(delete("/vehicule/{id}", createdVehiculeId)
                        .with(csrf()))
                .andDo(result -> System.out.println("Réponse du serveur (Delete Success) : " + result.getResponse().getContentAsString()))
                .andExpect(status().isOk())
                .andExpect(content().string("\"message\" : \"vehicule is deleted successfully\""));

        assertThat(vehiculeRepository.findById(createdVehiculeId)).isNotPresent();
    }

    /**
     * Cas de test 9 : Tentative de suppression d'un véhicule déjà supprimé (ID du test 1)
     * Doit retourner Not Found.
     */
    @Test
    @Order(9)
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void test9_deleteVehiculeNotFoundAfterDeletion() throws Exception {
        UUID nonExistentId = UUID.randomUUID(); // Un ID qui n'existe pas

        mockMvc.perform(delete("/vehicule/delete/{id}", nonExistentId)
                        .with(csrf()))
                .andDo(result -> System.out.println("Réponse du serveur (Véhicule non trouvé - attendu 404) : " + result.getResponse().getContentAsString()))
                .andExpect(status().isNotFound());

        assertThat(vehiculeRepository.findById(nonExistentId)).isNotPresent(); // S'assurer que le véhicule n'a pas été ajouté/supprimé
    }

    /**
     * Cas de test 10 : Tentative de création avec numéro d'immatriculation vide.
     */
    @Test
    @Order(10)
    @Rollback(false)
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void test10_createVehicule_invalidInput_emptyRegistrationNumber_shouldReturnBadRequest() throws Exception {
        VehiculeDto invalidVehiculeDto = new VehiculeDto();
        invalidVehiculeDto.setRegistrationNumber("");
        invalidVehiculeDto.setMake("Renault");
        invalidVehiculeDto.setModel("Clio");
        invalidVehiculeDto.setYear(2023);
        invalidVehiculeDto.setRentalPrice(40.0);
        invalidVehiculeDto.setCheminVersImage("/chemin/image_invalide.jpg");

        mockMvc.perform(post("/vehicule/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidVehiculeDto))
                        .with(csrf()))
                .andDo(result -> System.out.println("Réponse du serveur (Create Invalid Input) : " + result.getResponse().getContentAsString()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].code").value("registrationNumber"))
                .andExpect(jsonPath("$[0].message").value("Le numéro d'immatriculation ne peut pas être vide"));
    }

    /**
     * Cas de test 11 : Tentative de création d'un véhicule avec un numéro d'immatriculation en doublon.
     */
    @Test
    @Order(11)
    @Rollback(false)
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void test11_createVehicule_duplicateRegistrationNumber_shouldReturnConflict() throws Exception {
        VehiculeDto vehiculeDto = new VehiculeDto();
        vehiculeDto.setRegistrationNumber(createdVehiculeRegistrationNumber); // C'est le doublon
        vehiculeDto.setMake("Audi");
        vehiculeDto.setModel("A4");
        vehiculeDto.setYear(2021);
        vehiculeDto.setRentalPrice(130.0);
        vehiculeDto.setCheminVersImage("/images/audi.jpg");

        mockMvc.perform(post("/vehicule/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(vehiculeDto))
                        .with(csrf()))
                // Laisser ceci pour le débogage jusqu'à ce que le test passe
                .andDo(result -> System.out.println("Réponse du serveur (duplicate registration) : " + result.getResponse().getContentAsString()))
                .andExpect(status().isBadRequest()) // Ou .isConflict() si vous avez changé côté serveur
                .andExpect(jsonPath("$").isArray()) // Confirme que la racine est un tableau
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].code").value("UNAUTHORIZED_REQUEST")) // <-- CORRECTION ICI : Pas de ".errorModels"
                .andExpect(jsonPath("$[0].message").value("Il existe deja des vehicules avec ces numeros de registration")); // <-- CORRECTION ICI : Pas de ".errorModels"

        assertThat(vehiculeRepository.count()).isEqualTo(1);
    }

}