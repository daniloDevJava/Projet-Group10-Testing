package com.projet.testing.vehicule.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * The type Tokens.
 */
@Getter
@Setter
@AllArgsConstructor
public class ToKens {
    /**
     * The Access token.
     */
    String accessToken;
    /**
     * The Refresh token.
     */
    String refreshToken;

}
