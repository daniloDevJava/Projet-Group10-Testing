package com.projet.testing.vehicule;


import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.projet.testing.vehicule.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.file.Paths;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class JwtUtilTest {

    @Spy
    @InjectMocks
    private JwtUtil jwtUtil;

    private final String email = "test@example.com";


    private ExtentReports extent;
    private ExtentTest test;

    @BeforeAll
    void setupReport() {
        extent = ReportManager.createReport("JwtUtileTest");
        extent.setSystemInfo("Project", "Properlize projet of vehicule's location");
        extent.setSystemInfo("Tester", "DAN YVES BRICE AYOMBA II");
    }

    @BeforeEach
    void createTest(TestInfo info) {
        test = extent.createTest(info.getDisplayName());
    }

    @Test
    @DisplayName("Générer un AccessToken valide pour un utilisateur")
    @Tag("generateToken")
    void testGenerateAccessToken() {
        // Act
        String token = jwtUtil.generateAccessToken(email);

        // Assert
        assertNotNull(token);
        assertTrue(jwtUtil.isTokenValid(token));
        assertEquals(email, jwtUtil.extractSubject(token));
    }

    @Test
    @DisplayName("Générer un RefreshToken valide pour un utilisateur")
    @Tag("generateToken")
    void testGenerateRefreshToken() {
        // Act
        String token = jwtUtil.generateRefreshToken(email, 3600);

        // Assert
        assertNotNull(token);
        assertTrue(jwtUtil.isTokenValid(token));
        assertEquals(email, jwtUtil.extractSubject(token));
    }

    @Test
    @DisplayName("Extraire les claims d'un token")
    @Tag("extractClaims")
    void testExtractClaims() {
        // Arrange
        String token = jwtUtil.generateAccessToken(email);

        // Act
        Claims claims = jwtUtil.extractClaims(token);

        // Assert
        assertNotNull(claims);
        assertEquals(email, claims.getSubject());
    }

    @Test
    @DisplayName("Vérifier un token valide")
    @Tag("validateToken")
    void testIsTokenValid_ValidToken() {
        // Arrange
        String token = jwtUtil.generateAccessToken(email);

        // Act
        boolean isValid = jwtUtil.isTokenValid(token);

        // Assert
        assertTrue(isValid);
    }

    @Test
    @DisplayName("Vérifier un token invalide")
    @Tag("validateToken")
    void testIsTokenValid_InvalidToken() {
        // Arrange
        String invalidToken = "invalid.token";

        // Act
        boolean isValid = jwtUtil.isTokenValid(invalidToken);

        // Assert
        assertFalse(isValid);
    }

    @Test
    @DisplayName("Extraire le sujet d'un token")
    @Tag("extractSubject")
    void testExtractSubject() {
        // Arrange
        String token = jwtUtil.generateAccessToken(email);

        // Act
        String subject = jwtUtil.extractSubject(token);

        // Assert
        assertEquals(email, subject);
    }



    @Test
    @DisplayName("Vérifier qu'un token n'est pas expiré")
    @Tag("checkExpiration")
    void testIsTokenExpired_NotExpired() {
        // Arrange
        String token = jwtUtil.generateAccessToken(email);

        // Act
        boolean isExpired = jwtUtil.isTokenExpired(token);

        // Assert
        assertFalse(isExpired);
    }

    /*@Test
    @DisplayName("Vérifier qu'un token est expiré")
    @Tag("checkExpiration")
    void testIsTokenExpired_Expired() {
        // Arrange
        String token = jwtUtil.generateAccessToken(email);

        String expiredToken = Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date(now.getTime() - 10000)) // Issued in the past
                .setExpiration(new Date(now.getTime() - 5000)) // Expired in the past
                .signWith(jwtUtil.SECRET_KEY)
                .compact();

        // Act
        boolean isExpired = jwtUtil.isTokenExpired(expiredToken);

        // Assert
        assertTrue(isExpired);
    }*/
    @AfterAll
    void tearDownReport() {
        extent.flush();
        ReportManager.generateIndexHtml();
    }
}
