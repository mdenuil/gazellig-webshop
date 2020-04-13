package com.kantilever.bffwebwinkel.domain.klant.services;

import com.kantilever.bffwebwinkel.domain.klant.models.EKlantSoort;
import com.kantilever.bffwebwinkel.domain.klant.repositories.KlantSoortRepository;
import com.kantilever.bffwebwinkel.util.ObjectBuilders;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class KlantSoortServiceTest {
    @Mock
    private KlantSoortRepository klantSoortRepository;

    private KlantSoortService klantSoortService;

    @BeforeEach
    void init() {
        klantSoortService = new KlantSoortService(klantSoortRepository);
    }

    @Test
    @DisplayName("KlantSoort set gets correctly build from KlantEvent")
    void name() {
        // Arrange
        var klantEvent = ObjectBuilders.getDefaultKlantEvent().build();
        when(klantSoortRepository.findByNaam(EKlantSoort.PARTICULIER))
                .thenReturn(Optional.of(ObjectBuilders
                        .getDefaultKlantSoort()
                        .build()));
        // Act
        var klantSoortFromEvent = klantSoortService.getKlantSoortFromEvent(klantEvent);
        // Assert
        assertThat(klantSoortFromEvent.size()).isEqualTo(1);
        assertThat(klantSoortFromEvent
                .contains(ObjectBuilders
                        .getDefaultKlantSoort()
                        .build()))
                .isTrue();
    }
}