package com.projet.testing.vehicule.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ImagesDto {

    private Long id;
    private String chemin;
    private String nom;

    public void makeName(){
        this.nom="IMG"+ 0 +id;
    }
}
