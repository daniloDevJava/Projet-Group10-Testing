package com.projet.testing.vehicule.repository;

import com.projet.testing.vehicule.model.Jwt;
import com.projet.testing.vehicule.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;


public interface JwtRepository extends JpaRepository<Jwt ,Long> {
    Optional<Jwt> findByRefreshToken(RefreshToken refreshToken);
    List<Jwt> findByUtilisateurEmail(String email);
}
