package com.projet.testing.vehicule.controller;

import com.projet.testing.vehicule.dto.ImagesDto;
import com.projet.testing.vehicule.exception.BusinessException;
import com.projet.testing.vehicule.exception.ErrorModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.projet.testing.vehicule.dto.VehiculeDto;
import com.projet.testing.vehicule.service.VehiculeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;


/**
 * The type Vehicule controller.
 */
@Slf4j
@RestController
@RequestMapping("/vehicule")
@AllArgsConstructor
public class VehiculeController {
	
	private final VehiculeService vehiculeService;


	/**
	 * Create vehicule response entity.
	 *
	 * @param vehiculeDto the vehicule dto
	 * @return the response entity
	 */
	@PostMapping("/create")
	@Operation(summary = "add an vehicule")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201" , description = "le velicule a ete ajoutee"),
			@ApiResponse(responseCode = "400", description = "Bad entry of data .")
			
	})
	public ResponseEntity<VehiculeDto> createVehicule(@Valid @RequestBody VehiculeDto vehiculeDto){
		    try {
				VehiculeDto vehicule = vehiculeService.createVehicule(vehiculeDto);
				return new ResponseEntity<>(vehicule ,HttpStatus.CREATED);
			}

			catch (IllegalArgumentException e){
				ErrorModel errorModel=new ErrorModel();
				errorModel.setCode("UNAUTHORIZED_REQUEST");
				errorModel.setMessage(e.getMessage());

				throw new BusinessException(List.of(errorModel),HttpStatus.FORBIDDEN);
			}

	}

	/**
	 * Get all response entity.
	 *
	 * @return the response entity
	 */
	@GetMapping("/all")
	@Operation(summary= "get all vehicules")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "la liste de vehicules trouvee")
	})
	public ResponseEntity<List<VehiculeDto>> getAll(){
		
		List<VehiculeDto> listVehicule=vehiculeService.getAllVehicule();
		
		return new ResponseEntity<>(listVehicule ,HttpStatus.OK);
		
	}

	/**
	 * Gets vehicule by id.
	 *
	 * @param id the id
	 * @return the vehicule by id
	 */
	@GetMapping("/id/{id}")
	@Operation(summary= "get an vehicule by id")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "le vehicule a ete trouve avec success"),
			@ApiResponse(responseCode = "404", description =  "le vehicule n'a pas ey=te trouve")
			
	})
	public ResponseEntity<VehiculeDto> getVehiculeById(@Parameter(description = "id of vehicule") @PathVariable UUID id){
		try {
			VehiculeDto vehicule= vehiculeService.getVehiculeById(id);
			return new ResponseEntity<>(vehicule ,HttpStatus.OK);
		}
		catch (RuntimeException e){
			ErrorModel errorModel=new ErrorModel();
			errorModel.setCode("BAD_ARGUMENTS");
			errorModel.setMessage(e.getMessage());
			throw new BusinessException(List.of(errorModel),HttpStatus.NOT_FOUND);
		}


	}

	/**
	 * Gets vehicule by number.
	 *
	 * @param registerNum the register num
	 * @return the vehicule by number
	 */
	@GetMapping("/number/{registerNum}")
	@Operation(summary= "get an vehicule by number")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "le vehicule  a ete trouve avec success"),
			@ApiResponse(responseCode = "404", description =  "le vehicule n'a pas ete trouve")
			
	})
	public ResponseEntity<VehiculeDto> getVehiculeByNumber(@Parameter(description = "Number of vehicule") @PathVariable String registerNum){
		try {
			VehiculeDto vehicule= vehiculeService.getVehiculeByNumber(registerNum);
			return new ResponseEntity<>(vehicule ,HttpStatus.OK);
		}
		catch (RuntimeException e){
			ErrorModel errorModel=new ErrorModel();
			errorModel.setCode("BAD_ARGUMENTS");
			errorModel.setMessage(e.getMessage());
			throw new BusinessException(List.of(errorModel),HttpStatus.NOT_FOUND);
		}

	}

	/**
	 * Update vehicule response entity.
	 *
	 * @param vehicule the vehicule
	 * @param id       the id
	 * @return the response entity
	 */
	@PutMapping("/{id}")
	@Operation(summary = "update vehicule")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200" , description = "le vehicule a ete mis a jour avec success"),
			@ApiResponse(responseCode = "404" , description = "le vehicule n'existe pas"),
			@ApiResponse(responseCode = "400", description = "Bad entry of data .")
	})
	public ResponseEntity<VehiculeDto> updateVehicule(@Valid @RequestBody VehiculeDto vehicule, @Parameter(description = "id of vehicule") @PathVariable UUID id){
		try {
			VehiculeDto vehiculeDto = vehiculeService.updateVehicule(vehicule, id);
			return new ResponseEntity<>(vehiculeDto , HttpStatus.OK);
		}
		catch (RuntimeException e){
			ErrorModel errorModel=new ErrorModel();
			errorModel.setCode("BAD_ARGUMENTS");
			errorModel.setMessage(e.getMessage());
			throw new BusinessException(List.of(errorModel),HttpStatus.NOT_FOUND);
		}





	}

	/**
	 * Gets all byprice.
	 *
	 * @param rentalPrice the rental price
	 * @return the all byprice
	 */
	@GetMapping("/search-by-price")
	@Operation(summary= "get vehicules by price")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "la liste de vehicules trouvee")
	})
	public ResponseEntity<List<VehiculeDto>> getAllByprice(@Parameter(description = "the rental price of vehicule") @RequestParam Double rentalPrice){
		
		List<VehiculeDto> listVehicule=vehiculeService.getVehicule(rentalPrice);
		
		return new ResponseEntity<>(listVehicule ,HttpStatus.OK);
		
	}
	
	@PostMapping("/upload/images")
	@Operation(summary = "add an vehicule")
	@ApiResponses(value={
		@ApiResponse(responseCode="201", description="images is uploaded"),
		@ApiResponse(responseCode="500", description="Input or Output exceptions")
	})
	public ResponseEntity<ImagesDto> createImage(@RequestParam("file")MultipartFile file,@Parameter(description = "the id of product") @RequestParam("vehiculeId")UUID vehiculeId){
            try{
                ImagesDto imagesDto= vehiculeService.addImage(file,vehiculeId);
                return new ResponseEntity<>(imagesDto,HttpStatus.CREATED);
            }
            catch (IOException e){
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
            catch (IllegalArgumentException e){
                System.err.println(e.getMessage());
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
	/**
	 * Delete admin response entity.
	 *
	 * @param id the id
	 * @return the response entity
	 */
	@DeleteMapping("/{id}")
	 @Operation(summary = "delete vehicule")
    public ResponseEntity<String> deleteVehicule(@Parameter(description = "Id Of vehicule") @PathVariable UUID id){
        if(vehiculeService.deleteVehicule(id))
            return new ResponseEntity<>("\"message\" : \"vehicule is deleted successfully\"",HttpStatus.OK);
        else
            return new ResponseEntity<>("\"message\" : \"vehicule doesn't exists\"",HttpStatus.NOT_FOUND);
    }
	
	
}
