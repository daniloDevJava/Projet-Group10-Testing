
package com.projet.testing.vehicule.model;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.time.LocalDateTime;


@Setter
@Getter
@Builder
@SequenceGenerator(name = "refresh_token_seq", sequenceName = "refresh_token_seq", allocationSize = 1)
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "refresh_token_seq")
    private Long id;
    private boolean expire;
    private String valeur;
    private Instant creation;
    private Instant expiration;
    @CreationTimestamp
    @Column(nullable = false,updatable = false)
    private LocalDateTime createAt;
    @UpdateTimestamp
    private LocalDateTime updateAt;


}
