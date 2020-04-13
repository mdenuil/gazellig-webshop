package com.kantilever.dsklantbeheer.domain.klant.models;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * KlantSoort is the database representation of a {@link EKlantSoort}
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "KLANTSOORT")
public class KlantSoort {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "KLANTSOORT_ID")
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(name = "NAAM")
    private EKlantSoort naam;

    public static KlantSoort from(EKlantSoort eKlantSoort) {
        return KlantSoort.builder()
                .naam(eKlantSoort)
                .build();
    }
}
