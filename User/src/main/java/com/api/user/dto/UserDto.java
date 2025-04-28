package com.projet.testing.user.dto;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDto {

    private UUID id;

    @NotNull(message = "Name is mandatory")
    @NotEmpty(message = "Name cannot be empty")
    private String name;

    @NotNull(message = "Password is mandatory")
    @NotEmpty(message = "Password cannot be empty")
    private String password;
}
