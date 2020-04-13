package com.kantilever.bffwebwinkel.domain.bestelling.models;

import com.kantilever.bffwebwinkel.domain.bestelling.dto.AdresDto;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Adres represents either a FactuurAdres or AfleverAdres of a Klant.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Builder
public class Adres {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ARTIKEL_ID")
    private Long id;

    @Column(name = "STRAATNAAM")
    private String straatnaam;

    @Column(name = "HUISNUMMER")
    private String huisnummer;

    @Column(name = "POSTCODE")
    private String postcode;

    @Column(name = "WOONPLAATS")
    private String woonplaats;

    public static Adres from(AdresDto adresDto) {
        return Adres.builder()
                .straatnaam(adresDto.getStraatnaam())
                .huisnummer(adresDto.getHuisnummer())
                .postcode(adresDto.getPostcode())
                .woonplaats(adresDto.getWoonplaats())
                .build();
    }
}
