package com.kantilever.pcsbestellen.domain.bestelling.models;

import com.kantilever.pcsbestellen.domain.bestelling.dto.BesteldArtikelDto;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "ARTIKELEN")
public class BesteldArtikel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ARTIKEL_ID")
    private Long id;

    @Column(name = "AANTAL")
    private int aantal;

    @Column(name = "ARTIKELNUMMER")
    private int artikelNummer;

    public BesteldArtikel(int artikelNummer, int aantal) {
        this.aantal = aantal;
        this.artikelNummer = artikelNummer;
    }

    public static BesteldArtikel fromBesteldArtikelDto(BesteldArtikelDto besteldArtikelDto) {
        var artikelNummer = besteldArtikelDto.getArtikelNummer();
        var aantal = besteldArtikelDto.getAantal();

        return new BesteldArtikel(artikelNummer, aantal);
    }
}
