package com.projet.testing.vehicule.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
import com.projet.testing.vehicule.model.Vehicule;

public interface VehiculeRepository extends JpaRepository<Vehicule, UUID> {
	Optional<Vehicule> findByRegistrationNumber(String registration);
	
	List<Vehicule> findByRentalPrice(double price);
	

}
