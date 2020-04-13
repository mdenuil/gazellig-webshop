package com.kantilever.bffwebwinkel.domain.winkelmandje.models;

import com.kantilever.bffwebwinkel.domain.winkelmandje.dto.WinkelmandjeArtikelDto;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "WINKELMANDJE_ARTIKELEN")
public class WinkelmandjeArtikel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ARTIKEL_ID")
    private Long id;

    @Column(name = "ARTIKEL_NUMMER")
    private int artikelNummer;

    @Column(name = "ARTIKEL_AANTAL")
    private int aantal;

    @Builder
    public WinkelmandjeArtikel(int artikelNummer, int aantal) {
        this.artikelNummer = artikelNummer;
        this.aantal = aantal;
    }

    public static WinkelmandjeArtikel from(WinkelmandjeArtikelDto winkelmandjeArtikelDto) {
        return WinkelmandjeArtikel.builder()
                .aantal(winkelmandjeArtikelDto.getAantal())
                .artikelNummer(winkelmandjeArtikelDto.getArtikelNummer())
                .build();
    }
}
