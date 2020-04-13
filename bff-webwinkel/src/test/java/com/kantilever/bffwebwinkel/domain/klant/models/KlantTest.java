package com.kantilever.bffwebwinkel.domain.klant.models;

import com.kantilever.bffwebwinkel.domain.klant.amqp.events.KlantEvent;
import com.kantilever.bffwebwinkel.domain.klant.services.KlantSoortService;
import com.kantilever.bffwebwinkel.util.ObjectBuilders;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class KlantTest {
    @Mock
    private KlantSoortService klantSoortService;

    @Test
    @DisplayName("Static factory method creates correct Klant from KlantEvent")
    void fromKlantEventFactory_createsCorrectKlant() {
        // Arrange
        var klantEvent = ObjectBuilders.getDefaultKlantEvent().build();
        when(klantSoortService.getKlantSoortFromEvent(any(KlantEvent.class))).thenReturn(Set.of(
                KlantSoort.from(EKlantSoort.PARTICULIER)
        ));
        // Act
        var klant = Klant.from(klantEvent, klantSoortService);
        // Assert
        assertThat(klant.getEmail()).isEqualTo(klantEvent.getEmail());
        assertThat(klant.getWachtwoord()).isEqualTo(klantEvent.getWachtwoord());
        assertThat(klant.getKlantNummer()).isEqualTo(klantEvent.getKlantNummer());
        assertThat(klant.getInitialen()).isEqualTo(klantEvent.getInitialen());
        assertThat(klant.getAchternaam()).isEqualTo(klantEvent.getAchternaam());
        assertThat(klant.getKlantSoort().size()).isEqualTo(1);
        assertThat(klant.getKrediet()).isEqualTo(klantEvent.getKrediet());
        assertThat(klant.getKlantSoort().contains(KlantSoort.from(EKlantSoort.PARTICULIER))).isTrue();
    }
}