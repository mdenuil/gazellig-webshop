package com.kantilever.bffbestellingen.domain.bestelling.models;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;
import com.kantilever.bffbestellingen.domain.bestelling.dto.BesteldArtikelDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Builder
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

    @Column(name = "ISVERWERKT")
    private boolean isVerwerkt;

    public static BesteldArtikel from(BesteldArtikelDto besteldArtikelDto) {
        return BesteldArtikel.builder()
                .aantal(besteldArtikelDto.getAantal())
                .artikelNummer(besteldArtikelDto.getArtikelNummer())
                .isVerwerkt(besteldArtikelDto.isVerwerkt())
                .build();
    }

    public static List<BesteldArtikel> from(List<BesteldArtikelDto> besteldArtikelDtos) {
        var artikelen = new ArrayList<BesteldArtikel>();
        besteldArtikelDtos.forEach(x -> artikelen.add(BesteldArtikel.from(x)));
        return artikelen;
    }


}
