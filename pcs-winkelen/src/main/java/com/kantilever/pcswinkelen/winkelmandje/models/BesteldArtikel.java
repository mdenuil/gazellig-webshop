package com.kantilever.pcswinkelen.winkelmandje.models;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "BESTELDE_ARTIKELEN")
public class BesteldArtikel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ARTIKEL_ID")
    private Long id;

    @Column(name = "ARTIKEL_NUMMER")
    private int artikelNummer;

    @Column(name = "ARTIKEL_AANTAL")
    private int aantal;

    @Builder
    public BesteldArtikel(int artikelNummer, int aantal) {
        this.artikelNummer = artikelNummer;
        this.aantal = aantal;
    }
}
