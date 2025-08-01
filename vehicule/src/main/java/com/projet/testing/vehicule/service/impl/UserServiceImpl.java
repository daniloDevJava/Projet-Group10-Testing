package com.projet.testing.vehicule.service.impl;

import com.projet.testing.vehicule.dto.UserDto;
import com.projet.testing.vehicule.exception.BusinessException;
import com.projet.testing.vehicule.exception.ErrorModel;
import com.projet.testing.vehicule.mapper.UserMapper;
import com.projet.testing.vehicule.model.User;
import com.projet.testing.vehicule.repository.UserRepository;
import com.projet.testing.vehicule.service.JwtService;
import com.projet.testing.vehicule.service.ToKens;
import com.projet.testing.vehicule.service.UserService;
import com.projet.testing.vehicule.dto.ChangePasswordRequest;
import com.projet.testing.vehicule.util.PasswordValidator;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * The type User service.
 */
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final PasswordValidator passwordValidator;


    @Override
    public UserDto createUser(UserDto userDto) throws BusinessException{
        List<ErrorModel> errorModels= new ArrayList<>();
        User user=userMapper.toEntity(userDto);

        Optional<User> optionalUser=userRepository.findByEmailAndDeleteAtIsNull(userDto.getEmail());

        if(optionalUser.isEmpty() && verificateGoodEmail(user.getEmail())){

                user.setUsername(userDto.getName().toUpperCase());
                if(passwordValidator.isPasswordValid(user.getPassword())){
                    user.setMdp(passwordEncoder.encode(user.getMdp()));
                    return userMapper.toDto(userRepository.save(user));
                }
                else
                {
                    ErrorModel errorModel=new ErrorModel();
                    errorModel.setCode("BAD_ENTRY");
                    errorModel.setMessage("Le mot de passe est faible");
                    errorModels.add(errorModel);
                    throw new BusinessException(errorModels, HttpStatus.BAD_REQUEST);
                }

        }
        else{
            ErrorModel errorModel=new ErrorModel();
            errorModel.setCode("AUTHORIZATION_FAILED");
            errorModel.setMessage("il existe deja un user avec cet adresse mail");
            errorModels.add(errorModel);
            throw new BusinessException(errorModels, HttpStatus.FORBIDDEN);
        }

    }

    @Override
    public UserDto updateUser(UserDto userDto, UUID id) throws BusinessException{

        Optional<User> optionalUser=userRepository.findByIdAndDeleteAtIsNull(id);
        List<ErrorModel> errorModels= new ArrayList<>();

        if(optionalUser.isPresent()){
            User user= optionalUser.get();
            user.setMdp(passwordEncoder.encode(userDto.getMdp()));
            user.setUsername(userDto.getName().toUpperCase());
            Optional<User> optionalUserE=userRepository.findByEmailAndDeleteAtIsNull(userDto.getEmail());

            if((optionalUserE.isEmpty() || userDto.getEmail().equals(user.getEmail())) && verificateGoodEmail(userDto.getEmail()) ) {
                user.setEmail(userDto.getEmail());
                return userMapper.toDto(userRepository.save(user));
            }
            else{
                ErrorModel errorModel=new ErrorModel();
                errorModel.setCode("AUTHORIZATION_FAILED");
                errorModel.setMessage("il existe deja un user avec cet adresse mail");
                errorModels.add(errorModel);
                throw new BusinessException(errorModels,HttpStatus.UNAUTHORIZED);
            }

        }
        else{
           throw new IllegalArgumentException("Utilisateur non trouve");
        }
        }

        private static boolean verificateGoodEmail(String mail) throws BusinessException{
            if(!mail.contains("@") || !mail.contains(".")){
                List<ErrorModel> errorModels= new ArrayList<>();
                ErrorModel errorModel=new ErrorModel();
                errorModel.setCode("BAD_ENTRY");
                errorModel.setMessage("L'adresse mail est mal formée");
                errorModels.add(errorModel);
                throw new BusinessException(errorModels,HttpStatus.BAD_REQUEST);
            }
            else
                return true;
        }

        @Override
        public ToKens login(UserDto userDto,long time) throws BusinessException{
            Optional<User> optionalUser = userRepository.findByEmailAndDeleteAtIsNull(userDto.getEmail());

            if (optionalUser.isPresent()) {
                User utilisateur = optionalUser.get();

                // Vérification du mot de passe
                if (!passwordEncoder.matches(userDto.getMdp(), utilisateur.getPassword())) {
                    ErrorModel errorModel = new ErrorModel();
                    errorModel.setCode("BAD_CREDENTIALS");
                    errorModel.setMessage("Identifiants invalides");
                    throw new BusinessException(List.of(errorModel),HttpStatus.FORBIDDEN);
                }

                // Générer les tokens si tout est valide
                return jwtService.generateTokens(utilisateur,time);
            } else {
                throw new IllegalArgumentException("Utilisateur non trouvé");
            }

        }

    @Override
    public List<UserDto> getAllUsers() {
        List<User> list=userRepository.findByDeleteAtIsNull();
        return list.stream().map(userMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public ChangePasswordRequest changePassword(String oldPassword, String newPassword, ChangePasswordRequest changePasswordRequest) {

            Optional<User> optionalUser=userRepository.findByEmailAndDeleteAtIsNull(changePasswordRequest.getEmail());
            if(optionalUser.isPresent()) {
                User user= optionalUser.get();
                UserDto userDto = new UserDto();
                userDto.setMdp(oldPassword);
                userDto.setEmail(changePasswordRequest.getEmail());
                login(userDto,15);
                if(!oldPassword.equals(newPassword) && passwordValidator.isPasswordValid(newPassword)) {
                    user.setMdp(passwordEncoder.encode(newPassword));
                    userRepository.save(user);
                    changePasswordRequest.setPassword(newPassword);
                    return changePasswordRequest;
                }
                else{
                    ErrorModel errorModel=new ErrorModel();
                    errorModel.setMessage("Le nouveau mot de passe ne peut etre egal a l'ancien et doit etre fort");
                    errorModel.setCode("BAD_ENTRY");
                    throw new BusinessException(List.of(errorModel),HttpStatus.BAD_REQUEST);
                }
            }
            else
                throw new IllegalArgumentException("Utilisateur non trouvé pour cette adresse mail");

    }

    @Override
    public UserDto getUser(String email) {
        Optional<User> optionalUser=userRepository.findByEmailAndDeleteAtIsNull(email);
        if(optionalUser.isPresent()) {
            User user= optionalUser.get();
            return userMapper.toDto(user);
        }
        throw new IllegalArgumentException("Utilisateur non trouvé");
    }

}




