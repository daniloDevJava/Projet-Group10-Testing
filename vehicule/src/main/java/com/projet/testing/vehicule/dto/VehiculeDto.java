package com.projet.testing.vehicule.dto;

import java.util.UUID;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;


/**
 * The type Vehicule dto.
 */

@Getter
@Setter

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class VehiculeDto {
	
	
	private UUID id;
	

	

	@NotBlank(message = "Le numéro d'immatriculation ne peut pas être vide")
	private String registrationNumber;
	
	@NotNull(message = "make is mandatory")


	

	
	

	

	@NotNull(message = "make is mandatory")
	private String make;
    private String cheminVersImage;
	private String model;

	@Positive(message="L'année ne peut etre negative")
	// l'année de création du véhicule
	private int year;

	@PositiveOrZero(message = "le prix d'achat ne peut etre negatif")

	private double rentalPrice;
	
	

}
