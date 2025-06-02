package com.projet.testing.vehicule;

import com.projet.testing.vehicule.controller.UserController;
import com.projet.testing.vehicule.dto.UserDto;
import com.projet.testing.vehicule.exception.BusinessException;
import com.projet.testing.vehicule.exception.ErrorModel;
import com.projet.testing.vehicule.service.JwtService;
import com.projet.testing.vehicule.util.ChangePasswordRequest;
import com.projet.testing.vehicule.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.projet.testing.vehicule.service.UserService;
import com.projet.testing.vehicule.repository.*;
import org.junit.jupiter.api.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UserControllerTest {

    @Autowired
    private  UserService userService;
    @Autowired
    private  MockMvc mockMvc;
    @Autowired
    private  JwtService jwtService;
    @Autowired
    private  JwtUtil jwtUtil;
   
    

    private final ObjectMapper objectMapper = new ObjectMapper();
    
   



   /* @TestConfiguration
    static class MockConfig {

        @Bean
        JwtUtil jwtUtil() {
            return mock(JwtUtil.class);
        }

        @Bean
        UserService userService(){return mock(UserService.class);}

        @Bean
        JwtService jwtService(){return mock(JwtService.class);}

        @Bean
        UserController userController(UserService userService, JwtService jwtService,JwtUtil jwtUtil) {
            return new UserController(userService,jwtService,jwtUtil);
        }
    }*/

    @Test
    @DisplayName("U1 :Doit retourner un statut 201 et un objet json correct")
    public void testAddUser_Success() throws Exception {

        //Arrange
        UserDto user = new UserDto();
        user.setEmail("test@example.com");
        user.setMdp("StrongPass123");
        user.setName("John");




        //Act 2 & Assert
        mockMvc.perform(post("http://localhost:9000/users/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.name").value("JOHN"));
    }



    @Test
    @DisplayName("U2 : ")
    public void testAddUser_WeakPassword() throws Exception {

        UserDto user = new UserDto();
        user.setEmail("testUser@example.com");
        user.setMdp("123");
        user.setName("John");

        mockMvc.perform(post("http://localhost:9000/users/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.[0]").exists())
                .andExpect(jsonPath("$.[0].code").value("mdp"));;
    }



    @Test
    @DisplayName("U3") void testAddUser_InvalidEmail() throws Exception {

        //Act
        UserDto user = new UserDto();
        user.setEmail("marie");
        user.setName("Johny");

        UserDto user1=new UserDto();
        user1.setEmail("yutttttttttttttteiiiiiiiiiiiiiiyttttttttttttttttttttttttttttttdddddddddddddddddddddddd");
        user1.setName("hiruzen");
        user1.setMdp("yooke");

        //Act & Assert
        mockMvc.perform(post("http://localhost:9000/users/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0]").exists());
      /*  mockMvc.perform(post("http://localhost:9000/users/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user1)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.[0].code").value("email"));*/

    }



    @Test
    @DisplayName("U4 - Connexion réussie après ajout d'utilisateur")
    void testLogin_Success() throws Exception {
        // Création d’un utilisateur de test
        UserDto request = new UserDto();
        request.setEmail("test78@example.com");
        request.setMdp("StrongPass123");
        request.setName("Dan");

        // Ajout de l’utilisateur
        mockMvc.perform(post("http://localhost:9000/users/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
               .andExpect(status().isCreated());

        // Connexion avec cet utilisateur
        mockMvc.perform(post("http://localhost:9000/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.accessToken").exists())
               .andExpect(jsonPath("$.refreshToken").exists());
    }

    /*@Test @DisplayName("U5") void testLogin_InvalidCredentials() throws Exception {
        UserDto request = new UserDto();
        request.setMdp("wrongpass");
        request.setEmail("");
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test @DisplayName("U6") void testRefreshToken_Success() throws Exception {
        mockMvc.perform(post("/refresh-access-tokens")
                        .param("refreshToken","dllll-08-d05d-477zerktuimentkfles-b01")
                        .header("Authorization", "Bearer valid-refresh-token"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.hasLength(50)));
    }

    @Test @DisplayName("U7") void testRefreshToken_InvalidToken() throws Exception {
        mockMvc.perform(post("/refresh-access-tokens")
                        .header("Authorization", "Bearer invalid-token"))
                .andExpect(status().isForbidden())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Le refresh token est invalide")));
    }

    @Test @DisplayName("U8") void testRefreshToken_ExpiredToken() throws Exception {
        mockMvc.perform(post("/refresh-access-tokens")
                        .header("Authorization", "Bearer expired-token"))
                .andExpect(status().isForbidden())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Le refresh token est expiré")));
    }

    @Test @DisplayName("U9") void testRefreshTokens_Success() throws Exception {
        mockMvc.perform(post("/refresh-tokens")
                        .param("refreshToken","dkkkkkkkkkkflleeksld-00fffmfkdkdmdmsdmdnnd")
                        .header("Authorization", "Bearer valid-refresh-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").exists())
                .andExpect(jsonPath("$.refreshToken").exists());
    }

    @Test @DisplayName("U10") void testRefreshTokens_Expired() throws Exception {
        mockMvc.perform(post("/refresh-tokens")
                        .header("Authorization", "Bearer expired-token"))
                .andExpect(status().isForbidden());
    }

    @Test @DisplayName("U11") void testRefreshTokens_Invalid() throws Exception {
        mockMvc.perform(post("/refresh-tokens")
                        .header("Authorization", "Bearer invalid-token"))
                .andExpect(status().isForbidden());
    }

    @Test @DisplayName("U12") void testUpdateUser_Success() throws Exception {
        UserDto updated = new UserDto();
        updated.setEmail("");
        mockMvc.perform(put("/users/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("updated@example.com"))
                .andExpect(jsonPath("$.name").value("JULIE"));
    }

   /* @Test @DisplayName("U13") void testUpdateUser_InvalidEmail() throws Exception {
        UserDto updated = new UserDto("bademail", "Julie", "StrongPass123!");
        mockMvc.perform(put("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isBadRequest());
    }

    @Test @DisplayName("U14") void testUpdateUser_NotFound() throws Exception {
        UserDto updated = new UserDto("notfound@example.com", "Julie", "StrongPass123!");
        mockMvc.perform(put("/users/9999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isNotFound());
    }*/

   /* @Test @DisplayName("U15") void testGetAllUsers_Success() throws Exception {
        mockMvc.perform(get("/users/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email").exists());
    }

    @Test @DisplayName("U16") void testChangePassword_Success() throws Exception {
        ChangePasswordRequest req = new ChangePasswordRequest("StrongPass123!", "NewPass456!");
        mockMvc.perform(post("/change-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("mot de passe modifié")));
    }

    @Test @DisplayName("U17") void testChangePassword_WeakPassword() throws Exception {
        ChangePasswordRequest req = new ChangePasswordRequest("StrongPass123!", "123");
        mockMvc.perform(post("/change-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }

    @Test @DisplayName("U18") void testChangePassword_WrongOldPassword() throws Exception {
        ChangePasswordRequest req = new ChangePasswordRequest("WrongOld", "NewPass456!");
        mockMvc.perform(post("/change-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }*/





    // TODO: Implémenter les cas U4 à U18 avec le même pattern.
}

