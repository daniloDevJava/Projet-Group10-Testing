package com.projet.testing.vehicule.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

/**
 * The type User dto.
 */
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
public class UserDto {
    private UUID id;

    @NotNull(message = "email is mandatory")
    @Size(min = 1,max = 50,message = "la taille du mail doit etre entre 1 et 50 caracteres")
    private String email;
    @NotNull(message = "name is mandatory")
    private String name;
    @Size(min = 8, message = "le mot de passe est court")
    private String mdp;
}
