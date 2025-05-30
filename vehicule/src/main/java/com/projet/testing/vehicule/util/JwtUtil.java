package com.projet.testing.vehicule.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    private final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    /**
     * Génère un AccessToken pour un utilisateur.
     *
     * @param email L'adresse email de l'utilisateur
     * @return Le AccessToken généré
     */
    public String generateAccessToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date()) // Date de création
                .setExpiration(new Date(System.currentTimeMillis() + 15 * 60 * 1000)) // Expiration après 15 minutes
                .signWith(SECRET_KEY)
                .compact();
    }

    /**
     * Génère un RefreshToken pour un utilisateur.
     *
     * @param email L'adresse email de l'utilisateur
     * @param validityInSeconds Durée de validité du RefreshToken en secondes
     * @return Le RefreshToken généré
     */
    public String generateRefreshToken(String email, long validityInSeconds) {
        return Jwts.builder()
                .setSubject(email)
                .setId(generateUniqueId()) // ID unique pour le RefreshToken
                .setIssuedAt(new Date()) // Date de création
                .setExpiration(new Date(System.currentTimeMillis() + validityInSeconds * 1000)) // Expiration personnalisée
                .signWith(SECRET_KEY)
                .compact();
    }

    /**
     * Extrait les claims (payload) d'un token.
     *
     * @param token Le token JWT
     * @return Les claims contenus dans le token
     * @throws JwtException si le token est invalide
     */
    public Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Vérifie si un token est valide.
     *
     * @param token Le token JWT à vérifier
     * @return true si le token est valide, false sinon
     */
    public boolean isTokenValid(String token) {
        try {
            extractClaims(token); // Vérifie et parse le token
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Extrait le sujet (email de l'utilisateur) d'un token.
     *
     * @param token Le token JWT
     * @return L'email contenu dans le token
     */
    public String extractSubject(String token) {
        return extractClaims(token).getSubject();
    }


    /**
     * Vérifie si un token a expiré.
     *
     * @param token Le token JWT
     * @return true si le token a expiré, false sinon
     */
    public boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }

    /**
     * Génère un ID unique pour un RefreshToken.
     *
     * @return Un ID unique sous forme de chaîne
     */
    private String generateUniqueId() {
        return java.util.UUID.randomUUID().toString();
    }
}
