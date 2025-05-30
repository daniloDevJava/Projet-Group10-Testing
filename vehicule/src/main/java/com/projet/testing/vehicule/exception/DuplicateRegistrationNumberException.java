package com.projet.testing.vehicule.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST) // Ceci mappe l'exception à un statut HTTP 400
public class DuplicateRegistrationNumberException extends RuntimeException {
    public DuplicateRegistrationNumberException(String message) {
        super(message);
    }
}