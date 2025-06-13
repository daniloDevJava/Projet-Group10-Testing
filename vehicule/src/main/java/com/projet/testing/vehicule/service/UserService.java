package com.projet.testing.vehicule.service;

import com.projet.testing.vehicule.dto.UserDto;
import com.projet.testing.vehicule.exception.BusinessException;
import com.projet.testing.vehicule.dto.ChangePasswordRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * The interface User service.
 */
@Service
public interface  UserService {
    /**
     * Create user user dto.
     *
     * @param userDto the user dto
     * @return the user dto
     * @throws BusinessException the business exception
     */
     UserDto createUser(UserDto userDto) throws BusinessException;

    /**
     * Update user user dto.
     *
     * @param userDto the user dto
     * @param id      the id
     * @return the user dto
     * @throws BusinessException the business exception
     */
     UserDto updateUser(UserDto userDto, UUID id) throws BusinessException;

    /**
     * Login to kens.
     *
     * @param userDto the user dto
     * @return the tokens
     * @throws BusinessException the business exception
     */
     ToKens login(UserDto userDto,long time) throws BusinessException;

    List<UserDto> getAllUsers();

    ChangePasswordRequest changePassword(String oldPassword, String newPassword, ChangePasswordRequest changePasswordRequest);

    UserDto getUser(String email);


}
