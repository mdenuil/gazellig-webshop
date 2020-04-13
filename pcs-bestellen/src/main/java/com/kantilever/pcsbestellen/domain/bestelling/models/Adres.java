package com.kantilever.pcsbestellen.domain.bestelling.models;

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
    @Column(name = "ADRES_ID")
    private Long id;

    @Column(name = "STRAATNAAM")
    private String straatnaam;

    @Column(name = "HUISNUMMER")
    private String huisnummer;

    @Column(name = "POSTCODE")
    private String postcode;

    @Column(name = "WOONPLAATS")
    private String woonplaats;
}
