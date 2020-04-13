package com.kantilever.bffwebwinkel.domain.bestelling.models;

import com.kantilever.bffwebwinkel.domain.bestelling.amqp.events.BestellingEvent;
import java.util.List;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Bestelling represents an order made by a Klant.
 *
 * All orders must be tied to a Klant via klantNummer.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "BESTELLINGEN")
public class Bestelling {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BESTELLING_ID")
    private Long bestelNummer;

    @Column(name = "KLANT_ID")
    private Long klantNummer;

    @Column(name = "STATUS")
    private BestelStatusType status;

    @Embedded
    private KlantGegevens klantGegevens;

    @OneToOne(cascade = CascadeType.ALL)
    private Adres afleverAdres;

    @OneToOne(cascade = CascadeType.ALL)
    private Adres factuurAdres;

    @OneToMany(cascade = CascadeType.ALL)
    private List<BesteldArtikel> artikelen;

    public static Bestelling from(BestellingEvent bestellingEvent) {
        var klantGegevens = new KlantGegevens(bestellingEvent.getInitialen(),
                bestellingEvent.getAchternaam(),
                bestellingEvent.getEmail());

        return Bestelling.builder()
                .bestelNummer(bestellingEvent.getBestelNummer())
                .status(bestellingEvent.getStatus())
                .klantGegevens(klantGegevens)
                .klantNummer(bestellingEvent.getKlantNummer())
                .afleverAdres(Adres.from(bestellingEvent.getAfleverAdres()))
                .factuurAdres(Adres.from(bestellingEvent.getFactuurAdres()))
                .artikelen(BesteldArtikel.from(bestellingEvent.getArtikelen()))
                .build();
    }
}
