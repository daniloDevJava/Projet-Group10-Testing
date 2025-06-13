package com.projet.testing.vehicule.service.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import com.projet.testing.vehicule.dto.ImagesDto;
import com.projet.testing.vehicule.exception.BusinessException;
import com.projet.testing.vehicule.exception.ErrorModel;
import com.projet.testing.vehicule.mapper.ImageMapper;
import com.projet.testing.vehicule.model.Images;
import com.projet.testing.vehicule.repository.ImageRepository;
import com.projet.testing.vehicule.service.VehiculeService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.projet.testing.vehicule.dto.VehiculeDto;
import com.projet.testing.vehicule.model.Vehicule;
import com.projet.testing.vehicule.repository.VehiculeRepository;
import com.projet.testing.vehicule.mapper.VehiculeMapper;
import org.springframework.web.multipart.MultipartFile;

/**
 * The type Vehicule service.
 */
@Service
@AllArgsConstructor
public class VehiculeServiceImpl implements VehiculeService {

	private final VehiculeRepository vehiculeRepository;
	private final VehiculeMapper vehiculeMapper;
	private final ImageRepository imageRepository;
	private final ImageMapper imageMapper;



	@Override
	public VehiculeDto createVehicule(VehiculeDto vehiculeDto) {
		Optional<Vehicule> optionalVehicule = vehiculeRepository.findByRegistrationNumber(vehiculeDto.getRegistrationNumber());
		if (optionalVehicule.isPresent()) {
			throw new IllegalArgumentException("Il existe deja des vehicules avec ces numeros de registration");
		}

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

	@Override
	public ImagesDto addImage(MultipartFile file, UUID vehiculeId) throws IOException {
		Optional<Vehicule> optionalVehicule=vehiculeRepository.findById(vehiculeId);
		if(optionalVehicule.isPresent()){
			Vehicule vehicule=optionalVehicule.get();
			String imageDirectory = "images/vehicules/";
			Path directoryPath = Paths.get(imageDirectory);
			if (!Files.exists(directoryPath)) {
					Files.createDirectories(directoryPath);
				}
			// Créer le chemin du fichier
			String fileName = file.getOriginalFilename();
			System.out.println(fileName);
			assert fileName != null;
			if(!fileName.contains(".jpeg") && !fileName.contains(".png") && !fileName.contains(".jpg")){
				ErrorModel errorModel=new ErrorModel();
				errorModel.setCode("FILE_NOT_FOUND");
				errorModel.setMessage("le format du fichier image n'est pas pris en compte ");
				throw new BusinessException(List.of(errorModel), HttpStatus.BAD_REQUEST);
			}
			Path filePath = directoryPath.resolve(fileName);
			// Sauvegarder le fichier
			Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
			Images image=new Images();
			image.setVehicule(vehicule);
			image.setChemin(directoryPath + "/"+fileName);
			vehicule.setCheminVersImage(image.getChemin());
			vehiculeRepository.save(vehicule);
			image=imageRepository.save(image);
			image.makeName();
			return imageMapper.toDto(image);

		}
		else
			throw new IllegalArgumentException("vehicule non trouvé");

	}
}
