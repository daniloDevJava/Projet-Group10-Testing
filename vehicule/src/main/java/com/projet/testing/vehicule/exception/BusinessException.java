package com.projet.testing.vehicule.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.List;

/**
 * The type Business exception.
 */
@Getter
@Setter
@NoArgsConstructor
public class BusinessException extends RuntimeException{
    /**
     * The Error models.
     */
    List<ErrorModel> errorModels;
    /**
     * The Status.
     */
    HttpStatus status;

    /**
     * Instantiates a new Business exception.
     *
     * @param errorModels the error models
     * @param status      the status
     */
    public BusinessException(List<ErrorModel> errorModels , HttpStatus status){
        this.errorModels=errorModels;
        this.status=status;
    }
}
