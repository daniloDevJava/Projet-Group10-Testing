package com.projet.testing.vehicule.service;

import com.projet.testing.vehicule.exception.BusinessException;
import com.projet.testing.vehicule.model.User;
import org.springframework.stereotype.Service;

@Service
public interface JwtService {
     ToKens generateTokens(User user);
     ToKens refreshTokens(String refreshToken) throws BusinessException;
     String refreshAccessToken(String refreshToken) throws BusinessException;
}
