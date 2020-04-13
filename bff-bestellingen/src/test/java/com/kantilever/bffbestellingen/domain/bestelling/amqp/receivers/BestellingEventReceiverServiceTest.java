package com.kantilever.bffbestellingen.domain.bestelling.amqp.receivers;

import com.kantilever.bffbestellingen.domain.bestelling.amqp.events.bestelling.BestellingEvent;
import com.kantilever.bffbestellingen.domain.bestelling.amqp.events.bestellingstatus.BestellingStatusEvent;
import com.kantilever.bffbestellingen.domain.bestelling.models.BestelStatusType;
import com.kantilever.bffbestellingen.domain.bestelling.models.Bestelling;
import com.kantilever.bffbestellingen.domain.bestelling.services.BestellingService;
import com.kantilever.bffbestellingen.util.ObjectBuilders;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BestellingEventReceiverServiceTest {
    @Mock
    private BestellingService bestellingService;

    private BestellingEventReceiverService bestellingEventReceiverService;
    private BestellingStatusEvent.BestellingStatusEventBuilder bestellingStatusEventBuilder;
    private BestellingEvent.BestellingEventBuilder bestellingEventBuilder;
    private Bestelling.BestellingBuilder bestellingBuilder;

    @BeforeEach
    void init() {
        bestellingEventReceiverService = new BestellingEventReceiverService(bestellingService);
        bestellingStatusEventBuilder = ObjectBuilders.getDefaultBestellingStatusBuilder();
        bestellingEventBuilder = ObjectBuilders.getDefaultBestellingEventBuilder();
        bestellingBuilder = ObjectBuilders.getDefaultBestellingBuilder();
    }

    @Test
    @DisplayName("HandleBestellingToegevoegdEvent calls the correct service method with right parameters")
    void handleBestellingToegevoegdEvent_callsCorrectService() {
        // Arrange
        bestellingEventReceiverService.handleBestellingToegevoegdEvent(bestellingEventBuilder.build());
        // Act
        // Assert
        verify(bestellingService, times(1)).addNewBestelling(bestellingBuilder.build());
    }

    @Test
    @DisplayName("HandleBestellingStatusInAfwachtingGezetEvent calls the correct service method with right parameters")
    void handleBestellingStatusInAfwachtingGezetEvent_callsCorrectService() {
        // Arrange
        var bestellingStatusEvent = bestellingStatusEventBuilder.build();
        bestellingEventReceiverService.handleBestellingStatusInAfwachtingGezetEvent(bestellingStatusEvent);
        // Act
        // Assert
        verify(bestellingService, times(1)).setBestellingStatus(bestellingStatusEvent.getBestelNummer(), BestelStatusType.IN_AFWACHTING);
    }

    @Test
    @DisplayName("HandleBestellingStatusBehandelbaarGezetEvent calls the correct service method with right parameters")
    void handleBestellingStatusBehandelbaarGezetEvent_callsCorrectService() {
        var bestellingStatusEvent = bestellingStatusEventBuilder.build();
        bestellingEventReceiverService.handleBestellingStatusBehandelbaarGezetEvent(bestellingStatusEvent);
        // Act
        // Assert
        verify(bestellingService, times(1)).setBestellingStatus(bestellingStatusEvent.getBestelNummer(), BestelStatusType.BEHANDELBAAR);
    }

    @Test
    @DisplayName("HandleBestellingStatusInBehandelingGezetEvent calls the correct service method with right parameters")
    void handleBestellingStatusInBehandelingGezetEvent_callsCorrectService() {
        var bestellingStatusEvent = bestellingStatusEventBuilder.build();
        bestellingEventReceiverService.handleBestellingStatusInBehandelingGezetEvent(bestellingStatusEvent);
        // Act
        // Assert
        verify(bestellingService, times(1)).setBestellingStatus(bestellingStatusEvent.getBestelNummer(), BestelStatusType.IN_BEHANDELING);
    }

    @Test
    @DisplayName("HandleBestellingStatusVerstuurdGezetEvent calls the correct service method with right parameters")
    void handleBestellingStatusVerstuurdGezetEvent_callsCorrectService() {
        var bestellingStatusEvent = bestellingStatusEventBuilder.build();
        bestellingEventReceiverService.handleBestellingStatusVerstuurdGezetEvent(bestellingStatusEvent);
        // Act
        // Assert
        verify(bestellingService, times(1)).setBestellingStatus(bestellingStatusEvent.getBestelNummer(), BestelStatusType.VERSTUURD);
    }

    @Test
    @DisplayName("HandleBestellingStatusBetaaldGezetEvent calls the correct service method with right parameters")
    void handleBestellingStatusBetaaldGezetEvent_callsCorrectService() {
        var bestellingStatusEvent = bestellingStatusEventBuilder.build();
        bestellingEventReceiverService.handleBestellingStatusBetaaldGezetEvent(bestellingStatusEvent);
        // Act
        // Assert
        verify(bestellingService, times(1)).setBestellingStatus(bestellingStatusEvent.getBestelNummer(), BestelStatusType.BETAALD);
    }
}