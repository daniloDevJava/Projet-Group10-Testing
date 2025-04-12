package com.projet.testing.vehicule.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VendeurServiceImpl implements VendeurService {

    @Autowired
    private VehiculeRepository repository;

    @Override
    public List<Vehicule> getAllVehicules() {
        return repository.findAll();
    }

    @Override
    public Optional<Vehicule> getVehiculeById(String registrationNumber) {
        return repository.findById(registrationNumber);
    }

    @Override
    public Vehicule createVehicule(Vehicule vehicule) {
        return repository.save(vehicule);
    }

    @Override
    public Vehicule updateVehicule(String registrationNumber, Vehicule vehicule) {
        if (repository.existsById(registrationNumber)) {
            vehicule.setRegistrationNumber(registrationNumber);
            return repository.save(vehicule);
        } else {
            throw new RuntimeException("Véhicule non trouvé avec l'immatriculation: " + registrationNumber);
        }
    }

    @Override
    public void deleteVehicule(String registrationNumber) {
        repository.deleteById(registrationNumber);
    }

    @Override
    public Optional<Vehicule> searchByRegistration(String registrationNumber) {
        return repository.findById(registrationNumber);
    }

    @Override
    public List<Vehicule> searchByPrice(double maxPrice) {
        return repository.findByRentalPriceLessThanEqual(maxPrice);
    }
}
