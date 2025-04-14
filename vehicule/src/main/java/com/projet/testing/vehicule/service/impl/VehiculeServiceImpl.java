package com.projet.testing.vehicule.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.projet.testing.vehicule.dto.VehiculeDto;
import com.projet.testing.vehicule.service.VehiculeService;

@Service
public class VehiculeServiceImpl implements VehiculeService {

	@Override
	public VehiculeDto createVehicule(VehiculeDto vehiculeDto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VehiculeDto getVehicule(UUID vehicule_id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VehiculeDto getVehicule(String registerNumber) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<VehiculeDto> getVehicule(double rentalPrice) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<VehiculeDto> getAllVehicule() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VehiculeDto updateVehicule(VehiculeDto vehicule, UUID vehicule_id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean deleteVehicule(UUID vehicule_id) {
		// TODO Auto-generated method stub
		return false;
	}

}
