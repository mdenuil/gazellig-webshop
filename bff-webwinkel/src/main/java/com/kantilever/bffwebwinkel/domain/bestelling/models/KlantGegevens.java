package com.kantilever.bffwebwinkel.domain.bestelling.models;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * KlantGegevens represents the personal data of a Klant.
 *
 * This class is embedded into the persisted datastructure.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Embeddable
public class KlantGegevens {
    @Column(name = "INITIALEN")
    private String initialen;

    @Column(name = "ACHTERNAAM")
    private String achternaam;

    @Column(name = "EMAIL")
    private String email;
}
