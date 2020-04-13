package com.kantilever.pcswinkelen.winkelmandje;

import com.gazellig.amqpservice.amqp.audit.replay.AuditReplayStartListener;
import com.kantilever.pcswinkelen.winkelmandje.amqp.events.KlantEvent;
import com.kantilever.pcswinkelen.winkelmandje.amqp.events.WinkelmandjeEvent;
import com.kantilever.pcswinkelen.winkelmandje.amqp.jackson.WinkelmandjeSubtypesConfig;
import com.kantilever.pcswinkelen.winkelmandje.amqp.queues.KlantToegevoegdConfig;
import com.kantilever.pcswinkelen.winkelmandje.amqp.receivers.KlantToegevoegdEventReceiver;
import com.kantilever.pcswinkelen.winkelmandje.models.BesteldArtikel;
import com.kantilever.pcswinkelen.winkelmandje.models.Winkelmandje;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class WinkelmandjeServiceTest {

    private KlantEvent klantToegevoegdEvent;
    private WinkelmandjeEvent winkelmandjeEvent;

    @MockBean
    private KlantToegevoegdConfig klantToegevoegdConfig;

    @MockBean
    private WinkelmandjeSubtypesConfig klantSubtypesConfig;

    @MockBean
    private AuditReplayStartListener auditReplayStartListener;

    @MockBean
    private KlantToegevoegdEventReceiver klantToegevoegdEventReceiver;

    @MockBean
    private WinkelmandjeEventService winkelmandjeEventService;

    @MockBean
    private WinkelmandjeRepository winkelmandjeRepository;

    @Autowired
    private WinkelmandjeService winkelmandjeService;

    @BeforeEach
    void init() {
        klantToegevoegdEvent = KlantEvent.builder()
                .klantNummer(12L)
                .build();

        winkelmandjeEvent = WinkelmandjeEvent.builder()
                .klantNummer(15L)
                .artikelen(List.of(
                        new BesteldArtikel(1, 1),
                        new BesteldArtikel(121, 3)
                ))
                .build();
    }

    @Test
    @DisplayName("A new Winkelmandje can be saved in the database via WinkelmandjeService")
    void canSaveNewWinkelmandje_ViaWinkelmandjeService() {
        when(winkelmandjeService.saveNewWinkelmandje(klantToegevoegdEvent))
                .thenReturn(Optional.of(Winkelmandje.from(klantToegevoegdEvent)).get());

        var winkelmandje = winkelmandjeService.saveNewWinkelmandje(klantToegevoegdEvent);

        var expectedWinkelmandje = Winkelmandje.from(klantToegevoegdEvent);
        expectedWinkelmandje.setKlantNummer(12L);

        assertThat(winkelmandje.getKlantNummer()).isEqualTo(expectedWinkelmandje.getKlantNummer());
        assertThatThrownBy(() ->
                winkelmandje.getArtikelen().size()
        )
        .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("An existing Winkelmandje can be updated in the database via WinkelmandjeService")
    void canUpdateExistingWinkelmandje_ViaWinkelmandjeService_AndSendEvent() {
        when(winkelmandjeRepository.findById(winkelmandjeEvent.getKlantNummer()))
                .thenReturn(Optional.of(Winkelmandje.from(winkelmandjeEvent)));

        when(winkelmandjeRepository.save(Winkelmandje.from(winkelmandjeEvent)))
                .thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));

        winkelmandjeService.updateExistingWinkelmandje(winkelmandjeEvent);

        var inOrder = inOrder(winkelmandjeRepository, winkelmandjeEventService);

        inOrder.verify(winkelmandjeRepository, times(1))
                .findById(winkelmandjeEvent.getKlantNummer());
        inOrder.verify(winkelmandjeRepository, times(1))
                .save(Winkelmandje.from(winkelmandjeEvent));
        inOrder.verify(winkelmandjeEventService, times(1))
                .sendWinkelmandjeToegevoegdEvent(winkelmandjeEvent);
    }

    @Test
    @DisplayName("An existing Winkelmandje can be updated in the database via WinkelmandjeService and skip sending event")
    void canUpdateExistingWinkelmandje_ViaWinkelmandjeService_ButDoesNotSendEvent() {
        when(winkelmandjeRepository.findById(winkelmandjeEvent.getKlantNummer()))
                .thenReturn(Optional.of(Winkelmandje.from(winkelmandjeEvent)));

        when(winkelmandjeRepository.save(Winkelmandje.from(winkelmandjeEvent)))
                .thenReturn(null);

        winkelmandjeService.updateExistingWinkelmandje(winkelmandjeEvent);

        var inOrder = inOrder(winkelmandjeRepository, winkelmandjeEventService);

        inOrder.verify(winkelmandjeRepository, times(1))
                .findById(winkelmandjeEvent.getKlantNummer());
        inOrder.verify(winkelmandjeRepository, times(1))
                .save(Winkelmandje.from(winkelmandjeEvent));
        inOrder.verify(winkelmandjeEventService, times(0))
                .sendWinkelmandjeToegevoegdEvent(winkelmandjeEvent);
    }
}