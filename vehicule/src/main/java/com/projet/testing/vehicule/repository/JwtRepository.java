package com.projet.testing.vehicule.repository;

import com.projet.testing.vehicule.model.Jwt;
import com.projet.testing.vehicule.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;


/**
 * The interface Jwt repository.
 */
public interface JwtRepository extends JpaRepository<Jwt ,Long> {
    /**
     * Find by refresh token optional.
     *
     * @param refreshToken the refresh token
     * @return the optional
     */
    Optional<Jwt> findByRefreshToken(RefreshToken refreshToken);

    /**
     * Find by utilisateur email list.
     *
     * @param email the email
     * @return the list
     */
    List<Jwt> findByUtilisateurEmail(String email);
}
