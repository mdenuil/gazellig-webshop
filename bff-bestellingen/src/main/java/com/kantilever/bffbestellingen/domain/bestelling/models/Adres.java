package com.kantilever.bffbestellingen.domain.bestelling.models;

import com.kantilever.bffbestellingen.domain.bestelling.dto.AdresDto;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
