package com.projet.testing.vehicule.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
import com.projet.testing.vehicule.model.Vehicule;

/**
 * The interface Vehicule repository.
 */
public interface VehiculeRepository extends JpaRepository<Vehicule, UUID> {
    /**
     * Find by registration number optional.
     *
     * @param registration the registration
     * @return the optional
     */
    Optional<Vehicule> findByRegistrationNumber(String registration);

    /**
     * Find by rental price list.
     *
     * @param price the price
     * @return the list
     */
    List<Vehicule> findByRentalPrice(double price);
	

}
