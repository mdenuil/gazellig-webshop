package com.kantilever.pcswinkelen.winkelmandje.models;

import com.kantilever.pcswinkelen.winkelmandje.amqp.events.KlantEvent;
import com.kantilever.pcswinkelen.winkelmandje.amqp.events.WinkelmandjeEvent;
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
    private List<BesteldArtikel> artikelen;

    public static Winkelmandje from(WinkelmandjeEvent winkelmandjeEvent) {
        return Winkelmandje.builder()
                .klantNummer(winkelmandjeEvent.getKlantNummer())
                .artikelen(winkelmandjeEvent.getArtikelen())
                .build();
    }

    public static Winkelmandje from(KlantEvent klantToegevoegdEvent) {
        return Winkelmandje.builder()
                .klantNummer(klantToegevoegdEvent.getKlantNummer())
                .artikelen(null)
                .build();
    }
}
