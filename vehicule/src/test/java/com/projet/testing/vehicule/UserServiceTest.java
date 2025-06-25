package com.projet.testing.vehicule;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.projet.testing.vehicule.dto.ChangePasswordRequest;
import com.projet.testing.vehicule.dto.UserDto;
import com.projet.testing.vehicule.exception.BusinessException;
import com.projet.testing.vehicule.mapper.UserMapper;
import com.projet.testing.vehicule.repository.UserRepository;
import com.projet.testing.vehicule.service.ToKens;
import com.projet.testing.vehicule.service.UserService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;


import static org.junit.jupiter.api.Assertions.*;



@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    private  UserDto userDto;
    private ExtentReports extent;
    private ExtentTest test;

    @BeforeAll
    void setupTest() {
        extent = ReportManager.createReport("UserServiceTest");
        extent.setSystemInfo("Project", "Properlize projet of vehicule's location");
        extent.setSystemInfo("Tester", "DAN YVES BRICE AYOMBA II");
        userDto = new UserDto();
        userDto.setEmail("test@example.com");
        userDto.setMdp(passwordEncoder.encode("Passwd123"));
        userDto.setName("John".toUpperCase());
        userRepository.save(userMapper.toEntity(userDto));
    }
    @BeforeEach
    void setUp(TestInfo info) {
        test = extent.createTest(info.getDisplayName());

    }

    @Test
    @DisplayName("Test de levee d'une businessException apres ajout d'un user avec un mot de passe faible")
    void testCreateUserWithWeakPassword_ThrowsBusinessException(){
    	//Arrange + Act + Assert 1
        BusinessException exception=assertThrows(BusinessException.class ,() ->{
            UserDto user=new UserDto();
            user.setMdp("tamo14");
            user.setName("neymar");
            user.setEmail("test@mple.com");
            userService.createUser(user);
        });
	//Assert 2
        assertEquals("Le mot de passe est faible",exception.getErrorModels().getFirst().getMessage());

    }
    @Test
    @DisplayName("Test de levee d'exception apres modification d'une  adresse mail d'un utilisateur avec celle d'un autre utilisateur")
    void testUpdateUserMailWithMailOfOtherUser_ThrowsBusinessException(){
    
        //Arrange
        List<UserDto> list=userService.getAllUsers();
        UserDto userToUpdate=userService.getUser("test@example.com");
        
        
	//Act + Assert 1
        BusinessException exception=assertThrows(BusinessException.class ,() ->{

            userToUpdate.setMdp("tamo14");
            userToUpdate.setName("neymar");
            userToUpdate.setEmail(list.getLast().getEmail());
            userService.updateUser(userToUpdate,userToUpdate.getId());
        });
	//Assert 2
        assertEquals("il existe deja un user avec cet adresse mail",exception.getErrorModels().getFirst().getMessage());
    }


    @Test
    @DisplayName("Test d'ajout succès d'user dans la bd")
    void testCreateUser()  {
	    //Arrange
            UserDto user=new UserDto();
            user.setMdp("tuyamo14H");
            user.setName("neymar");
            user.setEmail("test@e1xample.com");
            
            //Act
            UserDto created = userService.createUser(user);
            
            //Assert
            assertNotNull(created);
            assertNotNull(created.getId());
            assertEquals("test@e1xample.com", created.getEmail());


    }


    @Test
    @DisplayName("Test de mise a jour reussie d'un utilisateur")
    void testUpdateUser() throws BusinessException {

	    //Arrange
            UserDto userToUpdate= userService.getUser("test@example.com");

            userToUpdate.setName("UpdatedName");
            userToUpdate.setMdp("NoemarA147");

            //Act
            UserDto updated = userService.updateUser(userToUpdate, userToUpdate.getId());

	    //Assert
            assertEquals("UpdatedName".toUpperCase(), updated.getName());


    }

    @Test
    @DisplayName("Test de chargement de la liste d'users dans la bd")
    void testGetAllUsers() throws BusinessException {
            
	    //Arrange
            UserDto user=new UserDto();
            user.setName("johnidep");
            user.setEmail("tesr@gmail.com");
            user.setMdp("Passerole12");
            userService.createUser(user);
            
            //Act
            List<UserDto> users = userService.getAllUsers();

	    //Assert
            assertFalse(users.isEmpty());
            assertEquals(2, users.size());


    }

    @Test
    @DisplayName("Test de reussite d'un login")
    @Transactional
    void testLoginSuccess()  {
    
    	    //Arrange
            UserDto user=new UserDto();
            user.setEmail("danil@gmail.com");
            user.setMdp("Passworld304");
            user.setName("Sung Jinwoo");
            userService.createUser(user);
            
            //Act
            ToKens tokens = userService.login(user, 15);
            
            //Assert
            assertNotNull(tokens);
            assertNotNull(tokens.getAccessToken());
            assertNotNull(tokens.getRefreshToken());


    }

    @Test
    @DisplayName("Test find user by mail")
    void testGetUserByEmail()  {
	    //Act
            UserDto fetched = userService.getUser("test@example.com");
            //Assert
            assertNotNull(fetched);
            assertEquals("John".toUpperCase(), fetched.getName());

    }

    @Test
    @DisplayName("Test de changement de  mot de passe")
    @Transactional
    void testChangePassword()  {
    
        //Arrange
        UserDto user=new UserDto();
        user.setMdp("tuyamo14H");
        user.setName("neymar");
        user.setEmail("test@xample.com");
        userService.createUser(user);

        ChangePasswordRequest req = new ChangePasswordRequest( "test@xample.com","newPasswo8d456");

	//Act
        ChangePasswordRequest result = userService.changePassword("tuyamo14H", "newPasswo8d456", req);
        //Assert
        assertEquals("test@xample.com", result.getEmail());


    }


    @Test
    @DisplayName("Test d'ajout d'utilisateur existant(mail) et levée d'exception dans la bd")
    void testCreateUserThrowsBusinessException() {

        // Act + Assert : l' appel doit lever une exception
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            UserDto user=new UserDto();
            user.setEmail("test@example.com");
            user.setMdp("testP14781");
            user.setName(userDto.getName());
            userService.createUser(user); // même email
        });

        // Optionnel : vérifier le message
        assertEquals("il existe deja un user avec cet adresse mail", exception.getErrorModels().getFirst().getMessage());
    }



    @AfterAll
    void tearDownReport() {
        extent.flush();
        ReportManager.generateIndexHtml();
    }
}

