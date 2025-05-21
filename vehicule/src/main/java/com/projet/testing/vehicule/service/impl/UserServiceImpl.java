package com.projet.testing.vehicule.service.impl;

import com.projet.testing.vehicule.dto.UserDto;
import com.projet.testing.vehicule.exception.BusinessException;
import com.projet.testing.vehicule.exception.ErrorModel;
import com.projet.testing.vehicule.mapper.UserMapper;
import com.projet.testing.vehicule.model.User;
import com.projet.testing.vehicule.repository.UserRepository;
import com.projet.testing.vehicule.service.ToKens;
import com.projet.testing.vehicule.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    @Override
    public UserDto createUser(UserDto userDto) throws BusinessException{
        List<ErrorModel> errorModels= new ArrayList<>();
        User user=userMapper.toEntity(userDto);

        Optional<User> optionalUser=userRepository.findByEmailAndDeleteAtIsNull(userDto.getEmail());

        if(optionalUser.isEmpty()){

            if(!user.getEmail().contains("@")){
                ErrorModel errorModel=new ErrorModel();
                errorModel.setCode("BAD_ENTRY");
                errorModel.setMessage("L'adresse n'est pas valide");
                errorModels.add(errorModel);
                throw new BusinessException(errorModels);
            }
            else{
                user.setUsername(userDto.getName().toUpperCase());
                user.setMdp(bCryptPasswordEncoder.encode(user.getPassword()));
                return userMapper.toDto(userRepository.save(user));
            }

        }
        else{
            ErrorModel errorModel=new ErrorModel();
            errorModel.setCode("AUTHORIZATION_FAILED");
            errorModel.setMessage("il existe deja un user avec cet adresse mail");
            errorModels.add(errorModel);
            throw new BusinessException(errorModels);
        }

    }

    @Override
    public UserDto updateUser(UserDto userDto, UUID id) throws BusinessException{

        Optional<User> optionalUser=userRepository.findByIdAndDeleteAtIsNull(id);
        List<ErrorModel> errorModels= new ArrayList<>();

        if(optionalUser.isPresent()){
            User user= optionalUser.get();
            user.setMdp(bCryptPasswordEncoder.encode(userDto.getMdp()));
            user.setUsername(userDto.getName().toUpperCase());
            Optional<User> optionalUserE=userRepository.findByEmailAndDeleteAtIsNull(userDto.getEmail());

            if(optionalUserE.isEmpty()) {
                user.setEmail(userDto.getEmail());
                return userMapper.toDto(userRepository.save(user));
            }
            else{
                ErrorModel errorModel=new ErrorModel();
                errorModel.setCode("AUTHORIZATION_FAILED");
                errorModel.setMessage("il existe deja un user avec cet adresse mail");
                errorModels.add(errorModel);
                throw new BusinessException(errorModels);
            }

            }
        else{
            ErrorModel errorModel=new ErrorModel();
            errorModel.setCode("AUTHORIZATION_FAILED");
            errorModel.setMessage("il existe deja un user avec cet adresse mail");
            errorModels.add(errorModel);
            throw new BusinessException(errorModels);
        }
        }

       /* @Override
        public ToKens login(UserDto userDto) throws BusinessException{

        }*/

    }




