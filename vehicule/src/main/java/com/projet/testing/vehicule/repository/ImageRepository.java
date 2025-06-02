package com.projet.testing.vehicule.repository;

import com.projet.testing.vehicule.model.Images;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Images, Long> {
}