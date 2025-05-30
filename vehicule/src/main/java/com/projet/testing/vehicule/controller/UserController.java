package com.projet.testing.vehicule.controller;

import com.projet.testing.vehicule.dto.UserDto;
import com.projet.testing.vehicule.exception.BusinessException;
import com.projet.testing.vehicule.service.JwtService;
import com.projet.testing.vehicule.service.ToKens;
import com.projet.testing.vehicule.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtService jwtService;

    @PostMapping("/add")
    @Operation(summary = "create User")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "enregistrer un utilisateur "),
            @ApiResponse(responseCode = "400", description = "mauvaise entree")
    })
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto) throws BusinessException {
        UserDto user=userService.createUser(userDto);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    @Operation(summary = "get an user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200" , description = "get an user "),
            @ApiResponse(responseCode = "404",description = "les informations de correction sont incorrectes")
    })
    public ResponseEntity<ToKens> getUser(UserDto userDto) throws BusinessException{
        try {
            ToKens toKens =userService.login(userDto);
            return new ResponseEntity<>(toKens,HttpStatus.OK);
        }
        catch (IllegalArgumentException e){
            ToKens error=new ToKens(e.getMessage(),"");
            return new ResponseEntity<>(error,HttpStatus.NOT_FOUND);
        }

    }

    @GetMapping("/{id}")
    @Operation(summary = "change ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200" , description = "updating is successfully"),
            @ApiResponse(responseCode = "404", description = "the user not found")
    })
    public ResponseEntity<UserDto> updateUser(@Valid @RequestBody UserDto userDto, UUID id) throws BusinessException{

            UserDto user=userService.updateUser(userDto,id);
            return new ResponseEntity<>(user,HttpStatus.OK);
    }

    @PostMapping("/refresh-tokens")
    @Operation(summary = "Trouver ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = ""),
            @ApiResponse(responseCode = "404", description = "")
    })
    public ResponseEntity<ToKens> refreshTokens(@RequestParam String refreshToken) throws BusinessException{
        ToKens toKens=jwtService.refreshTokens(refreshToken);
        return new ResponseEntity<>(toKens,HttpStatus.OK);

    }

    @PostMapping("/refresh-acccess-tokens")
    @Operation(summary = "")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "successfully"),
            @ApiResponse(responseCode = "400",description = "bad entry")
    })
    public ResponseEntity<String> refreshAccessToken(@RequestParam String refreshToken) throws BusinessException{
        String accessToken= jwtService.refreshAccessToken(refreshToken);
        return new ResponseEntity<>("{ \"accessToken\":"+accessToken,HttpStatus.OK);
    }









}
