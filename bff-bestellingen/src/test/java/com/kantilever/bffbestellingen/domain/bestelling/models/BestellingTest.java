package com.kantilever.bffbestellingen.domain.bestelling.models;

import java.util.List;
import com.kantilever.bffbestellingen.domain.bestelling.amqp.events.bestelling.BestellingEvent;
import com.kantilever.bffbestellingen.util.ObjectBuilders;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class BestellingTest {
    private BestellingEvent.BestellingEventBuilder bestellingEvent;
    private Bestelling.BestellingBuilder bestelling;

    @BeforeEach
    void init() {
        bestellingEvent = ObjectBuilders.getDefaultBestellingEventBuilder();
        bestelling = ObjectBuilders.getDefaultBestellingBuilder();
    }

    @Test
    @DisplayName("Static constructor method from(bestellingEvent) builds the correct Bestelling")
    void correctBestelling_getsBuildBy_fromBestellingEvent() {
        // Arrange
        var expected = bestelling.build();
        // Act
        var actual = Bestelling.from(bestellingEvent.build());
        var artikelen = actual.getArtikelen();
        // Assert
        assertThat(actual).isEqualTo(expected);
        assertThat(actual.getBehandelbaarSinds()).isNull();
        artikelen.forEach(besteldArtikel -> assertThat(besteldArtikel.isVerwerkt()).isFalse());
    }

    @Test
    @DisplayName("New Bestelling entities have a behandelbaarSinds date of null")
    void correctlySetsDate_toNull_onNewBestelling_fromBestellingEvent() {
        // Act
        var actual = Bestelling.from(bestellingEvent.build());
        // Assert
        assertThat(actual.getBehandelbaarSinds()).isNull();
    }

    @Test
    @DisplayName("When all BesteldArtikel items are marked as done isKlaar returns true")
    void allBesteldArtikelItemsDone_isKlaarReturnsTrue() {
        // Arrange
        var expected = bestelling.artikelen(List.of(
                BesteldArtikel.builder()
                        .aantal(10)
                        .artikelNummer(1)
                        .isVerwerkt(true)
                        .build(),
                BesteldArtikel.builder()
                        .aantal(10)
                        .artikelNummer(2)
                        .isVerwerkt(true)
                        .build()))
                .build();
        // Act
        // Assert
        assertThat(expected.isKlaar()).isTrue();
    }

    @Test
    @DisplayName("When not all BesteldArtikel items are marked as done isKlaar returns false")
    void notAllBesteldArtikelItemsDone_isKlaarReturnsFalse() {
        // Arrange
        var expected = bestelling.artikelen(List.of(
                BesteldArtikel.builder()
                        .aantal(10)
                        .artikelNummer(1)
                        .isVerwerkt(true)
                        .build(),
                BesteldArtikel.builder()
                        .aantal(10)
                        .artikelNummer(2)
                        .isVerwerkt(false)
                        .build()))
                .build();
        // Act
        // Assert
        assertThat(expected.isKlaar()).isFalse();
    }
}