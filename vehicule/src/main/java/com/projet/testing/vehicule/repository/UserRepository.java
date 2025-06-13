package com.projet.testing.vehicule.repository;

import com.projet.testing.vehicule.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * The interface User repository.
 */
public interface UserRepository extends JpaRepository <User , UUID > {
    /**
     * Find by id and delete at is null optional.
     *
     * @param id the id
     * @return the optional
     */
    Optional<User> findByIdAndDeleteAtIsNull(UUID id);


    /**
     * Find by email and delete at is null optional.
     *
     * @param email the email
     * @return the optional
     */
    Optional<User> findByEmailAndDeleteAtIsNull(String email);

    List<User> findByDeleteAtIsNull();
}
