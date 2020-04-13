package com.kantilever.bffwebwinkel.domain.klant.amqp.events;

import com.kantilever.bffwebwinkel.util.ObjectBuilders;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class KlantEventTest {

    @Test
    @DisplayName("The from(Klant) static factory method builds the correct KlantEvent")
    void fromKlant_buildsCorrectKlantEvent() {
        // Arrange
        var klant = ObjectBuilders.getDefaultKlant().build();
        // Act
        var actual = KlantEvent.from(klant, "topic");
        // Assert
        assertThat(actual.getTopic()).isEqualTo("topic");
        assertThat(actual.getKlantNummer()).isEqualTo(klant.getKlantNummer());
        assertThat(actual.getEmail()).isEqualTo(klant.getEmail());
        assertThat(actual.getWachtwoord()).isEqualTo(klant.getWachtwoord());
        assertThat(actual.getInitialen()).isEqualTo(klant.getInitialen());
        assertThat(actual.getAchternaam()).isEqualTo(klant.getAchternaam());
    }
}