package com.projet.testing.vehicule.service.impl;

import com.projet.testing.vehicule.exception.BusinessException;
import com.projet.testing.vehicule.exception.ErrorModel;
import com.projet.testing.vehicule.model.Jwt;
import com.projet.testing.vehicule.model.RefreshToken;
import com.projet.testing.vehicule.model.User;
import com.projet.testing.vehicule.repository.JwtRepository;
import com.projet.testing.vehicule.repository.RefreshTokenRepository;
import com.projet.testing.vehicule.service.JwtService;
import com.projet.testing.vehicule.service.ToKens;
import com.projet.testing.vehicule.util.JwtUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * The type Jwt service.
 */
@Service
@AllArgsConstructor
public class JwtServiceImpl implements JwtService {

    private final JwtRepository jwtRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtil jwtUtil;


    @Override
    public ToKens generateTokens(User utilisateur,long refreshTokenValidity) {
         refreshTokenValidity = TimeUnit.DAYS.toSeconds(refreshTokenValidity);

        String accessToken = jwtUtil.generateAccessToken(utilisateur.getEmail());
        String refreshTokenValue = jwtUtil.generateRefreshToken(utilisateur.getEmail(), refreshTokenValidity);

        RefreshToken refreshToken = RefreshToken.builder()
                .valeur(refreshTokenValue)
                .creation(Instant.now())
                .expiration(Instant.now().plusSeconds(refreshTokenValidity))
                .expire(false)
                .build();
        refreshToken = refreshTokenRepository.save(refreshToken);

        Jwt jwt = Jwt.builder()
                .valeur(accessToken)
                .expire(false)
                .desactive(false)
                .refreshToken(refreshToken)
                .utilisateur(utilisateur)
                .build();
        jwtRepository.save(jwt);

        return new ToKens(accessToken,refreshToken.getValeur());

    }

    @Override
    public ToKens refreshTokens(String refreshTokenValue,long time) throws BusinessException {
        Optional<RefreshToken> optionalrefreshToken = refreshTokenRepository.findByValeurAndExpireFalse(refreshTokenValue);
        if (optionalrefreshToken.isPresent()) {
            RefreshToken refreshToken = optionalrefreshToken.get();
            if (refreshToken.getExpiration().isBefore(Instant.now())) {
                Optional<Jwt> optionalJwt = jwtRepository.findByRefreshToken(refreshToken);
                if (optionalJwt.isPresent()) {
                    User utilisateur = optionalJwt.get().getUtilisateur();
                    if (utilisateur == null) {
                        ErrorModel errorModel = new ErrorModel();
                        errorModel.setCode("FAILED_AUTHENTIFICATION");
                        errorModel.setMessage("Aucun utilisateur associé à ce RefreshToken");
                        throw new BusinessException(List.of(errorModel), HttpStatus.NOT_FOUND);
                    }
                    refreshToken.setExpire(true);
                    refreshTokenRepository.save(refreshToken);
                    return generateTokens(utilisateur,time);
                }

            }
            ErrorModel errorModel = new ErrorModel();
            errorModel.setCode("UNAUTHORIZED");
            errorModel.setMessage("refresh token pas encore expiré");
            throw new BusinessException(List.of(errorModel),HttpStatus.UNAUTHORIZED);


        }
        ErrorModel errorModel = new ErrorModel();
        errorModel.setCode("INVALID_ENTRY");
        errorModel.setMessage("refresh token inconnu");
        throw new BusinessException(List.of(errorModel),HttpStatus.NOT_FOUND);
    }

    @Override
    public String refreshAccessToken(String refreshTokenValue) throws BusinessException{

        Optional<RefreshToken> optionalrefreshToken = refreshTokenRepository.findByValeurAndExpireFalse(refreshTokenValue);
        List<ErrorModel> errorModels=new ArrayList<>();
        if (optionalrefreshToken.isPresent()) {
            RefreshToken refreshToken = optionalrefreshToken.get();
            if (!refreshToken.getExpiration().isBefore(Instant.now())) {
                Optional<Jwt> optionalJwt = jwtRepository.findByRefreshToken(refreshToken);
                if (optionalJwt.isPresent()) {
                    User utilisateur = optionalJwt.get().getUtilisateur();
                    if (utilisateur == null) {
                        ErrorModel errorModel = new ErrorModel();
                        errorModel.setCode("FAILED_AUTHENTIFICATION");
                        errorModel.setMessage("Aucun utilisateur associé à ce RefreshToken");
                        errorModels.add(errorModel);
                        throw new BusinessException(errorModels,HttpStatus.NOT_FOUND);
                    }

                    return jwtUtil.generateAccessToken(utilisateur.getEmail());
                }
            }
            ErrorModel errorModel = new ErrorModel();
            errorModel.setCode("UNAUTHORIZED");
            errorModel.setMessage("refresh token expiré");
            throw new BusinessException(List.of(errorModel),HttpStatus.FORBIDDEN);

        }
        ErrorModel errorModel = new ErrorModel();
        errorModel.setCode("INVALID_ENTRY");
        errorModel.setMessage("refresh token inconnu");
        throw new BusinessException(List.of(errorModel),HttpStatus.NOT_FOUND);
    }
}
