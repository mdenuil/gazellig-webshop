package com.kantilever.pcswinkelen.winkelmandje.models;

import com.kantilever.pcswinkelen.winkelmandje.amqp.events.WinkelmandjeEvent;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class WinkelmandjeTest {

    @Test
    @DisplayName("Static factory method from() with WinkelmandjeEvent is built correctly")
    void winkelmandjeSFT_WithWinkelmandjeEvent_BuiltCorrectly() {
        var winkelmandjeEvent = WinkelmandjeEvent.builder()
                .klantNummer(10L)
                .artikelen(List.of(BesteldArtikel.builder().artikelNummer(1).aantal(1).build()))
                .build();

        var winkelmandje = Winkelmandje.from(winkelmandjeEvent);

        assertThat(winkelmandje.getArtikelen()).isEqualTo(winkelmandjeEvent.getArtikelen());
        assertThat(winkelmandje.getKlantNummer()).isEqualTo(winkelmandjeEvent.getKlantNummer());
    }
}