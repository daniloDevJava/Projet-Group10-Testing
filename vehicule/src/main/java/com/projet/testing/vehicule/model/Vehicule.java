package com.projet.testing.vehicule.model;

import java.util.List;
import java.util.UUID;
import java.util.ArrayList;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * The type Vehicule.
 */
@Entity
@Getter
@Setter
public class Vehicule {
	
	@Id
	@GeneratedValue(strategy=GenerationType.UUID)
	private UUID id;

	@Column(nullable=false ,unique=true)
	private String registrationNumber;
	
	@OneToMany(cascade = CascadeType.ALL,orphanRemoval = true)
	private List<Images> images=new ArrayList<>();
	
	private String make;
	
	private String model;
	private String cheminVersImage;
	@Column(name = "anee")
	//l'annee de creation du vehicule
	private int year;
	
	private double rentalPrice;

}

