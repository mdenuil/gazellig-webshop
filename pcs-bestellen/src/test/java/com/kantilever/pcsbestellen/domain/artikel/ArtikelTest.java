package com.kantilever.pcsbestellen.domain.artikel;

import com.kantilever.pcsbestellen.domain.artikel.amqp.events.ArtikelEvent;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ArtikelTest {
    private ArtikelEvent artikelEvent;

    @BeforeEach
    void init() {
        artikelEvent = ArtikelEvent.builder()
                .topic("artike.topic")
                .afbeeldingUrl("AfbeeldingUrl")
                .artikelNummer(1)
                .beschrijving("Beschrijving")
                .categorieen(List.of("cat1", "cat2"))
                .leverancier("Leverancier")
                .leverancierCode("Lev")
                .leverbaarTot("2017-12-08T02:15:42.529")
                .leverbaarVanaf("2017-12-08T02:15:42.529")
                .naam("Naam")
                .prijs(100L)
                .build();
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
    @DisplayName("Date values for leverbaarTot get handled gracefully")
    void leverbaarTot_withEventDateValue_getHandledGracefully() {
        // Arrange
        // Act
        var artikel = Artikel.from(artikelEvent);
        // Assert
        assertThat(artikel.getLeverbaarTot().getYear()).isEqualTo(2017);
        assertThat(artikel.getLeverbaarTot().getMonthValue()).isEqualTo(12);
        assertThat(artikel.getLeverbaarTot().getDayOfMonth()).isEqualTo(8);
        assertThat(artikel.getLeverbaarTot().getHour()).isEqualTo(2);
        assertThat(artikel.getLeverbaarTot().getMinute()).isEqualTo(15);
        assertThat(artikel.getLeverbaarTot().getSecond()).isEqualTo(42);
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
    @DisplayName("decreaseAantal decreases Aantal it by the correct amount")
    void decreaseAantal_decreasesAantal_byCorrectAmount() {
        // Arrange
        var artikel = Artikel.from(artikelEvent);
        // Act
        artikel.decreaseAantal(10);
        // Assert
        assertThat(artikel.getAantal()).isEqualTo(-10);
    }

    @Test
    @DisplayName("increaseAantal increases Aantal it by the correct amount")
    void increaseAantal_increasesAantal_byCorrectAmount() {
        // Arrange
        var artikel = Artikel.from(artikelEvent);
        // Act
        artikel.increaseAantal(10);
        // Assert
        assertThat(artikel.getAantal()).isEqualTo(10);
    }
}