package com.kantilever.bffwebwinkel.domain.bestelling.amqp.receivers;

import com.gazellig.amqpservice.amqp.audit.replay.AuditReplayStartListener;
import com.kantilever.bffwebwinkel.domain.bestelling.services.BestellingService;
import com.kantilever.bffwebwinkel.util.ObjectBuilders;
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
class BestellingToegevoegdEventReceiverTest {
    @MockBean
    private AuditReplayStartListener auditReplayStartListener;
    @MockBean
    private BestellingService bestellingService;
    @Autowired
    private BestellingToegevoegdEventReceiver bestellingToegevoegdEventReceiver;

    private String topic = "Kantilever.BestellenService.BestellingAanBestellenToegevoegdEvent";

    @Test
    @DisplayName("Topic for queue gets injected correctly")
    void topicInjectedCorrectly() {
        // Arrange
        var expected = topic;
        // Act
        // Assert
        assertThat(bestellingToegevoegdEventReceiver.getTopic()).isEqualTo(expected);
    }

    @Test
    @DisplayName("Correct service method is called from receive BestellingEvent")
    void name() {
        // Arrange
        var bestellingEvent = ObjectBuilders.getDefaultBestellingEventBuilder().build();
        // Act
        bestellingToegevoegdEventReceiver.receive(bestellingEvent);
        // Assert
        verify(bestellingService, times(1)).saveBestelling(bestellingEvent);
    }
}