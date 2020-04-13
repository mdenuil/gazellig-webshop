package com.kantilever.bffwebwinkel.domain.bestelling.amqp.events;

import com.kantilever.bffwebwinkel.util.ObjectBuilders;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class BestellingEventTest {

    @Test
    @DisplayName("The from(BestellingDto, topic) static factory method builds correct BestellingEvent")
    void fromBestelling_BuildsCorrectBestellingEvent() {
        // Arrange
        var bestellingDto = ObjectBuilders.getDefaultBestellingDtoBuilder().build();
        // Act
        var actual = BestellingEvent.from(bestellingDto, "topic");
        // Assert
        assertThat(actual.getTopic()).isEqualTo("topic");
        assertThat(actual.getInitialen()).isEqualTo(bestellingDto.getInitialen());
        assertThat(actual.getAchternaam()).isEqualTo(bestellingDto.getAchternaam());
        assertThat(actual.getEmail()).isEqualTo(bestellingDto.getEmail());
        assertThat(actual.getAfleverAdres()).isEqualTo(bestellingDto.getAfleverAdres());
        assertThat(actual.getFactuurAdres()).isEqualTo(bestellingDto.getFactuurAdres());
        assertThat(actual.getStatus()).isNull();
        assertThat(actual.getKlantNummer()).isNull();
        assertThat(actual.getBestelNummer()).isNull();
    }
}