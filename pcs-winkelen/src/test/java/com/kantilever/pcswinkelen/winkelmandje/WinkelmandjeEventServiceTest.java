package com.kantilever.pcswinkelen.winkelmandje;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gazellig.amqpservice.amqp.audit.replay.AuditReplayStartListener;
import com.kantilever.pcswinkelen.winkelmandje.amqp.events.WinkelmandjeEvent;
import com.kantilever.pcswinkelen.winkelmandje.amqp.jackson.WinkelmandjeSubtypesConfig;
import com.kantilever.pcswinkelen.winkelmandje.amqp.queues.KlantToegevoegdConfig;
import com.kantilever.pcswinkelen.winkelmandje.amqp.receivers.KlantToegevoegdEventReceiver;
import com.kantilever.pcswinkelen.winkelmandje.amqp.senders.WinkelmandjeAanWinkelenToegevoegdEventSender;
import com.kantilever.pcswinkelen.winkelmandje.models.BesteldArtikel;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@SpringBootTest
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class WinkelmandjeEventServiceTest {

    private WinkelmandjeEvent winkelmandjeEvent;

    @MockBean
    private KlantToegevoegdConfig klantToegevoegdConfig;

    @MockBean
    private WinkelmandjeSubtypesConfig klantSubtypesConfig;

    @MockBean
    private KlantToegevoegdEventReceiver klantToegevoegdEventReceiver;

    @MockBean
    private WinkelmandjeRepository winkelmandjeRepository;

    @MockBean
    private WinkelmandjeService winkelmandjeService;

    @MockBean
    private WinkelmandjeAanWinkelenToegevoegdEventSender winkelmandjeAanWinkelenToegevoegdEventSender;

    @MockBean
    private AuditReplayStartListener auditReplayStartListener;

    @Autowired
    private WinkelmandjeEventService winkelmandjeEventService;

    @BeforeEach
    void init() {
        winkelmandjeEvent = WinkelmandjeEvent.builder()
                            .klantNummer(10L)
                            .artikelen(List.of(
                                    new BesteldArtikel(1, 1)
                            ))
                            .build();
    }

    @Test
    @DisplayName("Can send WinkelmandjeEvent to RabbitMQ when a Winkelmandje is saved")
    void canSendWinkelmandjeEvent_WhenWinkelmandjeIsSaved() throws JsonProcessingException {
        winkelmandjeEventService.sendWinkelmandjeToegevoegdEvent(winkelmandjeEvent);

        verify(winkelmandjeAanWinkelenToegevoegdEventSender, times(1)).send(winkelmandjeEvent);
    }

    @Test
    @DisplayName("Throws JsonProcessingException when WinkelmandjeToegevoegdSender goes wrong")
    void throwsJsonProcessingException() throws JsonProcessingException {
        doThrow(new JsonProcessingException("ai"){})
                .when(winkelmandjeAanWinkelenToegevoegdEventSender).send(winkelmandjeEvent);

        winkelmandjeEventService.sendWinkelmandjeToegevoegdEvent(winkelmandjeEvent);

        assertThatExceptionOfType(JsonProcessingException.class);
    }
}