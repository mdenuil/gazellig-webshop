package com.kantilever.bffbestellingen.domain.artikel.models;

import com.kantilever.bffbestellingen.domain.artikel.amqp.events.artikel.ArtikelEvent;
import com.kantilever.bffbestellingen.util.ObjectBuilders;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

class ArtikelTest {
    private ArtikelEvent artikelEvent;

    @BeforeEach
    void init() {
        this.artikelEvent = ObjectBuilders.getDefaultArtikelEvent().build();
    }

    @Test
    @DisplayName("from(ArtikelEvent) builds correct Artikel")
    void fromArtikelEvent_BuildsCorrectArtikel() {
        // Arrange
        var artikel = Artikel.from(artikelEvent);
        // Act
        // Assert
        assertThat(artikel.getArtikelNummer()).isEqualTo(artikelEvent.getArtikelNummer());
        assertThat(artikel.getAfbeeldingUrl()).isEqualTo(artikelEvent.getAfbeeldingUrl());
        assertThat(artikel.getBeschrijving()).isEqualTo(artikelEvent.getBeschrijving());
        assertThat(artikel.getLeverancier()).isEqualTo(artikelEvent.getLeverancier());
        assertThat(artikel.getLeverancierCode()).isEqualTo(artikelEvent.getLeverancierCode());
        assertThat(artikel.getCategorieen()).isEqualTo(artikelEvent.getCategorieen());
    }

    @Test
    @DisplayName("No VAT values from Artikel events get converted to VAT values for displaying")
    void fromEventConstructor_convertsNoVatValues_toVatValues() {
        // Arrange
        // Act
        var artikel = Artikel.from(artikelEvent);
        var expected = BigDecimal.valueOf(100L).multiply(Artikel.BTW_PERCENTAGE);
        // Assert
        assertThat(artikel.getPrijs()).isEqualTo(expected);
    }

    @Test
    @DisplayName("Newly created Artikel entities get an Aantal value of zero")
    void fromEventConstructor_setsAantal_toZero() {
        // Arrange
        artikelEvent.setLeverbaarTot(null);
        // Act
        var artikel = Artikel.from(artikelEvent);
        // Assert
        assertThat(artikel.getAantal()).isEqualTo(0);
    }

    @Test
    @DisplayName("Date values for leverbaarTot get handled correctly")
    void leverbaarTot_withEventDateValue_getTransformedCorrectly() {
        // Arrange
        // Act
        var artikel = Artikel.from(artikelEvent);
        // Assert
        assertThat(artikel.getLeverbaarTot()).isEqualTo(LocalDateTime.now(ObjectBuilders.clock));
    }

    @Test
    @DisplayName("Null values for leverbaarTot get handled gracefully")
    void leverbaarTot_withEventValueNull_getHandledGracefully() {
        // Arrange
        artikelEvent.setLeverbaarTot(null);
        // Act
        var artikel = Artikel.from(artikelEvent);
        // Assert
        assertThat(artikel.getLeverbaarTot()).isNull();
    }


    @Test
    @DisplayName("verlaagAantal decreases Aantal it by the correct amount")
    void verlaagAantal_decreasesAantal_byCorrectAmount() {
        // Arrange
        var artikel = Artikel.from(artikelEvent);
        // Act
        artikel.verlaagAantal(10);
        // Assert
        assertThat(artikel.getAantal()).isEqualTo(-10);
    }

    @Test
    @DisplayName("verhoogAantal increases Aantal it by the correct amount")
    void verhoogAantal_increasesAantal_byCorrectAmount() {
        // Arrange
        var artikel = Artikel.from(artikelEvent);
        // Act
        artikel.verhoogAantal(10);
        // Assert
        assertThat(artikel.getAantal()).isEqualTo(10);
    }
}