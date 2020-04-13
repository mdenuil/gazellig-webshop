package com.kantilever.bffbestellingen.domain.bestelling.models;

import com.kantilever.bffbestellingen.domain.bestelling.amqp.events.bestelling.BestellingEvent;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.*;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EqualsAndHashCode
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

    @Column(name = "BEHANDELBAAR_SINDS")
    private LocalDateTime behandelbaarSinds;

    @Embedded
    private KlantGegevens klantGegevens;

    @OneToOne(cascade = CascadeType.ALL)
    private Adres afleverAdres;

    @OneToOne(cascade = CascadeType.ALL)
    private Adres factuurAdres;

    @OneToMany(cascade = CascadeType.ALL)
    private List<BesteldArtikel> artikelen;

    /**
     * Returns if all Artikel items in Bestelling have status isVerwerkt true
     *
     * @return true if bestelling is done.
     */
    public boolean isKlaar() {
        return artikelen.stream().allMatch(BesteldArtikel::isVerwerkt);
    }

    /**
     * Set the behandelbaarSinds time to now and update Status to IN_BEHANDELING
     */
    public void startVerwerken() {
        behandelbaarSinds = LocalDateTime.now();
        status = BestelStatusType.IN_BEHANDELING;
    }

    /**
     * Set the isVerwerkt status to true on an Artikel.
     *
     * @param artikelNummer of Artikel to set the status on.
     */
    public void verwerkArtikel(int artikelNummer) {
        artikelen.stream()
                .filter(besteldArtikel -> besteldArtikel.getArtikelNummer() == artikelNummer)
                .findFirst().ifPresent(besteldArtikel -> besteldArtikel.setVerwerkt(true));
    }

    public static Bestelling from(BestellingEvent bestellingEvent) {
        var klantGegevens = new KlantGegevens(bestellingEvent.getInitialen(),
                bestellingEvent.getAchternaam(),
                bestellingEvent.getEmail());

        return Bestelling.builder()
                .behandelbaarSinds(null)
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
