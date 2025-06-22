package com.projet.testing.vehicule.model;


import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@SequenceGenerator(name = "jwt_seq", sequenceName = "jwt_seq", allocationSize = 1)
public class Jwt {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "jwt_seq")

    private Long id;

    private String valeur;
    private boolean desactive;
    private boolean expire;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private RefreshToken refreshToken;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE})
    @JoinColumn(name = "utilisateur_id")
    private User utilisateur;

}
