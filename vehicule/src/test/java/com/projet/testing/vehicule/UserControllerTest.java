package com.projet.testing.vehicule;

import com.projet.testing.vehicule.dto.UserDto;
import com.projet.testing.vehicule.model.User;
import com.projet.testing.vehicule.service.JwtService;
import com.projet.testing.vehicule.service.ToKens;
import com.projet.testing.vehicule.dto.ChangePasswordRequest;
import com.projet.testing.vehicule.util.JwtUtil;
import com.aventstack.extentreports.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.projet.testing.vehicule.service.UserService;
import com.projet.testing.vehicule.repository.*;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;
import java.util.UUID;


import static org.mockito.Mockito.*;
import static org.springframework.test.util.AssertionErrors.assertFalse;
import static org.springframework.test.util.AssertionErrors.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserControllerTest {

    @Autowired
    private  UserService userService;
    @Autowired
    private  MockMvc mockMvc;
    @Autowired
    private  JwtService jwtService;
    @SpyBean
    private  JwtUtil jwtUtil;
    @Autowired
    private UserRepository userRepository;
    
    private ExtentReports extent;
    private ExtentTest test;

    @BeforeAll
    void setupReport() {
        extent = ReportManager.createReport("UserControllerTest");
        extent.setSystemInfo("Project", "Properlize projet of vehicule's location");
        extent.setSystemInfo("Tester", "DAN YVES BRICE AYOMBA II");
    }

    @BeforeEach
    void createTest(TestInfo info) {
        test = extent.createTest(info.getDisplayName());
    }



    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("Doit retourner un statut 201 et un objet json correct")
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
    @DisplayName(" Doit renvoyer une erreure 400 dont l'erreure est sous forme de tableau d'objets json pour un objet , on a le code est ' mdp ' et non 'BAD_ENTRY' ce qui veut dire que l'objet passe en corps de requete ne passe meme pas la validation")
    public void testAddUser_WeakPassword() throws Exception {

        UserDto user = new UserDto();
        user.setEmail("testUser@example.com");
        user.setMdp("123");
        user.setName("John");

        mockMvc.perform(post("http://localhost:9000/users/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.[0].code").value("mdp"));;
    }



    @Test
    @DisplayName(" Doit retourner le code 400 pour les deux utilisateurs mais pour le premier l'objet du corps de requete est correcte et le code est 'BAD_ENTRY' tandis que pour le second utilisateur ne passe pas la validation et le code est 'email'")
    void testAddUser_InvalidEmail() throws Exception {

        //Act
        UserDto user = new UserDto();
        user.setEmail("marie");
        user.setName("Johny");

        UserDto user1=new UserDto();
        user1.setEmail("yutttttttttttttteiiiiiiiiiiiiiiyttttttttttttttttttttttttttttttdddddddddddddddddddddddd");
        user1.setName("hiruzen");
        user1.setMdp("yookeAka7");

        //Act & Assert
        mockMvc.perform(post("http://localhost:9000/users/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].code").value("BAD_ENTRY"));
       mockMvc.perform(post("http://localhost:9000/users/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user1)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.[0].code").value("email"));

    }



    @Test
    @DisplayName(" Connexion réussie après ajout d'utilisateur")
    void testLogin_Success() throws Exception {
        // Création d’un utilisateur de test: Arrange part 1
        UserDto request = new UserDto();
        request.setEmail("test78@example.com");
        request.setMdp("StrongPass123");
        request.setName("Dan");

        // Ajout de l’utilisateur Arrange part 2
        mockMvc.perform(post("http://localhost:9000/users/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
               .andExpect(status().isCreated());

        // Connexion avec cet utilisateur: Act and Assert
        mockMvc.perform(post("http://localhost:9000/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.accessToken").exists())
               .andExpect(jsonPath("$.refreshToken").exists());
    }

    @Test
    @DisplayName("Connexions echouees avec de mauvaises informations de connexion l'api devra retourner 404(un email inconnu) et un code 'BAD_ARGUMENTS'403 (un mdp faux) et un code 'BAD_ARGUMENTS'")
    void testLogin_InvalidCredentials() throws Exception {

        //Arrange 1
        UserDto request = new UserDto();
        request.setMdp("wrongpass");
        request.setEmail("test78@example.com");

        //Act 1 & Asserts 1
        mockMvc.perform(post("http://localhost:9000/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.[0].code").value("BAD_CREDENTIALS"));

        //Arrange2
        request.setEmail("test87@gmail.com");

        //Act 2 & Assert2
        mockMvc.perform(post("http://localhost:9000/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.[0].code").value("BAD_ARGUMENTS"));

    }

    @Test
    @DisplayName("U6 : tester si l'accessToken est genere a partir d'un bon refreshToken")
    @Transactional
    void testRefreshAccessToken_Success() throws Exception {
        //Arrange
        UserDto request=new UserDto();
        request.setEmail("test78@example.com");
        request.setMdp("StrongPass123");
        ToKens toKens=userService.login(request,15);




        //Act & Assert
        mockMvc.perform(post("http://localhost:9000/users/refresh-access-tokens")
                        .param("refreshToken",toKens.getRefreshToken()))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("accessToken")));
        assertTrue("Le token entre est valide",jwtUtil.isTokenValid(toKens.getRefreshToken()));
        assertFalse("Le token entre ne doit pas etre expire",jwtUtil.isTokenExpired(toKens.getRefreshToken()));

    }

   @Test
   @DisplayName("U7 - tester si a partir d'un refresh token invalide l'api devra renvoyer 403 et un message d'erreur")
    void testRefreshAccessToken_InvalidToken() throws Exception {
        String token="rjjjjjjjjjjjjjjjjjjbdr";

        mockMvc.perform(post("http://localhost:9000/users/refresh-access-tokens")
                        .param("refreshToken","rjjjjjjjjjjjjjjjjjjbdr")
                        .header("Authorization", "Bearer invalid-token"))
                .andExpect(status().isForbidden())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("refreshToken invalide")));
        assertFalse("Le token entre est invalide", jwtUtil.isTokenValid(token));
    }

    @Test
    @DisplayName("teste de refresh accesstoken qui echoue a cause d'essai avec un refresh token expire  ")
    void testRefreshAccessToken_ExpiredToken() throws Exception {

        String refreshToken = jwtUtil.generateRefreshToken("test@example.com", 15);

        doReturn(true).when(jwtUtil).isTokenExpired(refreshToken);
        mockMvc.perform(post("http://localhost:9000/users/refresh-access-tokens")
                        .param("refreshToken", refreshToken)
                        .header("Authorization", "Bearer expired-token"))
                .andExpect(status().isForbidden())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("refreshToken invalide ou expiré")));

        assertTrue("Le token est toutefois valide...",jwtUtil.isTokenValid(refreshToken));

    }

    @Test
    @DisplayName("Test de refresh access token qui echoue avec un refresh token inconnu")
    void testRefreshAccessToken_WithRefreshTokenNotFound() throws Exception {
        String refresh= jwtUtil.generateAccessToken("test@ex.ple");

        mockMvc.perform(post("http://localhost:9000/users/refresh-access-tokens")
                        .param("refreshToken", refresh)
                        .header("Authorization", "Bearer expired-token"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.[0].code").value("INVALID_ENTRY"))
                .andExpect(jsonPath("$.[0].message").value("refresh token inconnu"));
    }


    @Test
    @DisplayName("Test de refresh access token qui echoue avec un refresh token inconnu")
    void testRefreshTokens_WithRefreshTokenNotFound() throws Exception{
        String refresh= jwtUtil.generateRefreshToken("test@ex.ple",15);

        mockMvc.perform(post("http://localhost:9000/users/refresh-tokens")
                        .param("refreshToken", refresh)
                        .header("Authorization", "Bearer expired-token"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.[0].code").value("INVALID_ENTRY"))
                .andExpect(jsonPath("$.[0].message").value("refresh token inconnu"));
    }


    @Test
    @DisplayName("Test de rafraichissements de tokens en succes")
    @Transactional
    void testRefreshTokens_Success() throws Exception{
        // Création d’un utilisateur de test: Arrange part 1
        UserDto request = new UserDto();
        request.setEmail("test89@example.com");
        request.setMdp("StrongPass123");
        request.setName("Dan");

        // Ajout de l’utilisateur Arrange part 2
        mockMvc.perform(post("http://localhost:9000/users/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());



        Optional<User> optionalUser=userRepository.findByEmailAndDeleteAtIsNull("test89@example.com");

        if(optionalUser.isPresent()) {


            ToKens toKens=jwtService.generateTokens(optionalUser.get(),15);
            doReturn(true).when(jwtUtil).isTokenExpired(toKens.getRefreshToken());


            mockMvc.perform(post("http://localhost:9000/users/refresh-tokens")
                            .param("refreshToken", toKens.getRefreshToken())
                            .header("Authorization", "Bearer valid-refresh-token"))
                    .andExpect(status().isOk());
        }
        System.err.println("Echec du test du a un user inexistant pour l'email de test");

    }


    @Test
    @DisplayName("Test de refresh token avec un refresh token pas expire")
    @Transactional
    void testRefreshTokens_With_RefreshToken_Not_Expired() throws Exception {
        //Arrange
        UserDto request=new UserDto();
        request.setEmail("test78@example.com");
        request.setMdp("StrongPass123");
        ToKens toKens=userService.login(request,15);

        mockMvc.perform(post("http://localhost:9000/users/refresh-tokens")
                        .param("refreshToken",toKens.getRefreshToken())
                        .header("Authorization", "Bearer valid-refresh-token"))
                .andExpect(status().isUnauthorized());

    }

    @Test
    @DisplayName("Test de changement d'informations ens success")
    void testUpdateUser_Success() throws Exception {
    // Création d’un utilisateur de test: Arrange part 1
            UserDto request = new UserDto();
            request.setEmail("test788@example.com");
            request.setMdp("StrongPass123");
            request.setName("Dan");

            // Ajout de l’utilisateur Arrange part 2
            mockMvc.perform(post("http://localhost:9000/users/add")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated());
            Optional<User> optionalUser=userRepository.findByEmailAndDeleteAtIsNull("test788@example.com");

            User user = optionalUser.get();

            UserDto updated = new UserDto();
            updated.setEmail("test@87example.com");
            updated.setName("Dan");
            updated.setMdp("ettttttteg");

            mockMvc.perform(put("http://localhost:9000/users/" + user.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updated)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.email").value("test@87example.com"))
                    .andExpect(jsonPath("$.name").value("DAN"));

    }

    @Test
    @DisplayName("Test de chamgement d'informations avec un nouvel email mal structuré")
    void testUpdateUser_InvalidEmail() throws Exception {
        Optional<User> optionalUser=userRepository.findByEmailAndDeleteAtIsNull("test@87example.com");
        User user= optionalUser.get();

        UserDto updated = new UserDto();
        updated.setEmail("bademail");
        updated.setMdp("StrongPass1234");
        updated.setName("julie");

        mockMvc.perform(put("/users/"+user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isBadRequest());

    }

    @Test
    @DisplayName("Test de changements d'informations rate a cause d'un mauvais id ")
    void testUpdateUser_NotFound() throws Exception {
        UserDto updated = new UserDto();
        updated.setEmail("bademail");
        updated.setMdp("StrongPass1234");
        updated.setName("julie");
        mockMvc.perform(put("http://localhost:9000/users/"+ UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Test de chargement de la liste d'utilisateurs")
    void testGetAllUsers_Success() throws Exception {
        mockMvc.perform(get("http://localhost:9000/users/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email").exists());
    }

    @Test
    @DisplayName("Test de changement de mot de passe reussi")
    void testChangePassword_Success() throws Exception {
        // Création d’un utilisateur de test: Arrange part 1
        UserDto request = new UserDto();
        request.setEmail("test8@example.com");
        request.setMdp("StrongPass123");
        request.setName("Dan");

        // Ajout de l’utilisateur Arrange part 2
        mockMvc.perform(post("http://localhost:9000/users/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        ChangePasswordRequest req = new ChangePasswordRequest(request.getEmail(), "NewPass456!");
        mockMvc.perform(patch("/users/change-password")
                        .param("oldPassword","StrongPass123")
                        .param("newPassword",req.getPassword())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.password").value("NewPass456!"));
    }

    @Test
    @DisplayName("Test qui echoue avec une adresse mail qui echoue")
    void testChangePassword_FailedWithEmailNotFound() throws Exception {
        ChangePasswordRequest req = new ChangePasswordRequest("etltktt@gnnek", "NewPass456!");
        mockMvc.perform(patch("/users/change-password")
                        .param("oldPassword","StrongPass123")
                        .param("newPassword",req.getPassword())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.[0].code").value("BAD_ARGUMENTS"));
    }

    @Test
    @DisplayName("Test d'echec de changement du mot de passe d'un utilisateur a cause du nouveau mot de passe faible")
    void testChangePassword_WeakPassword() throws Exception {
        ChangePasswordRequest req = new ChangePasswordRequest("test8@example.com", "123");
        mockMvc.perform(patch("/users/change-password")
                        .param("oldPassword","NewPass456!")
                        .param("newPassword",req.getPassword())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.[0].code").value("BAD_ENTRY"));
    }

    @Test
    @DisplayName("Test d'echec de changement du mot de passe d'un utilisateur a cause d'echec du mot de passe ")
    void testChangePassword_WrongOldPassword() throws Exception {
        ChangePasswordRequest req = new ChangePasswordRequest("test8@example.com", "OldPass456!");
        mockMvc.perform(patch("/users/change-password")
                        .param("oldPassword","OldPass14789")
                        .param("newPassword",req.getPassword())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Test de recherche d'un utilisateur par mail")
    void testGetUserByMail() throws Exception{
        // Création d’un utilisateur de test: Arrange part 1
        UserDto request = new UserDto();
        request.setEmail("test18@example.com");
        request.setMdp("StrongPass123");
        request.setName("Dan");

        // Ajout de l’utilisateur Arrange part 2
        mockMvc.perform(post("http://localhost:9000/users/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/users/get/"+"test18@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists());


    }

    @Test
    @DisplayName("Test de recherche d'un user par mail qui echoue avec un email non reconnu")
    void testGetUser_FailedWithNotFoundEmail() throws Exception{
        mockMvc.perform(get("/users/get/"+"test18000@example.com"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.[0]").exists());
    }
    
    @AfterAll
    void tearDownReport() {
        extent.flush();
        ReportManager.generateIndexHtml();
    }

}

