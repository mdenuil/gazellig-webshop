package com.kantilever.dsklantbeheer.domain.klant.amqp.receivers;

import com.kantilever.dsklantbeheer.domain.klant.amqp.events.KlantEvent;
import com.kantilever.dsklantbeheer.domain.klant.amqp.sender.KlantEventSenderService;
import com.kantilever.dsklantbeheer.domain.klant.models.EKlantSoort;
import com.kantilever.dsklantbeheer.domain.klant.models.Klant;
import com.kantilever.dsklantbeheer.domain.klant.models.KlantSoort;
import com.kantilever.dsklantbeheer.domain.klant.services.KlantService;
import com.kantilever.dsklantbeheer.domain.klant.services.KlantSoortService;
import com.kantilever.dsklantbeheer.util.ObjectBuilders;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KlantEventReceiverServiceTest {
    @Mock
    private KlantService klantService;
    @Mock
    private KlantSoortService klantSoortService;
    @Mock
    private KlantEventSenderService klantEventSenderService;

    private KlantEventReceiverService klantEventReceiverService;

    @BeforeEach
    void init() {
        klantEventReceiverService = new KlantEventReceiverService(
                klantService,
                klantSoortService,
                klantEventSenderService
        );
    }

    @Test
    @DisplayName("handleKlantGeregistreerdEvent persist calls the right services and persists the Klant")
    void handleKlantGeregistreerdEvent_callsCorrectServices_andPersistsKlant() {
        // Arrange
        var klantEvent = ObjectBuilders.getDefaultKlantEvent().build();
        var klant = ObjectBuilders.getDefaultKlant().klantNummer(1L).build();
        when(klantSoortService.getKlantSoortFromEvent(any(KlantEvent.class))).thenReturn(Set.of(
                KlantSoort.from(EKlantSoort.PARTICULIER)
        ));
        when(klantService.addNewKlant(any(Klant.class))).thenReturn(klant);
        // Act
        klantEventReceiverService.handleKlantGeregistreerdEvent(klantEvent);
        // Assert
        verify(klantService, times(1)).addNewKlant(klant);
        verify(klantEventSenderService, times(1)).sendKlantToegevoegdEvent(klant);
    }
}