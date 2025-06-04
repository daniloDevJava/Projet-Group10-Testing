package com.projet.testing.vehicule;

import com.projet.testing.vehicule.util.PasswordValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PasswordValidatorTesting {

    private PasswordValidator validator;

    @BeforeEach
    void setUp() {
        validator = new PasswordValidator();
    }

    @Test
    void testEmptyPassword() {
        // 0 itération dans la boucle de caractères
        assertFalse(validator.isPasswordValid(""));
    }

    @Test
    void testOneCharPassword() {
        // 1 itération
        assertFalse(validator.isPasswordValid("a"));
    }

    @Test
    void testTwoCharPassword() {
        // 2 itérations
        assertFalse(validator.isPasswordValid("a1"));
    }

    @Test
    void testValidPassword() {
        // Mot de passe conforme : majuscule, minuscule, chiffre, longueur ≥ 8, aucun mot interdit
        assertTrue(validator.isPasswordValid("Secure123"));
    }

    @Test
    void testForbiddenWordAdmin() {
        // Contient "admin"
        assertFalse(validator.isPasswordValid("Admin123"));
    }

    @Test
    void testForbiddenWordPassword() {
        // Contient "password"
        assertFalse(validator.isPasswordValid("Password1"));
    }

    @Test
    void testForbiddenWord123456() {
        // Contient "123456"
        assertFalse(validator.isPasswordValid("My123456Pass"));
    }

    @Test
    void testNoUppercase() {
        // Manque majuscule
        assertFalse(validator.isPasswordValid("secure123"));
    }

    @Test
    void testNoLowercase() {
        // Manque minuscule
        assertFalse(validator.isPasswordValid("SECURE123"));
    }

    @Test
    void testNoDigit() {
        // Manque chiffre
        assertFalse(validator.isPasswordValid("SecurePwd"));
    }

    @Test
    void testJustLongEnoughValid() {
        // 8 caractères, juste assez long, valide
        assertTrue(validator.isPasswordValid("A1b2c3d4"));
    }

    @Test
    void testTooShortEvenIfValidPattern() {
        // Moins de 8 caractères, même si contenu valide
        assertFalse(validator.isPasswordValid("Ab1c2"));
    }
}
