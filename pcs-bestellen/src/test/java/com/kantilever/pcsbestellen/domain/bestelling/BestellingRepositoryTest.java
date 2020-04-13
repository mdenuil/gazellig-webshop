package com.kantilever.pcsbestellen.domain.bestelling;

import com.kantilever.pcsbestellen.domain.bestelling.models.*;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static com.kantilever.pcsbestellen.domain.bestelling.models.BestelStatusType.IN_AFWACHTING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
class BestellingRepositoryTest {

    private Bestelling bestelling1, bestelling2, bestelling3;

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private BestellingRepository bestellingRepository;

    @BeforeEach
    void init() {
        bestelling1 = Bestelling.builder()
                .status(IN_AFWACHTING)
                .klantGegevens(new KlantGegevens("RRW", "Voermans", "ricky54321@live.nl"))
                .afleverAdres(new Adres())
                .factuurAdres(new Adres())
                .artikelen(List.of(new BesteldArtikel(1, 1),
                                   new BesteldArtikel(5, 2)))
                .build();

        bestelling2 = Bestelling.builder()
                .status(IN_AFWACHTING)
                .klantGegevens(new KlantGegevens("MC", "den Uijl", "mcu@live.nl"))
                .afleverAdres(new Adres())
                .factuurAdres(new Adres())
                .artikelen(List.of(new BesteldArtikel(2, 1)))
                .build();

        bestelling3 = Bestelling.builder()
                .status(BestelStatusType.BETAALD)
                .klantGegevens(new KlantGegevens("ST", "Mout", "thomazz@live.nl"))
                .afleverAdres(new Adres())
                .factuurAdres(new Adres())
                .artikelen(List.of(new BesteldArtikel(3, 1)))
                .build();
    }

    @Test
    @DisplayName("Alle bestellingen kunnen worden opgeslagen en uit de repository gehaald worden")
    void retrieveAllSavedBestellingen() {
        testEntityManager.persistAndFlush(bestelling1);
        testEntityManager.persistAndFlush(bestelling2);
        testEntityManager.persistAndFlush(bestelling3);

        List<Bestelling> bestellingen = (List<Bestelling>) bestellingRepository.findAll();

        assertThat(bestellingen.size()).isEqualTo(3);
    }

    @Test
    @DisplayName("Alle bestellingen worden opgehaald op basis van status 'IN_AFWACHTING'")
    void retrieveAllBestellingenWithStatusInAfwachting() {
        testEntityManager.persistAndFlush(bestelling1);
        testEntityManager.persistAndFlush(bestelling2);
        testEntityManager.persistAndFlush(bestelling3);

        List<Bestelling> bestellingen = (List<Bestelling>) bestellingRepository.findAllByStatus(IN_AFWACHTING);

        assertThat(bestellingen.size()).isEqualTo(2);

        assertThat(bestellingen.get(0).getStatus()).isEqualTo(IN_AFWACHTING);
        assertThat(bestellingen.get(1).getStatus()).isEqualTo(IN_AFWACHTING);
    }

    @Test
    @DisplayName("Kan een bestelling zoeken op basis van het bestelnummer")
    void retrieveBestellingByBestelnummer() {
        Long id = (Long) testEntityManager.persistAndGetId(bestelling1);

        Optional<Bestelling> foundBestelling = bestellingRepository.findById(id);

        assertThat(foundBestelling.isPresent()).isTrue();

        assertThat(foundBestelling.get().getBestelNummer()).isEqualTo(id);
        assertThat(foundBestelling.get().getArtikelen().size()).isEqualTo(2);
    }
}