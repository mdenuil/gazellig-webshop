package com.kantilever.pcsbestellen.domain.bestelling.models;

import com.kantilever.pcsbestellen.domain.bestelling.amqp.events.BestellingEvent;
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
@Table(name = "BESTELLINGEN")
public class Bestelling {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BESTELLING_ID", nullable = false, unique = true)
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

        var afleverAdres = Adres.builder()
                .straatnaam(bestellingEvent.getAfleverAdres().getStraatnaam())
                .huisnummer(bestellingEvent.getAfleverAdres().getHuisnummer())
                .postcode(bestellingEvent.getAfleverAdres().getPostcode())
                .woonplaats(bestellingEvent.getAfleverAdres().getWoonplaats())
                .build();

        var factuurAdres = Adres.builder()
                .straatnaam(bestellingEvent.getFactuurAdres().getStraatnaam())
                .huisnummer(bestellingEvent.getFactuurAdres().getHuisnummer())
                .postcode(bestellingEvent.getFactuurAdres().getPostcode())
                .woonplaats(bestellingEvent.getFactuurAdres().getWoonplaats())
                .build();


        List<BesteldArtikel> artikelen = new ArrayList<>();

        bestellingEvent.getArtikelen()
                .forEach(x -> artikelen.add(BesteldArtikel.fromBesteldArtikelDto(x)));

        return Bestelling.builder()
                .bestelNummer(bestellingEvent.getBestelNummer())
                .klantNummer(bestellingEvent.getKlantNummer())
                .status(bestellingEvent.getStatus())
                .klantGegevens(klantGegevens)
                .afleverAdres(afleverAdres)
                .factuurAdres(factuurAdres)
                .artikelen(artikelen)
                .build();
    }
}
