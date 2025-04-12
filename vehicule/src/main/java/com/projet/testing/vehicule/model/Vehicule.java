package com.projet.testing.vehicule.model;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Vehicule {
	
	@Id
	@GeneratedValue(strategy=GenerationType.UUID)
	private UUID id;
	
	@Column(nullable=false)
	private String registrationNumber;
	
	
	private String make;
	
	private String model;
	
	
	//l'annee de creation du vehicule
	private int year;
	
	private double rentalPrice;
	
	

}