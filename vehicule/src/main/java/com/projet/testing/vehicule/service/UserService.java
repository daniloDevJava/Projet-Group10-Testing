package com.projet.testing.vehicule.service;

import com.projet.testing.vehicule.dto.UserDto;
import com.projet.testing.vehicule.exception.BusinessException;
import org.springframework.stereotype.Service;

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
    public UserDto createUser(UserDto userDto) throws BusinessException;

    /**
     * Update user user dto.
     *
     * @param userDto the user dto
     * @param id      the id
     * @return the user dto
     * @throws BusinessException the business exception
     */
    public UserDto updateUser(UserDto userDto, UUID id) throws BusinessException;

    /**
     * Login to kens.
     *
     * @param userDto the user dto
     * @return the to kens
     * @throws BusinessException the business exception
     */
    public ToKens login(UserDto userDto) throws BusinessException;
    ;
}
