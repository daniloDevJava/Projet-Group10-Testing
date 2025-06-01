package com.projet.testing.vehicule.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Images {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String chemin;
    private String nom;


    @ManyToOne
    @JoinColumn(name = "vehicule_id",nullable = false)
    private Vehicule vehicule;

    public void makeName(){
        this.nom="IMG"+ 0 +id;
    }


}
