package com.projet.testing.vehicule.service;

import com.projet.testing.vehicule.dto.UserDto;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface  UserService {
    public UserDto createUser(UserDto userDto);
    public UserDto updateUser(UserDto userDto, UUID id);
    public ToKens login(UserDto userDto);
    public ToKens refreshToken(String oldRefreshToken);
}
