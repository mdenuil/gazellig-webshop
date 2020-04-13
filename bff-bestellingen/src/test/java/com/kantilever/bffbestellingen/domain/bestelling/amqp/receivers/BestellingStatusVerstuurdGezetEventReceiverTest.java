package com.kantilever.bffbestellingen.domain.bestelling.amqp.receivers;

import com.gazellig.amqpservice.amqp.audit.replay.AuditReplayStartListener;
import com.kantilever.bffbestellingen.domain.bestelling.models.BestelStatusType;
import com.kantilever.bffbestellingen.util.ObjectBuilders;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
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
class BestellingStatusVerstuurdGezetEventReceiverTest {
    @MockBean
    private AuditReplayStartListener auditReplayStartListener;
    @MockBean
    private BestellingEventReceiverService bestellingEventReceiverService;
    @Autowired
    private BestellingStatusVerstuurdGezetEventReceiver bestellingStatusVerstuurdGezetEventReceiver;
    private String topic = "Kantilever.BestellenService.BestellingStatusVerstuurdGezetEvent";

    @Test
    @DisplayName("Correct topic is injected into the class")
    void topicLoaded() {
        // Arrange
        // Act
        var actual = bestellingStatusVerstuurdGezetEventReceiver.getTopic();
        // Assert
        assertThat(actual).isEqualTo(topic);
    }

    @Test
    @DisplayName("bestellingEventReceiverService has the correct function called to handle event")
    void correctServiceFunctionCalled() {
        // Arrange
        var bestellingStatusEvent = ObjectBuilders.getDefaultBestellingStatusBuilder()
                .status(BestelStatusType.VERSTUURD)
                .build();
        // Act
        bestellingStatusVerstuurdGezetEventReceiver.receive(bestellingStatusEvent);
        // Assert
        verify(bestellingEventReceiverService, times(1)).handleBestellingStatusVerstuurdGezetEvent(bestellingStatusEvent);
    }
}