package com.projet.testing.vehicule.repository;

import com.projet.testing.vehicule.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * The interface Refresh token repository.
 */
public interface RefreshTokenRepository extends JpaRepository<RefreshToken ,Long> {
    /**
     * Find by valeur and expire false optional.
     *
     * @param valeur the valeur
     * @return the optional
     */
    Optional<RefreshToken> findByValeurAndExpireFalse(String valeur);

    /**
     * Find by valeur optional.
     *
     * @param valeur the valeur
     * @return the optional
     */
    Optional<RefreshToken> findByValeur(String valeur);
}
