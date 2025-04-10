package com.projet.testing.vehicule.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
import com.projet.testing.vehicule.model.Vehicule;

public interface VendeurRepository extends JpaRepository<Vehicule, UUID> {
	Optional<Vehicule> findByRegistration(String registration);
	
	Optional<Vehicule> findByRentalPrice(double price);
	

}
