package com.projet.testing.vehicule;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.projet.testing.vehicule.dto.VehiculeDto;
import com.projet.testing.vehicule.model.Vehicule;
import com.projet.testing.vehicule.repository.VehiculeRepository;
import org.junit.jupiter.api.*;
import com.aventstack.extentreports.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.transaction.annotation.Transactional; // Très important pour réinitialiser la BDD

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.assertj.core.api.Assertions.assertThat; // Pour des assertions plus idiomatiques

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@ActiveProfiles("test")
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS) // Chaque test est exécuté dans une transaction rollbackée à la fin
public class VehiculeControllerTest {

    @Autowired
    private WebApplicationContext context; // Contexte de l'application web

    private MockMvc mockMvc; // Objet pour simuler les requêtes HTTP

    @Autowired
    private ObjectMapper objectMapper; // Pour convertir les objets Java en JSON et vice-versa

    @Autowired
    private VehiculeRepository vehiculeRepository; // Le VRAI repository (pas un mock)
    
    private ExtentReports extent;
    private ExtentTest test;
    private static UUID createdVehiculeId;
    @BeforeAll
    void setupReport() {
        extent = ReportManager.createReport("VehiculeControllerTest");
        extent.setSystemInfo("Project", "Properlize projet of vehicule's location");
        extent.setSystemInfo("Tester", "Chenjo Prosper");
    }
    @BeforeEach
    void createTest(TestInfo info) {
        test = extent.createTest(info.getDisplayName());
    }

    @BeforeEach
    public void setup() {
        // Initialiser MockMvc avec le contexte de l'application et la sécurité Spring
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
        // @Transactional s'occupe du nettoyage de la base de données après chaque test,
        // donc pas besoin de supprimer manuellement les données ici.
    }

    // --- V1: Succès de création de véhicule ---
    @Test
    @Order(1)
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Test Succès de création de véhicule")
    public void testCreateVehiculeSuccess() throws Exception {
        VehiculeDto vehiculeDto = new VehiculeDto();
        // Générer un numéro d'immatriculation unique pour ce test
        String uniqueRegistrationNumber = "ABC_" + UUID.randomUUID().toString().substring(0, 8); // ex: ABC_1234abcd
        vehiculeDto.setRegistrationNumber(uniqueRegistrationNumber);
        vehiculeDto.setMake("Toyota");
        vehiculeDto.setModel("Corolla");
        vehiculeDto.setYear(2022);
        vehiculeDto.setRentalPrice(300.0);
        vehiculeDto.setCheminVersImage("/chemin/par/defaut/de/mon/image.jpg");

        mockMvc.perform(post("/vehicule/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(vehiculeDto))
                        .with(csrf()))
                .andDo(result -> System.out.println("Réponse du serveur (400 Bad Request) : " + result.getResponse().getContentAsString()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.registrationNumber").value(uniqueRegistrationNumber)) // <-- CORRECTION ICI : Utilisez uniqueRegistrationNumber
                .andExpect(jsonPath("$.id").exists());

        assertThat(vehiculeRepository.findByRegistrationNumber(uniqueRegistrationNumber)).isPresent(); // <-- CORRECTION ICI : Utilisez uniqueRegistrationNumber
    }

    // --- V2: Echec de création - numéro d'immatriculation vide ---
    @Test
    @Order(2)
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Echec de création - numéro d'immatriculation vide")
    public void createVehicule_invalidInput_emptyRegistrationNumber_shouldReturnBadRequest() throws Exception {
        VehiculeDto invalidVehiculeDto = new VehiculeDto();
        invalidVehiculeDto.setRegistrationNumber(""); // Numéro d'immatriculation vide pour provoquer l'erreur
        invalidVehiculeDto.setMake("Renault");
        invalidVehiculeDto.setModel("Clio");
        invalidVehiculeDto.setYear(2023);
        invalidVehiculeDto.setRentalPrice(40.0);
        invalidVehiculeDto.setCheminVersImage("/chemin/image_invalide.jpg"); // Ajoutez ce champ si obligatoire

        mockMvc.perform(post("/vehicule/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidVehiculeDto))
                        .with(csrf()))
                // Cette ligne est pour le débogage, gardez-la si le test échoue encore pour voir la réponse
                .andDo(result -> System.out.println("Réponse du serveur (createVehicule_invalidInput_emptyRegistrationNumber) : " + result.getResponse().getContentAsString()))
                .andExpect(status().isBadRequest()) // Attendre un statut HTTP 400 Bad Request
                .andExpect(jsonPath("$").isArray()) // La réponse est un tableau d'erreurs de validation
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].code").value("registrationNumber"))
                .andExpect(jsonPath("$[0].message").value("Le numéro d'immatriculation ne peut pas être vide"));

        //assertThat(vehiculeRepository.count()).isEqualTo(0); // S'assurer que rien n'a été persisté
    }

    @Test
    @Order(3)
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Echec de création - numéro d'immatriculation manquant")
    public void createVehicule_invalidInput_nullRegistrationNumber_shouldReturnBadRequest() throws Exception {
        VehiculeDto invalidVehiculeDto = new VehiculeDto();
        invalidVehiculeDto.setRegistrationNumber(null); // Numéro d'immatriculation null
        invalidVehiculeDto.setMake("Peugeot");
        invalidVehiculeDto.setModel("308");
        invalidVehiculeDto.setYear(2021);
        invalidVehiculeDto.setRentalPrice(55.0);

        mockMvc.perform(post("/vehicule/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidVehiculeDto))
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].code").value("registrationNumber"))
                .andExpect(jsonPath("$[0].message").value("Le numéro d'immatriculation ne peut pas être vide"));
        //assertThat(vehiculeRepository.count()).isEqualTo(0);
    }

    @Test
    @Order(4)
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Echec de création - marque manquante")
    public void createVehicule_invalidInput_nullMake_shouldReturnBadRequest() throws Exception {
        VehiculeDto invalidVehiculeDto = new VehiculeDto();
        invalidVehiculeDto.setRegistrationNumber("DEF456");
        invalidVehiculeDto.setMake(null); // Marque null
        invalidVehiculeDto.setModel("Corsa");
        invalidVehiculeDto.setYear(2020);
        invalidVehiculeDto.setRentalPrice(35.0);

        mockMvc.perform(post("/vehicule/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidVehiculeDto))
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].code").value("make"))
                .andExpect(jsonPath("$[0].message").value(" make is mandatory"));
        //assertThat(vehiculeRepository.count()).isEqualTo(0);
    }

    @Test
    @Order(6)
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Echec de création - Negatif prix de location")
    public void createVehicule_invalidInput_negativeRentalPrice_shouldReturnBadRequest() throws Exception {
        VehiculeDto invalidVehiculeDto = new VehiculeDto();
        invalidVehiculeDto.setRegistrationNumber("GHI789");
        invalidVehiculeDto.setMake("BMW");
        invalidVehiculeDto.setModel("X5");
        invalidVehiculeDto.setYear(2024);
        invalidVehiculeDto.setRentalPrice(-20.0); // Prix de location négatif

        mockMvc.perform(post("/vehicule/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidVehiculeDto))
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].code").value("rentalPrice"))
                .andExpect(jsonPath("$[0].message").value("le prix d'achat ne peut etre negatif"));
        //assertThat(vehiculeRepository.count()).isEqualTo(0);
    }

    @Test
    @Order(7)
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Echec de création - annee negative")
    public void createVehicule_invalidInput_negativeYear_shouldReturnBadRequest() throws Exception {
        VehiculeDto invalidVehiculeDto = new VehiculeDto();
        invalidVehiculeDto.setRegistrationNumber("YEARNEG");
        invalidVehiculeDto.setMake("Fiat");
        invalidVehiculeDto.setModel("Panda");
        invalidVehiculeDto.setYear(-2020); // Année négative
        invalidVehiculeDto.setRentalPrice(10.0);

        mockMvc.perform(post("/vehicule/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidVehiculeDto))
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$.[0].code").value("year"))
                .andExpect(jsonPath("$.[0].message").value("L'année ne peut etre negative")); // Message de @Positive
        //assertThat(vehiculeRepository.count()).isEqualTo(0);
    }

    // --- V3: Échec de création : numéro d'immatriculation en doublon ---
    // Dans VehiculeIntegrationTest.java, méthode createVehicule_duplicateRegistrationNumber_shouldReturnForbidden
    @Test
    @Order(8)
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Échec de création : numéro d'immatriculation en doublon")
    public void createVehicule_duplicateRegistrationNumber_shouldReturnForbidden() throws Exception {
        String baseRegNumber = "DUPLI_" + UUID.randomUUID().toString().substring(0, 8);
        Vehicule existingVehicule = new Vehicule();
        existingVehicule.setRegistrationNumber(baseRegNumber);
        existingVehicule.setMake("Audi");
        existingVehicule.setModel("A4");
        existingVehicule.setYear(2020);
        existingVehicule.setRentalPrice(120.0);
        existingVehicule.setCheminVersImage("/images/audi.jpg");
        vehiculeRepository.save(existingVehicule);

        VehiculeDto vehiculeDto = new VehiculeDto();
        vehiculeDto.setRegistrationNumber(baseRegNumber); // C'est le doublon
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
                .andExpect(status().isForbidden()) // Ou .isConflict() si vous avez changé côté serveur
                .andExpect(jsonPath("$").isArray()) // Confirme que la racine est un tableau
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].code").value("UNAUTHORIZED_REQUEST")) // <-- CORRECTION ICI : Pas de ".errorModels"
                .andExpect(jsonPath("$[0].message").value("Il existe deja des vehicules avec ces numeros de registration")); // <-- CORRECTION ICI : Pas de ".errorModels"

        //assertThat(vehiculeRepository.count()).isEqualTo(1);
    }

    // --- V4: Succès GET /vehicule/id/{id} ---
    @Test
    @Order(9)
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Succès GET /vehicule/id/{id}")
    public void getVehiculeById_success_shouldReturnVehicule() throws Exception {
        // Pré-condition: Sauvegarder un véhicule pour le récupérer
        Vehicule vehiculeToSave = new Vehicule();
        vehiculeToSave.setRegistrationNumber("GETID123");
        vehiculeToSave.setMake("Volkswagen");
        vehiculeToSave.setModel("Golf");
        vehiculeToSave.setYear(2018);
        vehiculeToSave.setRentalPrice(75.0);
        Vehicule savedVehicule = vehiculeRepository.save(vehiculeToSave); // Sauvegarde réelle en BDD

        mockMvc.perform(get("/vehicule/id/{id}", savedVehicule.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedVehicule.getId().toString()))
                .andExpect(jsonPath("$.registrationNumber").value("GETID123"));
    }

    // --- V5: Succès suppression /vehicule/{id} ---
    // Le contrôleur renvoie une chaîne de caractères directement, pas un JSON.
    @Test
    @Order(10)
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Succès suppression /vehicule/{id}")
    public void deleteVehicule_success_shouldReturnSuccessMessage() throws Exception {
        // Pré-condition: Sauvegarder un véhicule à supprimer
        Vehicule vehiculeToDelete = new Vehicule();
        vehiculeToDelete.setRegistrationNumber("DEL456");
        vehiculeToDelete.setMake("Ford");
        vehiculeToDelete.setYear(2015);
        vehiculeToDelete.setRentalPrice(50.0);
        Vehicule savedVehicule = vehiculeRepository.save(vehiculeToDelete);

        mockMvc.perform(delete("/vehicule/{id}", savedVehicule.getId())
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string("\"message\" : \"vehicule is deleted successfully\"")); // Vérifie la chaîne de caractères exacte
        assertThat(vehiculeRepository.findById(savedVehicule.getId())).isNotPresent(); // Vérifie la suppression en BDD
    }

    // --- V6: Échec suppression : id inconnu ---
    // Le contrôleur renvoie une chaîne de caractères directement, pas un JSON.
    @Test
    @Order(11)
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Échec suppression : id inconnu")
    public void deleteVehicule_notFound_shouldReturnInternalServerError() throws Exception {
        UUID nonExistentId = UUID.randomUUID(); // Un ID qui n'existe pas

        mockMvc.perform(delete("/vehicule/delete/{id}", nonExistentId)
                        .with(csrf()))
                .andDo(result -> System.out.println("Réponse du serveur (Véhicule non trouvé - attendu 404) : " + result.getResponse().getContentAsString()))
                .andExpect(status().isNotFound());

        assertThat(vehiculeRepository.findById(nonExistentId)).isNotPresent(); // S'assurer que le véhicule n'a pas été ajouté/supprimé
    }

    // --- V7: Échec récupération par ID inconnu (Gère la BusinessException de votre contrôleur) ---
    @Test
    @Order(12)
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Échec récupération par ID inconnu (Gère la BusinessException de votre contrôleur)")
    public void getVehiculeById_notFound_shouldReturnNotFound() throws Exception {
        UUID nonExistentId = UUID.randomUUID();

        mockMvc.perform(get("/vehicule/id/{id}", nonExistentId)
                        .with(csrf()))
                .andDo(result -> System.out.println("Réponse du serveur (Véhicule non trouvé - attendu 404) : " + result.getResponse().getContentAsString()))
                .andExpect(status().isNotFound());

    }

    // --- V8: Succès /vehicule/search-by-price ---
    @Test
    @Order(13)
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Succès /vehicule/search-by-price")
    public void getAllByPrice_success_shouldReturnFilteredList() throws Exception {
        // Pré-condition: Sauvegarder des véhicules avec différents prix
        vehiculeRepository.save(createVehiculeEntity("P1", "Make1", 50.0));
        vehiculeRepository.save(createVehiculeEntity("P2", "Make2", 100.0));
        vehiculeRepository.save(createVehiculeEntity("P3", "Make3", 50.0)); // Un autre à 50.0

        double searchPrice = 50.0;

        mockMvc.perform(get("/vehicule/search-by-price")
                        .param("rentalPrice", String.valueOf(searchPrice)) // Passe le paramètre de requête
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2)) // Deux véhicules attendus à 50.0
                .andExpect(jsonPath("$.[0].rentalPrice").value(50.0))
                .andExpect(jsonPath("$.[1].rentalPrice").value(50.0));
    }

    /**
     * Cas de test 5 : Création d'une image pour le véhicule existant (utilisant l'ID du test 1)
     */
   /* @Test
    @Order(5)
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Rollback(false) // Si vous avez ajouté @Rollback(false) au test 1, vous voudrez peut-être aussi ici
    // pour que l'image soit "réellement" ajoutée si des tests futurs devaient la vérifier.
    public void test5_addImageToVehicule() throws Exception {
        List<Vehicule> vehiculeList=vehiculeRepository.findAll();
        assertNotNull(createdVehiculeId=vehiculeList.getFirst().getId(), "L'ID du véhicule créé ne doit pas être null (test1_createVehiculeSuccess a échoué ?)");

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
    }*/

    // --- V9: Succès /vehicule/number/{registerNum} ---
    @Test
    @Order(14)
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Succès /vehicule/number/{registerNum}")
    public void getVehiculeByNumber_success_shouldReturnVehicule() throws Exception {
        // Pré-condition: Sauvegarder un véhicule
        Vehicule vehiculeToSave = new Vehicule();
        vehiculeToSave.setRegistrationNumber("NUMTEST123");
        vehiculeToSave.setMake("Mercedes");
        vehiculeToSave.setModel("C-Class");
        vehiculeToSave.setYear(2022);
        vehiculeToSave.setRentalPrice(200.0);
        vehiculeRepository.save(vehiculeToSave);

        mockMvc.perform(get("/vehicule/number/{registerNum}", "NUMTEST123")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.registrationNumber").value("NUMTEST123"))
                .andExpect(jsonPath("$.make").value("Mercedes"));
    }

    // --- V10: Échec /vehicule/number/{registerNum} : registre inconnu ---
    // Le contrôleur renvoie HttpStatus.NOT_FOUND (404) et une BusinessException.
    @Test
    @Order(15)
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Échec /vehicule/number/{registerNum}")
    public void getVehiculeByNumber_notFound_shouldReturnNotFound() throws Exception {
        String nonExistentRegistrationNum = "UNKNOWN404";

        mockMvc.perform(get("/vehicule/number/{registerNum}", nonExistentRegistrationNum)
                        .with(csrf()))
                .andDo(result -> System.out.println("Réponse du serveur (Véhicule non trouvé - attendu 404) : " + result.getResponse().getContentAsString()))
                .andExpect(status().isNotFound());    }

    // --- V11: Succès update /vehicule/{id} ---
    @Test
    @Order(16)
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("V11: Succès update /vehicule/{id}")
    public void updateVehicule_success_shouldReturnUpdatedVehicule() throws Exception {
        // Pré-condition: Sauvegarder un véhicule à mettre à jour
        Vehicule existingVehicule = new Vehicule();
        existingVehicule.setRegistrationNumber("OLDREG");
        existingVehicule.setMake("OldMake");
        existingVehicule.setModel("OldModel");
        existingVehicule.setYear(2010);
        existingVehicule.setRentalPrice(100.0);
        Vehicule savedVehicule = vehiculeRepository.save(existingVehicule); // L'ID réel est généré ici

        VehiculeDto updatedDto = new VehiculeDto();
        updatedDto.setRegistrationNumber("NEWREG"); // Champ mis à jour
        updatedDto.setMake("NewMake");
        updatedDto.setModel("NewModel");
        updatedDto.setYear(2023); // Année valide
        updatedDto.setRentalPrice(150.0); // Prix valide

        mockMvc.perform(put("/vehicule/{id}", savedVehicule.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedDto))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedVehicule.getId().toString()))
                .andExpect(jsonPath("$.registrationNumber").value("NEWREG"))
                .andExpect(jsonPath("$.make").value("NewMake"));

        // Vérification directe dans la base de données après la mise à jour
        Optional<Vehicule> foundVehicule = vehiculeRepository.findById(savedVehicule.getId());
        assertThat(foundVehicule).isPresent();
        assertThat(foundVehicule.get().getRegistrationNumber()).isEqualTo("NEWREG");
    }

    // --- V11 - Scénario d'échec : ID inexistant pour la mise à jour ---
    // Le contrôleur renvoie HttpStatus.NOT_FOUND (404) et une BusinessException.
    @Test
    @Order(17)
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("V10 - Scénario d'échec : ID inexistant pour la mise à jour")
    public void updateVehicule_notFound_shouldReturnNotFound() throws Exception {
        UUID nonExistentId = UUID.randomUUID();
        VehiculeDto updateDto = new VehiculeDto();
        updateDto.setRegistrationNumber("UPDATE_NONEXIST");
        updateDto.setMake("Test");
        updateDto.setRentalPrice(10.0);
        updateDto.setYear(2020); // Ajouter une année valide pour éviter les erreurs de validation du DTO en amont

        mockMvc.perform(put("/vehicule/{id}", nonExistentId)
        		.contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto))
                        .with(csrf()))
                .andDo(result -> System.out.println("Réponse du serveur (Véhicule non trouvé - attendu 404) : " + result.getResponse().getContentAsString()))
                .andExpect(status().isNotFound());    }

    // --- V11 - Scénario d'échec : Validation du DTO de mise à jour ---
    @Test
    @Order(18)
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("V11 - Scénario d'échec : Validation du DTO de mise à jour")
    public void updateVehicule_invalidInput_shouldReturnBadRequest() throws Exception {
        // Pré-condition: Sauvegarder un véhicule valide pour avoir un ID existant
        Vehicule existingVehicule = new Vehicule();
        existingVehicule.setRegistrationNumber("VALID_ID_FOR_UPDATE");
        existingVehicule.setMake("ValidMake");
        existingVehicule.setModel("ValidModel");
        existingVehicule.setYear(2020);
        existingVehicule.setRentalPrice(100.0);
        Vehicule savedVehicule = vehiculeRepository.save(existingVehicule);

        VehiculeDto invalidUpdateDto = new VehiculeDto();
        invalidUpdateDto.setRegistrationNumber("VALIDREG");
        invalidUpdateDto.setMake(null); // 'make' est null, ce qui est invalide selon @NotNull
        invalidUpdateDto.setRentalPrice(10.0);
        invalidUpdateDto.setYear(2020); // Ajouter une année valide

        mockMvc.perform(put("/vehicule/{id}", savedVehicule.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidUpdateDto))
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.[0].code").value("make"))
                .andExpect(jsonPath("$.[0].message").value(" make is mandatory"));

        // Vérifier que le véhicule existant n'a pas été modifié dans la BDD
        Optional<Vehicule> afterUpdate = vehiculeRepository.findById(savedVehicule.getId());
        assertThat(afterUpdate).isPresent();
        assertThat(afterUpdate.get().getMake()).isEqualTo("ValidMake");
    }

    // Méthode utilitaire pour créer une entité Vehicule pour les pré-conditions des tests
    private Vehicule createVehiculeEntity(String registrationNumber, String make, double rentalPrice) {
        Vehicule vehicule = new Vehicule();
        vehicule.setRegistrationNumber(registrationNumber);
        vehicule.setMake(make);
        vehicule.setModel("ModelX"); // Valeur par défaut
        vehicule.setYear(2020);     // Valeur par défaut
        vehicule.setRentalPrice(rentalPrice);
        return vehicule;
    }
    @AfterAll
    void tearDownReport() {
        extent.flush();
        ReportManager.generateIndexHtml();
    }
}
