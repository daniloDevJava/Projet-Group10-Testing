package com.projet.testing.vehicule.dto;

import java.util.UUID;

<<<<<<< HEAD

=======
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
>>>>>>> imelda
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
<<<<<<< HEAD
=======
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
>>>>>>> imelda
public class VehiculeDto {
	
	
	private UUID id;
	
<<<<<<< HEAD
	
	private String registrationNumber;
	
=======
	@NotNull(message = "registration number is mandatory")
	@NotEmpty
	private String registrationNumber;
	
	@NotNull(message = " make is mandatory")
>>>>>>> imelda
	
	private String make;
	
	private String model;
	
	
	//l'annee de creation du vehicule
	private int year;
	
	private double rentalPrice;
	
	

}
