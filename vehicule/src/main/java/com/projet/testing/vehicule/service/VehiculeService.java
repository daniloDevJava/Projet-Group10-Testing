package com.projet.testing.vehicule.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.projet.testing.vehicule.dto.VehiculeDto;

@Service
public interface VehiculeService {
	
	VehiculeDto createVehicule(VehiculeDto vehiculeDto);
	
	VehiculeDto getVehicule(UUID vehicule_id);
	
	VehiculeDto getVehicule(String registerNumber);
	
	List<VehiculeDto> getVehicule(double rentalPrice);
	
	List<VehiculeDto> getAllVehicule();
	 
	// mise a jour totale de vehicule
	VehiculeDto updateVehicule(VehiculeDto vehicule ,UUID vehicule_id);
	
	boolean deleteVehicule(UUID vehicule_id);

}
