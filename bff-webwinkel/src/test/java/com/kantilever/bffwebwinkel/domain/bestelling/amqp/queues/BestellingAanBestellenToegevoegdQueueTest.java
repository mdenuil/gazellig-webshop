package com.kantilever.bffwebwinkel.domain.bestelling.amqp.queues;

import com.gazellig.amqpservice.amqp.audit.replay.AuditReplayStartListener;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Binding;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class BestellingAanBestellenToegevoegdQueueTest {
    @MockBean
    private AuditReplayStartListener auditReplayStartListener;
    @Autowired
    private BestellingAanBestellenToegevoegdQueue bestellingStatusBehandelbaarQueue;
    @Autowired
    private Binding auditBestellingToegevoegdBinding;
    @Autowired
    private Binding topicBestellingToegevoegdBinding;

    private String topic = "Kantilever.BestellenService.BestellingAanBestellenToegevoegdEvent";

    @Test
    @DisplayName("Topic for queue gets injected correctly")
    void topicInjectedCorrectly() {
        // Arrange
        var expected = topic;
        // Act
        // Assert
        assertThat(bestellingStatusBehandelbaarQueue.getTopic()).isEqualTo(expected);
    }

    @Test
    @DisplayName("Topic queue gets created with the correct topic")
    void queueCreatedCorrectly() {
        // Arrange
        var expected = topic + "ToBffWebwinkel";
        // Act
        var actual = bestellingStatusBehandelbaarQueue.topicBestellingToegevoegdQueue();
        // Assert
        assertThat(actual.getActualName()).isEqualTo(expected);
    }

    @Test
    @DisplayName("Audit binding creates correct binding between queue and replay exchange")
    void auditBindingCreatedCorrectly() {
        // Arrange
        // Act
        // Assert
        assertThat(auditBestellingToegevoegdBinding.getExchange()).isEqualTo("Kantilever.Replay.Webwinkel");
        assertThat(auditBestellingToegevoegdBinding.getRoutingKey()).isEqualTo(topic);
    }

    @Test
    @DisplayName("Topic binding creates correct binding between queue and kantilever exchange")
    void topicBindingCreatedCorrectly() {
        // Arrange
        // Act
        // Assert
        assertThat(topicBestellingToegevoegdBinding.getExchange()).isEqualTo("Kantilever.Eventbus");
        assertThat(topicBestellingToegevoegdBinding.getRoutingKey()).isEqualTo(topic);
    }
}