package com.projet.testing.vehicule;

import com.projet.testing.vehicule.dto.ImagesDto;
import com.projet.testing.vehicule.dto.VehiculeDto;
import com.projet.testing.vehicule.exception.DuplicateRegistrationNumberException;
import com.projet.testing.vehicule.model.Vehicule;
import com.projet.testing.vehicule.repository.VehiculeRepository;
import com.projet.testing.vehicule.service.VehiculeService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class VehiculeServiceTest {

    @Autowired
    private VehiculeService vehiculeService;

    @Autowired
    private VehiculeRepository vehiculeRepository;

    // Variables statiques pour stocker les données créées et les réutiliser
    private static UUID createdVehiculeId;
    private static String createdVehiculeRegistrationNumber;
    private static String createdVehiculeMake = "WorkflowMake";
    private static double createdVehiculeRentalPrice = 150.0;


    /**
     * Cas de test 1 : Création réussie d'un véhicule
     * Crée un véhicule et stocke son ID et numéro d'immatriculation pour les tests suivants.
     */
    @Test
    @Order(1)
    @Rollback(false)
    @DisplayName("Creation d'un vehicule avec succes")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void test1_createVehiculeSuccess() {
        VehiculeDto vehiculeDto = new VehiculeDto();
        createdVehiculeRegistrationNumber = "WF_" + UUID.randomUUID().toString().substring(0, 8);
        vehiculeDto.setRegistrationNumber(createdVehiculeRegistrationNumber);
        vehiculeDto.setMake(createdVehiculeMake);
        vehiculeDto.setModel("Corolla");
        vehiculeDto.setYear(2022);
        vehiculeDto.setRentalPrice(createdVehiculeRentalPrice);
        vehiculeDto.setCheminVersImage("/chemin/par/defaut/de/mon/image.jpg");

        // Call the service directly
        VehiculeDto createdDto = vehiculeService.createVehicule(vehiculeDto);

        assertNotNull(createdDto.getId(), "L'ID du véhicule créé ne doit pas être null");
        assertThat(createdDto.getRegistrationNumber()).isEqualTo(createdVehiculeRegistrationNumber);
        assertThat(createdDto.getMake()).isEqualTo(createdVehiculeMake);

        createdVehiculeId = createdDto.getId();

        // Verify with the repository
        Optional<Vehicule> persistedVehicule = vehiculeRepository.findByRegistrationNumber(createdVehiculeRegistrationNumber);
        assertThat(persistedVehicule).isPresent();
        assertThat(persistedVehicule.get().getId()).isEqualTo(createdVehiculeId);
        assertThat(persistedVehicule.get().getRegistrationNumber()).isEqualTo(createdVehiculeRegistrationNumber);

        System.out.println("Vehicule créé : ID=" + createdVehiculeId + ", RegistrationNumber=" + createdVehiculeRegistrationNumber);
    }

    /**
     * Cas de test 2 : Récupération d'un véhicule par ID (utilisant l'ID du test 1)
     */
    @Test
    @Order(2)
    @DisplayName("recuperation d'un vehicule par l'id avec succes")
    @WithMockUser(username = "user", roles = {"USER"})
    public void test2_getVehiculeByIdSuccess() {
        assertNotNull(createdVehiculeId, "L'ID du véhicule créé ne doit pas être null (test1_createVehiculeSuccess a échoué ?)");

        // Call the service directly
        VehiculeDto foundDto = vehiculeService.getVehiculeById(createdVehiculeId);

        assertNotNull(foundDto, "Le véhicule devrait être trouvé.");
        assertThat(foundDto.getId()).isEqualTo(createdVehiculeId);
        assertThat(foundDto.getRegistrationNumber()).isEqualTo(createdVehiculeRegistrationNumber);
        assertThat(foundDto.getMake()).isEqualTo(createdVehiculeMake);
        System.out.println("Vehicule récupéré par ID : " + foundDto);
    }

    /**
     * Cas de test 3 : Récupération d'un véhicule par numéro d'immatriculation (utilisant le numéro du test 1)
     */
    @Test
    @Order(3)
    @DisplayName("recuperation d'un vehicule par number avec succes")
    @WithMockUser(username = "user", roles = {"USER"})
    public void test3_getVehiculeByNumberSuccess() {
        assertNotNull(createdVehiculeRegistrationNumber, "Le numéro d'immatriculation ne doit pas être null (test1_createVehiculeSuccess a échoué ?)");

        // Call the service directly
        VehiculeDto foundDto = vehiculeService.getVehiculeByNumber(createdVehiculeRegistrationNumber);

        assertNotNull(foundDto, "Le véhicule devrait être trouvé.");
        assertThat(foundDto.getId()).isEqualTo(createdVehiculeId);
        assertThat(foundDto.getRegistrationNumber()).isEqualTo(createdVehiculeRegistrationNumber);
        assertThat(foundDto.getMake()).isEqualTo(createdVehiculeMake);
        System.out.println("Vehicule récupéré par numéro d'immatriculation : " + foundDto);
    }

    /**
     * Cas de test 4 : Mise à jour d'un véhicule existant (utilisant l'ID du test 1)
     */
    @Test
    @Order(4)
    @Rollback(false)
    @DisplayName("Mise a jour d'un vehicule avec succes")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void test4_updateVehiculeSuccess() {
        assertNotNull(createdVehiculeId, "L'ID du véhicule créé ne doit pas être null (test1_createVehiculeSuccess a échoué ?)");

        VehiculeDto updatedVehiculeDto = new VehiculeDto();
        updatedVehiculeDto.setRegistrationNumber(createdVehiculeRegistrationNumber);
        updatedVehiculeDto.setMake("UpdatedMake");
        updatedVehiculeDto.setModel("UpdatedModel");
        updatedVehiculeDto.setYear(2023);
        updatedVehiculeDto.setRentalPrice(250.0);
        updatedVehiculeDto.setCheminVersImage("/chemin/updated/image.jpg");

        // Call the service directly
        VehiculeDto resultDto = vehiculeService.updateVehicule(updatedVehiculeDto, createdVehiculeId);

        assertNotNull(resultDto, "Le véhicule mis à jour ne devrait pas être null.");
        assertThat(resultDto.getId()).isEqualTo(createdVehiculeId);
        assertThat(resultDto.getMake()).isEqualTo("UpdatedMake");
        assertThat(resultDto.getRentalPrice()).isEqualTo(250.0);

        // Verify with the repository
        Optional<Vehicule> updatedVehicule = vehiculeRepository.findById(createdVehiculeId);
        assertThat(updatedVehicule).isPresent();
        assertThat(updatedVehicule.get().getMake()).isEqualTo("UpdatedMake");
        assertThat(updatedVehicule.get().getRentalPrice()).isEqualTo(250.0);
        System.out.println("Vehicule mis à jour : " + resultDto);
    }

    /**
     * Cas de test 5 : Création d'une image pour le véhicule existant (utilisant l'ID du test 1)
     */
    @Test
    @Order(5)
    @DisplayName("ajout d'une image a un vehicule avec succes")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Rollback(false)
    public void test5_addImageToVehicule() throws IOException {
        assertNotNull(createdVehiculeId, "L'ID du véhicule créé ne doit pas être null (test1_createVehiculeSuccess a échoué ?)");

        byte[] fileContent = "some image bytes for testing".getBytes();
        MockMultipartFile mockFile = new MockMultipartFile(
                "file",
                "test-image.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                fileContent
        );

        // Call the service directly
        ImagesDto imageResult = vehiculeService.addImage(mockFile, createdVehiculeId);

        assertNotNull(imageResult, "Le résultat de l'ajout d'image ne devrait pas être null.");
        assertNotNull(imageResult.getId());
        assertNotNull(imageResult.getChemin());
        assertThat(imageResult.getNom()).isEqualTo("IMG0"+imageResult.getId());
        System.out.println("Image ajoutée au véhicule : " + imageResult);
    }

    /**
     * Cas de test 6 : Récupération de tous les véhicules (incluant celui du test 1)
     */
    @Test
    @Order(6)
    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("recuperer tout les vehicles avec succes")
    public void test6_getAllVehicule() {
        // Call the service directly
        List<VehiculeDto> allVehicules = vehiculeService.getAllVehicule();

        assertThat(allVehicules).isNotEmpty();
        assertThat(allVehicules.size()).isGreaterThanOrEqualTo(1);
        assertThat(allVehicules).anyMatch(v -> v.getId().equals(createdVehiculeId));
        assertThat(allVehicules).anyMatch(v -> v.getRegistrationNumber().equals(createdVehiculeRegistrationNumber));
        System.out.println("Nombre total de véhicules : " + allVehicules.size());
    }

    /**
     * Cas de test 7 : Récupération de véhicules par prix (incluant celui du test 1)
     */
    @Test
    @Order(7)
    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("recuperer les vehicules par prix")
    public void test7_getVehicule() {
        // Create an additional vehicle for this test's scenario
        VehiculeDto anotherVehiculeDto = new VehiculeDto();
        String anotherRegNumber = "WF_OTHER_" + UUID.randomUUID().toString().substring(0, 8);
        anotherVehiculeDto.setRegistrationNumber(anotherRegNumber);
        anotherVehiculeDto.setMake("BMW");
        anotherVehiculeDto.setModel("X5");
        anotherVehiculeDto.setYear(2024);
        anotherVehiculeDto.setRentalPrice(200.0);
        anotherVehiculeDto.setCheminVersImage("/chemin/bmw.jpg");
        vehiculeService.createVehicule(anotherVehiculeDto);

        // Call the service directly
        List<VehiculeDto> vehiculesByPrice = vehiculeService.getVehicule(200.0);

        assertThat(vehiculesByPrice).isNotEmpty();
        assertThat(vehiculesByPrice.size()).isEqualTo(1);
        assertThat(vehiculesByPrice.get(0).getRentalPrice()).isEqualTo(200.0);
        System.out.println("Véhicules récupérés par prix 200.0 : " + vehiculesByPrice.size());
    }

    /**
     * Cas de test 8 : Suppression réussie d'un véhicule (utilisant l'ID du test 1)
     */
    @Test
    @Order(8)
    @DisplayName("suppression d'un vehicule par l'id avec succes")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void test8_deleteVehiculeSuccess() {
        assertNotNull(createdVehiculeId, "L'ID du véhicule à supprimer ne doit pas être null (test1_createVehiculeSuccess a échoué ?)");

        // Call the service directly
        boolean isDeleted = vehiculeService.deleteVehicule(createdVehiculeId);
        assertThat(isDeleted).isTrue();

        // Verify with the repository
        assertThat(vehiculeRepository.findById(createdVehiculeId)).isNotPresent();
        System.out.println("Vehicule avec ID " + createdVehiculeId + " supprimé.");
    }

    /**
     * Cas de test 9 : Tentative de suppression d'un véhicule déjà supprimé ou inexistant.
     * Le service est supposé retourner 'false' si le véhicule n'est pas trouvé.
     */
    @Test
    @Order(9)
    @DisplayName("suppression d'un vehicule inexistant")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void test9_deleteVehiculeNotFoundAfterDeletion() {
        UUID nonExistentId = UUID.randomUUID();

        // S'attend à une RuntimeException et vérifie son message
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            vehiculeService.deleteVehicule(nonExistentId);
        }, "Should throw RuntimeException for non-existent ID");

        assertThat(thrown.getMessage()).isEqualTo("Véhicule non trouvé");
        System.out.println("Tentative de suppression d'un véhicule inexistant (ID: " + nonExistentId + ") a échoué comme prévu (exception lancée : " + thrown.getMessage() + ").");
    }

    /**
     * Cas de test 10 : Tentative de création avec numéro d'immatriculation vide.
     * Le service est supposé ne pas créer le véhicule invalide.
     */
    @Test
    @Order(10)
    @DisplayName("creation d'un vehicule avec numero d'immatriculation absent")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void test10_createVehicule_invalidInput_emptyRegistrationNumber_shouldNotCreate() {
        VehiculeDto invalidVehiculeDto = new VehiculeDto();
        invalidVehiculeDto.setRegistrationNumber(""); // Empty registration number
        invalidVehiculeDto.setMake("Renault");
        invalidVehiculeDto.setModel("Clio");
        invalidVehiculeDto.setYear(2023);
        invalidVehiculeDto.setRentalPrice(40.0);
        invalidVehiculeDto.setCheminVersImage("/chemin/image_invalide.jpg");

        // Appelle le service directement
        VehiculeDto createdDto = vehiculeService.createVehicule(invalidVehiculeDto);

        // Vérifie que la création a échoué (par exemple, en retournant null)
        assertNull(createdDto, "Le DTO créé devrait être null pour une entrée invalide.");

        // Vérifie qu'aucun véhicule avec un numéro d'immatriculation vide n'a été persisté
        assertThat(vehiculeRepository.findByRegistrationNumber("")).isNotPresent();
        System.out.println("Tentative de création avec numéro d'immatriculation vide a échoué comme prévu (non persistant).");
    }

    /**
     * Cas de test 11 : Tentative de création d'un véhicule avec un numéro d'immatriculation en doublon.
     * Le service est supposé ne pas créer le véhicule en doublon.
     */
    @Test
    @Order(11)
    @DisplayName("creation d'un vehicule avec un numero d'immatriculation existant")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void test11_createVehicule_duplicateRegistrationNumber_shouldThrowException() {
        // First, create a vehicle to ensure a duplicate exists for the test
        VehiculeDto existingVehicule = new VehiculeDto();
        String tempRegNumber = "DUP_TEST_" + UUID.randomUUID().toString().substring(0, 8);
        existingVehicule.setRegistrationNumber(tempRegNumber);
        existingVehicule.setMake("TestMake");
        existingVehicule.setModel("TestModel");
        existingVehicule.setYear(2020);
        existingVehicule.setRentalPrice(100.0);
        existingVehicule.setCheminVersImage("/path/test.jpg");
        vehiculeService.createVehicule(existingVehicule); // Create the first one

        VehiculeDto duplicateVehiculeDto = new VehiculeDto();
        duplicateVehiculeDto.setRegistrationNumber(tempRegNumber); // This is the duplicate
        duplicateVehiculeDto.setMake("Audi");
        duplicateVehiculeDto.setModel("A4");
        duplicateVehiculeDto.setYear(2021);
        duplicateVehiculeDto.setRentalPrice(130.0);
        duplicateVehiculeDto.setCheminVersImage("/images/audi.jpg");

        // Expect IllegalArgumentException
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            vehiculeService.createVehicule(duplicateVehiculeDto);
        });

        assertThat(thrown.getMessage()).contains("Il existe deja des vehicules avec ces numeros de registration");
        System.out.println("Tentative de création avec numéro d'immatriculation en doublon a échoué comme prévu : " + thrown.getMessage());
    }
}
