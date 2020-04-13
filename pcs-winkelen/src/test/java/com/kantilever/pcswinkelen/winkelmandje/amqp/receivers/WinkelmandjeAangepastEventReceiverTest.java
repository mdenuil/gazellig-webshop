package com.kantilever.pcswinkelen.winkelmandje.amqp.receivers;

import com.gazellig.amqpservice.amqp.audit.replay.AuditReplayStartListener;
import com.kantilever.pcswinkelen.winkelmandje.WinkelmandjeService;
import com.kantilever.pcswinkelen.winkelmandje.amqp.events.WinkelmandjeEvent;
import com.kantilever.pcswinkelen.winkelmandje.models.BesteldArtikel;
import java.util.List;
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
class WinkelmandjeAangepastEventReceiverTest {

    @Value("${rabbitmq.topics.WinkelmandjeAangepast}")
    private String actualTopic;

    @Autowired
    private WinkelmandjeAangepastEventReceiver winkelmandjeAangepastEventReceiver;

    @MockBean
    private AuditReplayStartListener auditReplayStartListener;

    @MockBean
    private WinkelmandjeService winkelmandjeService;

    @Test
    @DisplayName("Can retrieve correct topic from WinkelmandjeAangepastEventReceiver")
    void canRetrieveCorrectTopic() {
        var topic = winkelmandjeAangepastEventReceiver.getTopic();

        assertThat(topic).isEqualTo(actualTopic);
    }

    @Test
    @DisplayName("Verify with mockito that WinkelmandjeService executes the correct method when KlantEvent is received")
    void verifyThatWinkelmandjeServiceExecutesCorrectMethod() {
        var winkelmandjeEvent = WinkelmandjeEvent.builder()
                .klantNummer(323L)
                .artikelen(List.of(
                        BesteldArtikel.builder().artikelNummer(1).aantal(1).build()))
                .build();

        winkelmandjeAangepastEventReceiver.receive(winkelmandjeEvent);

        verify(winkelmandjeService, times(1)).updateExistingWinkelmandje(winkelmandjeEvent);
    }
}