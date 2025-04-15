package com.projet.testing.vehicule.dto;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class VehiculeDto {
	
	
	private UUID id;
	
	
	private String registrationNumber;
	
	
	private String make;
	
	private String model;
	
	
	//l'annee de creation du vehicule
	private int year;
	
	private double rentalPrice;
	
	

}
