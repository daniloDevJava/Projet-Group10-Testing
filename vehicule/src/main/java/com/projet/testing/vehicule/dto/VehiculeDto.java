package com.projet.testing.vehicule.dto;

import java.util.UUID;


import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class VehiculeDto {
	
	
	private UUID id;
	
	
	private String registrationNumber;
	
	
	private String make;
	
	private String model;
	
	
	//l'annee de creation du vehicule
	private int year;
	
	private double rentalPrice;
	
	

}
