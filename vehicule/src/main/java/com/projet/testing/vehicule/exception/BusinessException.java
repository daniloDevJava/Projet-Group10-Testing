package com.projet.testing.vehicule.exception;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BusinessException extends Exception{
    List<ErrorModel> errorModels;

    public BusinessException(List<ErrorModel> errorModels){
        this.errorModels=errorModels;
    }
}
