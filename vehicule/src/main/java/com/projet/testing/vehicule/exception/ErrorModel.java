package com.projet.testing.vehicule.exception;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * The type Error model.
 */
@Getter
@Setter
@ToString
public class ErrorModel {
    private String code;
    private String message;
}
