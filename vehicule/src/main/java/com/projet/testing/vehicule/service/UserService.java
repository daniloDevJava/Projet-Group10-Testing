package com.projet.testing.vehicule.service;

import com.projet.testing.vehicule.dto.UserDto;
import com.projet.testing.vehicule.exception.BusinessException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface  UserService {
    public UserDto createUser(UserDto userDto) throws BusinessException;
    public UserDto updateUser(UserDto userDto, UUID id) throws BusinessException;
    public ToKens login(UserDto userDto) throws BusinessException;
    public ToKens refreshToken(String oldRefreshToken) throws BusinessException;
}
