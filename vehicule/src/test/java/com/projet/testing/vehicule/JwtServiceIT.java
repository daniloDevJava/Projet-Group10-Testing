package com.projet.testing.vehicule;


import com.projet.testing.vehicule.exception.BusinessException;
import com.projet.testing.vehicule.model.User;
import com.projet.testing.vehicule.repository.UserRepository;
import com.projet.testing.vehicule.service.JwtService;
import com.projet.testing.vehicule.service.ToKens;
import com.projet.testing.vehicule.util.JwtUtil;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;


import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class JwtServiceIT {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    private User user;

    @BeforeEach
    void setUp(){
        user=new User();
        user.setEmail("test@gmail.com");
        user.setUsername("dan");
        user.setMdp("NewPAss123");

        userRepository.save(user);
    }

    @Test
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
    void testRefreshAllTokens_Success(){
        try {
            //Arrange
            ToKens toKens=jwtService.generateTokens(user,0);
            String expectedMail="test@gmail.com";


            //Act
            ToKens newTokens=jwtService.refreshTokens(toKens.getRefreshToken(),15);
            String actualMail = jwtUtil.extractClaims(newTokens.getRefreshToken()).getSubject();

            //Assert
            assertEquals(expectedMail,actualMail);
            assertNotNull(newTokens.getAccessToken());

        }
        catch (BusinessException e){
            System.err.println(e.getMessage());
        }
    }

    @Test
    void testRefreshAccessToken_Success(){
        //Arrange
        ToKens toKens=jwtService.generateTokens(user,1);
        String expectedMail="test@gmail.com";
        Date expectedDateExpiration=jwtUtil.extractClaims(toKens.getAccessToken()).getExpiration();

        //Act
        String accesToken=jwtService.refreshAccessToken(toKens.getRefreshToken());
        String actualMail=jwtUtil.extractSubject(accesToken);
        //Date actualDateExpiration=jwtUtil.extractClaims(accesToken).getExpiration();

        //Assert
        //assertTrue(expectedDateExpiration.before(actualDateExpiration),(expectedDateExpiration +""+ actualDateExpiration));
        assertEquals(expectedMail,actualMail);


    }










}
