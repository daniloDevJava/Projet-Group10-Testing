package com.projet.testing.vehicule.controller;

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
import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequestMapping("/vehicule")
@AllArgsConstructor
public class VehiculeController {
	
	private final VehiculeService vehiculeService;
	
	
	@PostMapping("/create")
	@Operation(summary = "add an vehicule")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201" , description = "le velicule a ete ajoutee"),
			@ApiResponse(responseCode = "400", description = "Bad entry of data .")
			
	})
	public ResponseEntity<VehiculeDto> createVehicule(@Valid @RequestBody VehiculeDto vehiculeDto){
	
			VehiculeDto vehicule = vehiculeService.createVehicule(vehiculeDto);
			return new ResponseEntity<>(vehicule ,HttpStatus.CREATED);
	}
	
	@GetMapping("/all")
	@Operation(summary= "get all vehicules")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "la liste de vehicules trouvee")
	})
	public ResponseEntity<List<VehiculeDto>> getAll(){
		
		List<VehiculeDto> listVehicule=vehiculeService.getAllVehicule();
		
		return new ResponseEntity<>(listVehicule ,HttpStatus.OK);
		
	}
	
	@GetMapping("/id/{id}")
	@Operation(summary= "get an vehicule by id")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "le vehicule a ete trouve avec success"),
			@ApiResponse(responseCode = "404", description =  "le vehicule n'a pas ey=te trouve")
			
	})
	public ResponseEntity<VehiculeDto> getVehiculeById(@Parameter(description = "id of vehicule") @PathVariable UUID id){
		
		VehiculeDto vehicule= vehiculeService.getVehiculeById(id);
		if(vehicule!=null)
			return new ResponseEntity<>(vehicule ,HttpStatus.OK);
		else
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
	@GetMapping("/number/{registerNum}")
	@Operation(summary= "get an vehicule by number")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "le vehicule  a ete trouve avec success"),
			@ApiResponse(responseCode = "404", description =  "le vehicule n'a pas ete trouve")
			
	})
	public ResponseEntity<VehiculeDto> getVehiculeByNumber(@Parameter(description = "Number of vehicule") @PathVariable String registerNum){
		
		VehiculeDto vehicule= vehiculeService.getVehiculeByNumber(registerNum);
		if(vehicule!=null) 
			return new ResponseEntity<>(vehicule ,HttpStatus.OK);
		else
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
	
	@PutMapping("/{id}")
	@Operation(summary = "update vehicule")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200" , description = "le vehicule a ete mis a jour avec success"),
			@ApiResponse(responseCode = "404" , description = "le vehicule n'existe pas"),
			@ApiResponse(responseCode = "400", description = "Bad entry of data .")
	})
	public ResponseEntity<VehiculeDto> updateVehicule(@Valid @RequestBody VehiculeDto vehicule, @Parameter(description = "id of vehicule") @PathVariable UUID id){
		VehiculeDto vehiculeDto = vehiculeService.updateVehicule(vehicule, id);
		
		if (vehiculeDto != null) {
			return new ResponseEntity<>(vehiculeDto , HttpStatus.OK);
		}
		else
			return new ResponseEntity<>(vehiculeDto , HttpStatus.NOT_FOUND);
	}
	
	@GetMapping("/search-by-price")
	@Operation(summary= "get vehicules by price")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "la liste de vehicules trouvee")
	})
	public ResponseEntity<List<VehiculeDto>> getAllByprice(@Parameter(description = "the rental price of vehicule") @RequestParam Double rentalPrice){
		
		List<VehiculeDto> listVehicule=vehiculeService.getVehicule(rentalPrice);
		
		return new ResponseEntity<>(listVehicule ,HttpStatus.OK);
		
	}
	
	@DeleteMapping("/{id}")
	 @Operation(summary = "delete admin")
    public ResponseEntity<String> deleteAdmin(@Parameter(description = "Id Of vehicule") @PathVariable UUID id){
        if(vehiculeService.deleteVehicule(id))
            return new ResponseEntity<>("{\"message\" : \"vehicule is deleted successfully\"}",HttpStatus.OK);
        else
            return new ResponseEntity<>("{\"message\" : \"vehicule doesn't exists\"}",HttpStatus.NOT_FOUND);
    }
	
	
}
