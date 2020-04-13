package com.kantilever.pcsbestellen.domain.bestelling.amqp.events;

import com.kantilever.pcsbestellen.domain.bestelling.dto.AdresDto;
import com.kantilever.pcsbestellen.domain.bestelling.dto.BesteldArtikelDto;
import com.kantilever.pcsbestellen.domain.bestelling.models.Adres;
import com.kantilever.pcsbestellen.domain.bestelling.models.BesteldArtikel;
import com.kantilever.pcsbestellen.domain.bestelling.models.Bestelling;
import com.kantilever.pcsbestellen.domain.bestelling.models.KlantGegevens;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BestellingEventTest {

    private BestellingEvent bestellingEvent;
    private Bestelling bestelling;

    @BeforeEach
    void init() {
        bestellingEvent = BestellingEvent.builder()
                .topic("topic")
                .bestelNummer(5L)
                .initialen("RRW")
                .achternaam("Voermans")
                .email("ricky54321@live.nl")
                .afleverAdres(new AdresDto("Straat1", "1", "5555AA", "Ede"))
                .factuurAdres(new AdresDto("Straat2", "2", "5555BB", "Epe"))
                .artikelen(List.of(new BesteldArtikelDto(1, 1),
                                   new BesteldArtikelDto(12, 9)))
                .build();

        bestelling = Bestelling.builder()
                .bestelNummer(1L)
                .klantNummer(1L)
                .klantGegevens(new KlantGegevens("R", "R", "ricky54321@live.nl"))
                .afleverAdres(Adres.builder()
                        .woonplaats("Tilburg")
                        .huisnummer("24D")
                        .straatnaam("De zwaan")
                        .postcode("5050EE")
                        .build())
                .factuurAdres(Adres.builder()
                        .woonplaats("Tilburg")
                        .huisnummer("24D")
                        .straatnaam("De zwaan")
                        .postcode("5050EE")
                        .build())
                .artikelen(List.of(
                        BesteldArtikel.fromBesteldArtikelDto(new BesteldArtikelDto(1, 1)),
                        BesteldArtikel.fromBesteldArtikelDto(new BesteldArtikelDto(12, 9))))
                .build();
    }

    @Test
    @DisplayName("Kan bestelling van static factory method aanmaken die een BestellingEvent binnenkrijt")
    void canCreateBestellingFromBestellingEvent() {
        var bestelling = Bestelling.from(bestellingEvent);

        assertThat(bestelling).isNotNull();
        assertThat(bestelling.getArtikelen().size()).isEqualTo(2);
        assertThat(bestelling.getKlantGegevens().getEmail()).isEqualTo(bestellingEvent.getEmail());
    }

    @Test
    @DisplayName("AfleverAdres en FactuurAdres zijn niet hetzelfde")
    void afleverAdresAndFactuurAdresAreNotTheSame() {
        var bestelling = Bestelling.from(bestellingEvent);

        assertThat(bestelling.getAfleverAdres()).isNotNull();
        assertThat(bestelling.getFactuurAdres()).isNotNull();
        assertThat(bestelling.getAfleverAdres()).isNotEqualTo(bestelling.getFactuurAdres());
    }

    @Test
    @DisplayName("AfleverAdres en FactuurAdres zijn hetzelfde")
    void afleverAdresAndFactuurAdresAreTheSame() {
        bestellingEvent.setAfleverAdres(bestellingEvent.getFactuurAdres());
        var bestelling = Bestelling.from(bestellingEvent);

        assertThat(bestelling.getAfleverAdres()).isNotNull();
        assertThat(bestelling.getFactuurAdres()).isNotNull();
        assertThat(bestelling.getAfleverAdres()).isEqualTo(bestelling.getFactuurAdres());
    }

    @Test
    @DisplayName("Can create BestellingEvent from existing bestelling with static factory method")
    void canCreateBestellingFromStaticFactory() {
        var bestellingEvent = BestellingEvent.from(bestelling, "RandomTopic");

        assertThat(bestelling.getAfleverAdres().getHuisnummer())
                .isEqualTo(bestellingEvent.getAfleverAdres().getHuisnummer());
        assertThat(bestelling.getAfleverAdres().getPostcode())
                .isEqualTo(bestellingEvent.getAfleverAdres().getPostcode());
        assertThat(bestelling.getAfleverAdres().getStraatnaam())
                .isEqualTo(bestellingEvent.getAfleverAdres().getStraatnaam());
        assertThat(bestelling.getAfleverAdres().getWoonplaats())
                .isEqualTo(bestellingEvent.getAfleverAdres().getWoonplaats());

        assertThat(bestelling.getKlantGegevens().getEmail())
                .isEqualTo(bestellingEvent.getEmail());
        assertThat(bestelling.getKlantGegevens().getAchternaam())
                .isEqualTo(bestellingEvent.getAchternaam());
        assertThat(bestelling.getKlantGegevens().getInitialen())
                .isEqualTo(bestellingEvent.getInitialen());
    }
}