package com.projet.testing.vehicule.service;

import com.projet.testing.vehicule.exception.BusinessException;
import com.projet.testing.vehicule.model.User;
import org.springframework.stereotype.Service;

/**
 * The interface Jwt service.
 */
@Service
public interface JwtService {
    /**
     * Generate tokens to kens.
     *
     * @param user the user
     * @return the to kens
     */
    ToKens generateTokens(User user);

    /**
     * Refresh tokens to kens.
     *
     * @param refreshToken the refresh token
     * @return the to kens
     * @throws BusinessException the business exception
     */
    ToKens refreshTokens(String refreshToken) throws BusinessException;

    /**
     * Refresh access token string.
     *
     * @param refreshToken the refresh token
     * @return the string
     * @throws BusinessException the business exception
     */
    String refreshAccessToken(String refreshToken) throws BusinessException;
}
