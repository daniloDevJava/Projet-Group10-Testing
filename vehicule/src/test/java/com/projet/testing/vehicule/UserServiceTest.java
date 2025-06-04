package com.projet.testing.vehicule;

import com.projet.testing.vehicule.dto.ChangePasswordRequest;
import com.projet.testing.vehicule.dto.UserDto;
import com.projet.testing.vehicule.exception.BusinessException;
import com.projet.testing.vehicule.mapper.UserMapper;
import com.projet.testing.vehicule.repository.UserRepository;
import com.projet.testing.vehicule.service.ToKens;
import com.projet.testing.vehicule.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@ActiveProfiles("test")
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRepository userRepository;

    private UserDto userDto;

    @BeforeEach
    void setUp() {
        userDto = new UserDto();
        userDto.setEmail("test@example.com");
        userDto.setMdp("Password123");
        userDto.setName("John");

        // Remplis les autres champs nécessaires
    }

    @Test
    void testCreateUser() throws Exception {
        try {

            UserDto created = userService.createUser(userDto);
            assertNotNull(created);
            assertNotNull(created.getId());
            assertEquals("test@example.com", created.getEmail());
        }
        catch (BusinessException e){
            System.err.println(e.getMessage());
        }

    }

    @Test
    void testUpdateUser() throws BusinessException {
        try {
            userService.createUser(userDto);
            UserDto userToUpdate= userService.getUser(userDto.getEmail());

            userToUpdate.setName("UpdatedName");
            userToUpdate.setMdp("NoemarA147");

            UserDto updated = userService.updateUser(userToUpdate, userToUpdate.getId());

            assertEquals("UpdatedName", updated.getName());
        }
        catch (BusinessException e){
            System.err.println(e.getMessage());
        }

    }

    @Test
    void testGetAllUsers() throws BusinessException {
        try {
            UserDto user=userDto;
            user.setEmail("tesr@gmail.com");
            userService.createUser(user);
            List<UserDto> users = userService.getAllUsers();

            assertFalse(users.isEmpty());
            assertEquals(2, users.size());
        }
        catch ( BusinessException e){
            System.err.println(e.getMessage());
        }

    }

    @Test
    void testLoginSuccess() throws Exception {
        try {
            userService.createUser(userDto);
            ToKens tokens = userService.login(userDto, 15);
            assertNotNull(tokens);
            assertNotNull(tokens.getAccessToken());
            assertNotNull(tokens.getRefreshToken());
        }
        catch ( BusinessException e){
            System.err.println(e.getMessage());
        }


    }

    @Test
    void testGetUserByEmail() throws Exception {
        try {
            userService.createUser(userDto);
            UserDto fetched = userService.getUser("test@example.com");
            assertNotNull(fetched);
            assertEquals("John", fetched.getName());
        }
        catch (BusinessException e){
            System.err.println(e.getMessage());
        }

    }

    @Test
    void testChangePassword() throws Exception {
        try {
            userService.createUser(userDto);
            ChangePasswordRequest req = new ChangePasswordRequest("test@example.com","newPassword456");

            ChangePasswordRequest result = userService.changePassword("Password123", "newPassword456", req);
            assertEquals("test@example.com", result.getEmail());
        }
        catch (BusinessException e){
            System.err.println(e.getMessage());
        }

    }
}

