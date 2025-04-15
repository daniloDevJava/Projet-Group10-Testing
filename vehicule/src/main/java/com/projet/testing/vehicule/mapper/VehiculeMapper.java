package com.projet.testing.vehicule.mapper;

import org.springframework.stereotype.Component;

import com.projet.testing.vehicule.dto.VehiculeDto;
import com.projet.testing.vehicule.model.Vehicule;

import lombok.Getter;
import lombok.Setter;

@Component
@Getter
@Setter
public class VehiculeMapper {
	
	public Vehicule toEntity(VehiculeDto vehiculeDto) {
		
		Vehicule vehicule = new Vehicule();
		vehicule.setRegistrationNumber(vehiculeDto.getRegistrationNumber());
		vehicule.setMake(vehiculeDto.getMake());
		vehicule.setModel(vehiculeDto.getModel());
		vehicule.setYear(vehiculeDto.getYear());
		vehicule.setRentalPrice(vehiculeDto.getRentalPrice());
		
		return vehicule;
	}
	
	public VehiculeDto toDto(Vehicule vehicule) {
		
		VehiculeDto vehiculeDto= new VehiculeDto();
		
		vehiculeDto.setId(vehicule.getId());
		vehiculeDto.setRegistrationNumber(vehicule.getRegistrationNumber());
		vehiculeDto.setMake(vehicule.getMake());
		vehiculeDto.setModel(vehicule.getModel());
		vehiculeDto.setYear(vehicule.getYear());
		vehiculeDto.setRentalPrice(vehicule.getRentalPrice());
		
		return vehiculeDto;
	}

}
