package com.projet.testing.vehicule.mapper;

import com.projet.testing.vehicule.dto.ImagesDto;
import com.projet.testing.vehicule.model.Images;
import org.springframework.stereotype.Component;

@Component
public class ImageMapper {
    public ImagesDto toDto(Images images){
        ImagesDto imagesDto=new ImagesDto();
        imagesDto.setId(images.getId());
        imagesDto.setNom(images.getNom());
        imagesDto.setChemin(images.getChemin());
        return imagesDto;
    }
}
