package com.kantilever.bffwebwinkel.domain.klant.amqp.queues;

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
class KlantToegevoegdQueueTest {
    @MockBean
    private AuditReplayStartListener auditReplayStartListener;
    @Autowired
    private KlantToegevoegdQueue klantToegevoegdQueue;
    @Autowired
    private Binding auditKlantToegevoegdEventBinding;
    @Autowired
    private Binding topicKlantToegevoegdEventBinding;

    private String topic = "Kantilever.KlantService.KlantToegevoegdEvent";

    @Test
    @DisplayName("Topic for queue gets injected correctly")
    void topicInjectedCorrectly() {
        // Arrange
        var expected = topic;
        // Act
        // Assert
        assertThat(klantToegevoegdQueue.getTopic()).isEqualTo(expected);
    }

    @Test
    @DisplayName("Topic queue gets created with the correct topic")
    void queueCreatedCorrectly() {
        // Arrange
        var expected = topic + "ToBffWebwinkel";
        // Act
        var actual = klantToegevoegdQueue.topicKlantToegevoegdEventQueue();
        // Assert
        assertThat(actual.getActualName()).isEqualTo(expected);
    }

    @Test
    @DisplayName("Audit binding creates correct binding between queue and replay exchange")
    void auditBindingCreatedCorrectly() {
        // Arrange
        // Act
        // Assert
        assertThat(auditKlantToegevoegdEventBinding.getExchange())
                .isEqualTo("Kantilever.Replay.Webwinkel");
        assertThat(auditKlantToegevoegdEventBinding.getRoutingKey())
                .isEqualTo(topic);
    }

    @Test
    @DisplayName("Topic binding creates correct binding between queue and kantilever exchange")
    void topicBindingCreatedCorrectly() {
        // Arrange
        // Act
        // Assert
        assertThat(topicKlantToegevoegdEventBinding.getExchange()).isEqualTo("Kantilever.Eventbus");
        assertThat(topicKlantToegevoegdEventBinding.getRoutingKey()).isEqualTo(topic);
    }
}