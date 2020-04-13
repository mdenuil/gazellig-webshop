package com.kantilever.bffwebwinkel.domain.bestelling.services;

import com.kantilever.bffwebwinkel.domain.bestelling.models.Bestelling;
import com.kantilever.bffwebwinkel.domain.bestelling.repositories.BestellingRepository;
import com.kantilever.bffwebwinkel.domain.klant.services.KlantService;
import com.kantilever.bffwebwinkel.util.ObjectBuilders;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BestellingServiceTest {
    @Mock
    private BestellingRepository bestellingRepository;

    private BestellingService bestellingService;

    @BeforeEach
    void init() {
        this.bestellingService = new BestellingService(bestellingRepository);
    }

    @Test
    @DisplayName("Save Bestelling correclty converts event to entity and calls save")
    void saveBestelling_persistsBestelling_fromEvent() {
        // Arrange
        var klantNummer = 1L;
        var bestellingEvent = ObjectBuilders.getDefaultBestellingEventBuilder().build();
        var expected = ObjectBuilders.getDefaultBestellingBuilder().bestelNummer(klantNummer).build();
        when(bestellingRepository.save(any(Bestelling.class)))
                .thenAnswer(invocationOnMock -> {
                    var bestelling = (Bestelling) invocationOnMock.getArgument(0);
                    bestelling.setBestelNummer(1L);
                    return bestelling;
                });
        // Act
        var actual = bestellingService.saveBestelling(bestellingEvent);
        // Assert
        assertThat(actual).isEqualTo(expected);
        verify(bestellingRepository, times(1)).save(expected);
    }

    @Test
    @DisplayName("")
    void findAllBestellingForUser_callsBestellingRepository_forKlantId() {
        var klantNummer = 1L;
        // Act
        var actual = bestellingService.findAllBestellingenForKlant(klantNummer);
        // Assert
        verify(bestellingRepository, times(1)).findAllByKlantNummer(klantNummer);

    }
}