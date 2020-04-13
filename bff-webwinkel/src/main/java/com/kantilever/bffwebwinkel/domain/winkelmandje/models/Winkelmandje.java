package com.kantilever.bffwebwinkel.domain.winkelmandje.models;

import com.kantilever.bffwebwinkel.domain.winkelmandje.amqp.events.WinkelmandjeEvent;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "WINKELMANDJES")
public class Winkelmandje {

    @Id
    private Long klantNummer;

    @OneToMany(cascade = CascadeType.ALL)
    private List<WinkelmandjeArtikel> artikelen;

    public static Winkelmandje from(WinkelmandjeEvent event) {
        List<WinkelmandjeArtikel> artikelen = new ArrayList<>();
        event.getArtikelen().forEach(artikel ->
            WinkelmandjeArtikel.builder()
                    .artikelNummer(artikel.getArtikelNummer())
                    .aantal(artikel.getAantal())
                    .build());

        return Winkelmandje.builder()
                    .klantNummer(event.getKlantNummer())
                    .artikelen(artikelen)
                    .build();
    }
}
