package com.projet.testing.vehicule;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.projet.testing.vehicule.util.PasswordValidator;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PasswordValidatorTest {

    @Autowired
    private PasswordValidator validator;

    private ExtentReports extent;
    private ExtentTest test;

    @BeforeAll
    void setupReport() {
        extent = ReportManager.createReport("PasswordValidatorTest");
        extent.setSystemInfo("Project", "Properlize projet of vehicule's location");
        extent.setSystemInfo("Tester", "Chenjo Prosper");
    }
    @BeforeEach
    void setUp(TestInfo info) {
        test = extent.createTest(info.getDisplayName());
    }

    @Test
    @DisplayName("Test d'iteration 0 et echec de validation pour un mot de passe vide")
    void testEmptyPassword() {
        // 0 itération dans la boucle de caractères
        assertFalse(validator.isPasswordValid(""));
    }

    @Test
    @DisplayName("Test d'echec de validation pour une iteration pour un mot de passe d'un caractere")
    void testOneCharPassword() {
        // 1 itération
        assertFalse(validator.isPasswordValid("a"));
    }

    @Test
    @DisplayName("Test d'iteration 2 et echec pour un mot de passe avec 2 caracteres")
    void testTwoCharPassword() {
        // 2 itérations
        assertFalse(validator.isPasswordValid("a1"));
    }

    @Test
    @DisplayName("Test de validation ok pour un bon mot de passe")
    void testValidPassword() {
        // Mot de passe conforme : majuscule, minuscule, chiffre, longueur ≥ 8, aucun mot interdit
        assertTrue(validator.isPasswordValid("Secure123"));
    }

    @Test
    @DisplayName("Test de validation refusee pour un mot de passe contenant un mot non autorise(admin)")

    void testForbiddenWordAdmin() {
        // Contient "admin"
        assertFalse(validator.isPasswordValid("Admin123"));
    }

    @Test
    @DisplayName("Test de validation refusee pour un mot de passe contenant un mot non autorise(password)")

    void testForbiddenWordPassword() {
        // Contient "password"
        assertFalse(validator.isPasswordValid("Password1"));
    }

    @Test
    @DisplayName("Test de validation refusee pour un mot de passe contenant un mot non autorise(123456)")
    void testForbiddenWord123456() {
        // Contient "123456"
        assertFalse(validator.isPasswordValid("My123456Pass"));
    }

    @Test
    @DisplayName("Test de validation refusee pour un mor=t de passe sans majuscule")
    void testNoUppercase() {
        // Manque majuscule
        assertFalse(validator.isPasswordValid("secure123"));
    }

    @Test
    @DisplayName("Test de validation refusee pour um mot de passe sans minuscule")
    void testNoLowercase() {
        // Manque minuscule
        assertFalse(validator.isPasswordValid("SECURE123"));
    }

    @Test
    @DisplayName("Tes de validation refusee a cause de'um mot de passe sans chiffre")
    void testNoDigit() {
        // Manque chiffre
        assertFalse(validator.isPasswordValid("SecurePwd"));
    }

    @Test
    @DisplayName("Test de validation ok de mot de passe ")
    void testJustLongEnoughValid() {
        // 8 caractères, juste assez long, valide
        assertTrue(validator.isPasswordValid("A1b2c3d4"));
    }

    @Test
    @DisplayName("Test d'echec de validation du mot de passe pour un mdp court")
    void testTooShortEvenIfValidPattern() {
        // Moins de 8 caractères, même si contenu valide
        assertFalse(validator.isPasswordValid("Ab1c2"));
    }

    @AfterAll
    void tearDownReport() {
        extent.flush();
        ReportManager.generateIndexHtml();
    }
}
