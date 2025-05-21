package com.projet.testing.vehicule.service.impl;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.projet.testing.vehicule.service.VehiculeService;
import org.springframework.stereotype.Service;

import com.projet.testing.vehicule.dto.VehiculeDto;
import com.projet.testing.vehicule.model.Vehicule;
import com.projet.testing.vehicule.repository.VehiculeRepository;
import com.projet.testing.vehicule.mapper.VehiculeMapper;

@Service
public class VehiculeServiceImpl implements VehiculeService {

	private final VehiculeRepository vehiculeRepository;
	private final VehiculeMapper vehiculeMapper;

	public VehiculeServiceImpl(VehiculeRepository vehiculeRepository, VehiculeMapper vehiculeMapper) {
		this.vehiculeRepository = vehiculeRepository;
		this.vehiculeMapper = vehiculeMapper;
	}

	@Override
	public VehiculeDto createVehicule(VehiculeDto vehiculeDto) {
		Vehicule vehicule = vehiculeMapper.toEntity(vehiculeDto);
		Vehicule savedVehicule = vehiculeRepository.save(vehicule);
		return vehiculeMapper.toDto(savedVehicule);
	}

	@Override
	public VehiculeDto getVehiculeById(UUID vehicule_id) {
		Vehicule vehicule = vehiculeRepository.findById(vehicule_id)
				.orElseThrow(() -> new RuntimeException("Véhicule non trouvé"));
		return vehiculeMapper.toDto(vehicule);
	}

	@Override
	public VehiculeDto getVehiculeByNumber(String registrationNumber) {
		Vehicule vehicule = vehiculeRepository.findByRegistrationNumber(registrationNumber)
				.orElseThrow(() -> new RuntimeException("Véhicule non trouvé"));
		return vehiculeMapper.toDto(vehicule);
	}

	@Override
	public List<VehiculeDto> getVehicule(double rentalPrice) {
		return vehiculeRepository.findByRentalPrice(rentalPrice)
				.stream()
				.map(vehiculeMapper::toDto)
				.collect(Collectors.toList());
	}

	@Override
	public List<VehiculeDto> getAllVehicule() {
		return vehiculeRepository.findAll()
				.stream()
				.map(vehiculeMapper::toDto)
				.collect(Collectors.toList());
	}

	@Override
	public VehiculeDto updateVehicule(VehiculeDto vehiculeDto, UUID vehicule_id) {
		Vehicule vehicule = vehiculeRepository.findById(vehicule_id)
				.orElseThrow(() -> new RuntimeException("Véhicule non trouvé"));

		// Mise à jour des champs
		vehicule.setRegistrationNumber(vehiculeDto.getRegistrationNumber());
		vehicule.setMake(vehiculeDto.getMake());
		vehicule.setModel(vehiculeDto.getModel());
		vehicule.setYear(vehiculeDto.getYear() );
		vehicule.setRentalPrice(vehiculeDto.getRentalPrice());

		Vehicule updated = vehiculeRepository.save(vehicule);
		return vehiculeMapper.toDto(updated);
	}

	@Override
	public boolean deleteVehicule(UUID vehicule_id) {
		Vehicule vehicule = vehiculeRepository.findById(vehicule_id)
				.orElseThrow(() -> new RuntimeException("Véhicule non trouvé"));
		vehiculeRepository.delete(vehicule);
		return true;
	}
}
