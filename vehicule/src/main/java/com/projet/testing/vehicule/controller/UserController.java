package com.projet.testing.vehicule.controller;

import com.projet.testing.vehicule.dto.UserDto;
import com.projet.testing.vehicule.exception.BusinessException;
import com.projet.testing.vehicule.service.JwtService;
import com.projet.testing.vehicule.service.ToKens;
import com.projet.testing.vehicule.service.UserService;
import com.projet.testing.vehicule.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * The type User controller.
 */
@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtService jwtService;

    private final JwtUtil jwtUtil;

    /**
     * Create user response entity.
     *
     * @param userDto the user dto
     * @return the response entity
     * @throws BusinessException the business exception
     */
    @PostMapping("/add")
    @Operation(summary = "create User")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "enregistrer un utilisateur "),
            @ApiResponse(responseCode = "400", description = "mauvaise entree")
    })
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto){
        try {

            UserDto user=userService.createUser(userDto);
            return new ResponseEntity<>(user, HttpStatus.CREATED);
        }
        catch (BusinessException e){
            throw new BusinessException(e.getErrorModels(),e.getStatus());
        }
    }

    /**
     * Gets user.
     *
     * @param userDto the user dto
     * @return the user
     * @throws BusinessException the business exception
     */
    @PostMapping("/login")
    @Operation(summary = "get an user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200" , description = "get an user "),
            @ApiResponse(responseCode = "404",description = "les informations de correction sont incorrectes")
    })
    public ResponseEntity<ToKens> getUser(@RequestBody UserDto userDto) throws BusinessException{
        try {
            ToKens toKens =userService.login(userDto);
            return new ResponseEntity<>(toKens,HttpStatus.OK);
        }
        catch (IllegalArgumentException e){
            ToKens error=new ToKens(e.getMessage(),"");
            return new ResponseEntity<>(error,HttpStatus.NOT_FOUND);
        }

    }

    /**
     * Update user response entity.
     *
     * @param userDto the user dto
     * @param id      the id
     * @return the response entity
     * @throws BusinessException the business exception
     */
    @PutMapping("/{id}")
    @Operation(summary = "change ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200" , description = "updating is successfully"),
            @ApiResponse(responseCode = "404", description = "the user not found")
    })
    public ResponseEntity<UserDto> updateUser(@Valid @RequestBody UserDto userDto, UUID id) throws BusinessException{

            UserDto user=userService.updateUser(userDto,id);
            return new ResponseEntity<>(user,HttpStatus.OK);
    }

    /**
     * Refresh tokens response entity.
     *
     * @param refreshToken the refresh token
     * @return the response entity
     * @throws BusinessException the business exception
     */
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

    /**
     * Refresh access token response entity.
     *
     * @param refreshToken the refresh token
     * @return the response entity
     * @throws BusinessException the business exception
     */
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
