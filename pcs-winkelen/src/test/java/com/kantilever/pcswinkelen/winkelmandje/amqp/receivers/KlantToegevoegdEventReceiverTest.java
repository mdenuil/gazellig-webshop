package com.kantilever.pcswinkelen.winkelmandje.amqp.receivers;

import com.gazellig.amqpservice.amqp.audit.replay.AuditReplayStartListener;
import com.kantilever.pcswinkelen.winkelmandje.WinkelmandjeService;
import com.kantilever.pcswinkelen.winkelmandje.amqp.events.KlantEvent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class KlantToegevoegdEventReceiverTest {

    @Value("${rabbitmq.topics.KlantToegevoegd}")
    private String actualTopic;

    @Autowired
    private KlantToegevoegdEventReceiver klantToegevoegdEventReceiver;

    @MockBean
    private AuditReplayStartListener auditReplayStartListener;

    @MockBean
    private WinkelmandjeService winkelmandjeService;

    @Test
    @DisplayName("Can retrieve correct topic from KlantToegevoegdEventReceiver")
    void canRetrieveCorrectTopic() {
        var topic = klantToegevoegdEventReceiver.getTopic();

        assertThat(topic).isEqualTo(actualTopic);
    }

    @Test
    @DisplayName("Verify with mockito that WinkelmandjeService executes the correct method when KlantEvent is received")
    void verifyThatWinkelmandjeServiceExecutesCorrectMethod() {
        var klantEvent = KlantEvent.builder()
                .klantNummer(323L)
                .build();

        klantToegevoegdEventReceiver.receive(klantEvent);

        verify(winkelmandjeService, times(1)).saveNewWinkelmandje(klantEvent);
    }
}