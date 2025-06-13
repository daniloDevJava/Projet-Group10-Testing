package com.projet.testing.vehicule;



import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.projet.testing.vehicule.exception.BusinessException;
import com.projet.testing.vehicule.model.RefreshToken;
import com.projet.testing.vehicule.model.User;
import com.projet.testing.vehicule.repository.RefreshTokenRepository;
import com.projet.testing.vehicule.repository.UserRepository;
import com.projet.testing.vehicule.service.JwtService;
import com.projet.testing.vehicule.service.ToKens;
import com.projet.testing.vehicule.util.JwtUtil;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;



import java.util.List;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;


@SpringBootTest
@ActiveProfiles("test")
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class JwtServiceIT {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepository userRepository;

    @SpyBean
    private JwtUtil jwtUtil;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    
    private ExtentReports extent;
    private ExtentTest test;

    private User user;
    
    @BeforeAll
    void setupTest() {
        extent = ReportManager.createReport("JwtServiceTest");
        extent.setSystemInfo("Project", "Properlize projet of vehicule's location");
        extent.setSystemInfo("Tester", "DAN YVES BRICE AYOMBA II");
    }

    @BeforeEach
    void setUp(){
        user=new User();
        user.setEmail("test@gmail.com");
        user.setUsername("dan");
        user.setMdp("NewPAss123");

        userRepository.save(user);
    }
    
    @BeforeEach
    void createTest(TestInfo info) {
        test = extent.createTest(info.getDisplayName());
    }

    @Test
    @DisplayName("Test des generations de tokens")
    void testGenerateTokens_Success(){
        //Arrange
        long ValidityTimeRefreshToken=15;
        String expectedMail ="test@gmail.com";

        //Act
        ToKens toKens=jwtService.generateTokens(user,ValidityTimeRefreshToken);
        String actualMail = jwtUtil.extractClaims(toKens.getRefreshToken()).getSubject();

        //Assert
        assertEquals(expectedMail, actualMail);
        assertNotNull(toKens.getAccessToken());

    }

    @Test
    @DisplayName("Test  d'echec de raffraichissement de l'access token avec un token inconnu")
    void testRefreshAccessToken_ThrowsBusinessExceptionWithRefreshTokenNotFound(){
        //Arrange
        String refreshToken= jwtUtil.generateRefreshToken("test@gmail.com",15);

        //Act
        BusinessException exception=assertThrows(BusinessException.class , () ->{
            jwtService.refreshAccessToken(refreshToken);
        });

        //Assert
        assertEquals("refresh token inconnu",exception.getErrorModels().getFirst().getMessage());
    }

    @Test
    @DisplayName("Test d'echec de raffraichissement des tokens avec un token inconnu")
    void testRefreshTokens_WithRefreshTokenNotFound(){
        //Arrange
        String refreshToken= jwtUtil.generateRefreshToken("test@gmail.com",15);

        //Act
        BusinessException exception=assertThrows(BusinessException.class , () ->{
            jwtService.refreshTokens(refreshToken,15);
        });

        //Assert
        assertEquals("refresh token inconnu",exception.getErrorModels().getFirst().getMessage());
    }


    @Test
    @DisplayName("Test de rafraichissement de tokens")
    void testRefreshAllTokens_Success(){

            //Arrange
            ToKens toKens=jwtService.generateTokens(user,15);
            String expectedMail="test@gmail.com";


            //Act
            doReturn(true).when(jwtUtil).isTokenExpired(toKens.getRefreshToken());
            ToKens newTokens=jwtService.refreshTokens(toKens.getRefreshToken(),15);
            String actualMail = jwtUtil.extractClaims(newTokens.getRefreshToken()).getSubject();

            //Assert
            assertEquals(expectedMail,actualMail);
            assertNotNull(newTokens.getAccessToken());

    }

    @Test
    @DisplayName("test de rafraichissement de l'access token")
    void testRefreshAccessToken_Success(){
        //Arrange
        ToKens toKens=jwtService.generateTokens(user,15);
        String expectedMail="test@gmail.com";
        //Date expectedDateExpiration=jwtUtil.extractClaims(toKens.getAccessToken()).getExpiration();

        //Act
        String accesToken=jwtService.refreshAccessToken(toKens.getRefreshToken());
        String actualMail=jwtUtil.extractSubject(accesToken);
        //Date actualDateExpiration=jwtUtil.extractClaims(accesToken).getExpiration();

        //Assert
        //assertTrue(expectedDateExpiration.before(actualDateExpiration),(expectedDateExpiration +""+ actualDateExpiration));
        assertEquals(expectedMail,actualMail);


    }
    @Test
    @DisplayName("Test d'echec de raffraichissement des tokens avec un token expire")
    void testRefreshTokens_WithRefreshTokenNotExpired(){
        //Arrange
        jwtService.generateTokens(user,15);
        List<RefreshToken> refreshTokenList=refreshTokenRepository.findAll();
        RefreshToken refreshToken=refreshTokenList.getFirst();
        String refreshTokenValue=refreshToken.getValeur();

        //Act
        BusinessException exception=assertThrows(BusinessException.class , () ->{
            jwtService.refreshTokens(refreshTokenValue,15);
        });

        //Assert
        assertEquals("refresh token pas encore expiré",exception.getErrorModels().getFirst().getMessage());

    }
    
    @AfterAll
    void tearDownReport() {
        extent.flush();
        ReportManager.generateIndexHtml();
    }



}
