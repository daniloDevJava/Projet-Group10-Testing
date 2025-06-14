package com.projet.testing.vehicule.mapper;

import com.projet.testing.vehicule.dto.UserDto;
import com.projet.testing.vehicule.model.User;
import org.springframework.stereotype.Component;

/**
 * The type User mapper.
 */
@Component
public class UserMapper {
    /**
     * To dto user dto.
     *
     * @param user the user
     * @return the user dto
     */
    public UserDto toDto(User user){
        UserDto userDto =new UserDto();
        userDto.setId(user.getId());
        userDto.setName(user.getUsername());
        userDto.setEmail(user.getEmail());

        return userDto;

    }

    /**
     * To entity user.
     *
     * @param userDto the user dto
     * @return the user
     */
    public User toEntity(UserDto userDto ){
        User user = new User() ;
        user.setEmail(userDto.getEmail());
        user.setUsername(userDto.getName());
        user.setMdp(userDto.getMdp());
        return user;
    }
}
