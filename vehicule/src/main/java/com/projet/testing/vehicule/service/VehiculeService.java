package com.projet.testing.vehicule.service;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import com.projet.testing.vehicule.dto.ImagesDto;
import org.springframework.stereotype.Service;

import com.projet.testing.vehicule.dto.VehiculeDto;
import org.springframework.web.multipart.MultipartFile;

/**
 * The interface Vehicule service.
 */
@Service
public interface VehiculeService {

	/**
	 * Create vehicule vehicule dto.
	 *
	 * @param vehiculeDto the vehicule dto
	 * @return the vehicule dto
	 */
	VehiculeDto createVehicule(VehiculeDto vehiculeDto);

	/**
	 * Gets vehicule by id.
	 *
	 * @param vehicule_id the vehicule id
	 * @return the vehicule by id
	 */
	VehiculeDto getVehiculeById(UUID vehicule_id);

	/**
	 * Gets vehicule by number.
	 *
	 * @param registerNumber the register number
	 * @return the vehicule by number
	 */
	VehiculeDto getVehiculeByNumber(String registerNumber);

	/**
	 * Gets vehicule.
	 *
	 * @param rentalPrice the rental price
	 * @return the vehicule
	 */
	List<VehiculeDto> getVehicule(double rentalPrice);

	/**
	 * Gets all vehicule.
	 *
	 * @return the all vehicule
	 */
	List<VehiculeDto> getAllVehicule();

	/**
	 * Update vehicule vehicule dto.
	 *
	 * @param vehicule    the vehicule
	 * @param vehicule_id the vehicule id
	 * @return the vehicule dto
	 */
// mise a jour totale de vehicule
	VehiculeDto updateVehicule(VehiculeDto vehicule ,UUID vehicule_id);

	/**
	 * Delete vehicule boolean.
	 *
	 * @param vehicule_id the vehicule id
	 * @return the boolean
	 */
	boolean deleteVehicule(UUID vehicule_id);

	ImagesDto addImage(MultipartFile file,UUID vehiculeId) throws IOException;

}
