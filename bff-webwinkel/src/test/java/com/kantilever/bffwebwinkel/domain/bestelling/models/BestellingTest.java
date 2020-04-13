package com.kantilever.bffwebwinkel.domain.bestelling.models;

import com.kantilever.bffwebwinkel.domain.bestelling.amqp.events.BestellingEvent;
import com.kantilever.bffwebwinkel.util.ObjectBuilders;
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
        // Assert
        assertThat(actual).isEqualTo(expected);
    }
}
