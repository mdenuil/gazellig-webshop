package com.kantilever.bffbestellingen.domain.bestelling.amqp.queues;

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
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class BestellingStatusBehandelbaarQueueTest {
    @MockBean
    private AuditReplayStartListener auditReplayStartListener;
    @Autowired
    private BestellingStatusBehandelbaarQueue bestellingStatusBehandelbaarQueue;
    @Autowired
    private Binding auditBestellingStatusBehandelbaarBinding;
    @Autowired
    private Binding topicBestellingStatusBehandelbaarBinding;

    private String topic = "Kantilever.BestellenService.BestellingStatusBehandelbaarGezetEvent";
    private String identifier = "ToBffBestellingen";

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
    @DisplayName("Topic for queue gets injected correctly")
    void identifierInjectedCorrectly() {
        // Arrange
        var expected = identifier;
        // Act
        // Assert
        assertThat(bestellingStatusBehandelbaarQueue.getIdentifier()).isEqualTo(expected);
    }

    @Test
    @DisplayName("Topic queue gets created with the correct topic")
    void queueCreatedCorrectly() {
        // Arrange
        var expected = topic + identifier;
        // Act
        var actual = bestellingStatusBehandelbaarQueue.topicBestellingStatusBehandelbaarQueue();
        // Assert
        assertThat(actual.getActualName()).isEqualTo(expected);
    }

    @Test
    @DisplayName("Audit binding creates correct binding between queue and replay exchange")
    void auditBindingCreatedCorrectly() {
        // Arrange
        // Act
        // Assert
        assertThat(auditBestellingStatusBehandelbaarBinding.getExchange()).isEqualTo("Kantilever.Replay.Bestellingen");
        assertThat(auditBestellingStatusBehandelbaarBinding.getRoutingKey()).isEqualTo(topic);
    }

    @Test
    @DisplayName("Topic binding creates correct binding between queue and kantilever exchange")
    void topicBindingCreatedCorrectly() {
        // Arrange
        // Act
        // Assert
        assertThat(topicBestellingStatusBehandelbaarBinding.getExchange()).isEqualTo("Kantilever.Eventbus");
        assertThat(topicBestellingStatusBehandelbaarBinding.getRoutingKey()).isEqualTo(topic);
    }
}