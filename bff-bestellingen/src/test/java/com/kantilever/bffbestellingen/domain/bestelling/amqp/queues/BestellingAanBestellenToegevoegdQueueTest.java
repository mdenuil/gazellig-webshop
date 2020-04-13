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
class BestellingAanBestellenToegevoegdQueueTest {
    @MockBean
    private AuditReplayStartListener auditReplayStartListener;
    @Autowired
    private BestellingAanBestellenToegevoegdQueue bestellingAanBestellenToegevoegdQueue;
    @Autowired
    private Binding auditBestellingToegevoegdBinding;
    @Autowired
    private Binding topicBestellingToegevoegdBinding;

    private String topic = "Kantilever.BestellenService.BestellingAanBestellenToegevoegdEvent";
    private String identifier = "ToBffBestellingen";

    @Test
    @DisplayName("Topic for queue gets injected correctly")
    void topicInjectedCorrectly() {
        // Arrange
        var expected = topic;
        // Act
        // Assert
        assertThat(bestellingAanBestellenToegevoegdQueue.getTopic()).isEqualTo(expected);
    }

    @Test
    @DisplayName("Topic for queue gets injected correctly")
    void identifierInjectedCorrectly() {
        // Arrange
        var expected = identifier;
        // Act
        // Assert
        assertThat(bestellingAanBestellenToegevoegdQueue.getIdentifier()).isEqualTo(expected);
    }

    @Test
    @DisplayName("Topic queue gets created with the correct topic")
    void queueCreatedCorrectly() {
        // Arrange
        var expected = topic + identifier;
        // Act
        var actual = bestellingAanBestellenToegevoegdQueue.topicBestellingToegevoegdQueue();
        // Assert
        assertThat(actual.getActualName()).isEqualTo(expected);
    }

    @Test
    @DisplayName("Audit binding creates correct binding between queue and replay exchange")
    void auditBindingCreatedCorrectly() {
        // Arrange
        // Act
        // Assert
        assertThat(auditBestellingToegevoegdBinding.getExchange()).isEqualTo("Kantilever.Replay.Bestellingen");
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