package com.kantilever.dsklantbeheer.domain.klant.amqp.queues;

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
class KlantGeregistreerdEventQueueTest {
    @MockBean
    private AuditReplayStartListener auditReplayStartListener;
    @Autowired
    private KlantGeregistreerdEventQueue klantGeregistreerdEventQueue;
    @Autowired
    private Binding auditKlantGeregistreerdEventBinding;
    @Autowired
    private Binding topicKlantGeregistreerdEventBinding;

    private String topic = "Kantilever.BffWebwinkel.KlantGeregistreerdEvent";

    @Test
    @DisplayName("Topic for queue gets injected correctly")
    void topicInjectedCorrectly() {
        // Arrange
        var expected = topic;
        // Act
        // Assert
        assertThat(klantGeregistreerdEventQueue.getTopic()).isEqualTo(expected);
    }

    @Test
    @DisplayName("Topic queue gets created with the correct topic")
    void queueCreatedCorrectly() {
        // Arrange
        var expected = topic + "ToDsKlant";
        // Act
        var actual = klantGeregistreerdEventQueue.topicKlantGeregistreerdEventQueue();
        // Assert
        assertThat(actual.getActualName()).isEqualTo(expected);
    }

    @Test
    @DisplayName("Audit binding creates correct binding between queue and replay exchange")
    void auditBindingCreatedCorrectly() {
        // Arrange
        // Act
        // Assert
        assertThat(auditKlantGeregistreerdEventBinding.getExchange()).isEqualTo("Kantilever.Replay.Klantbeheer");
        assertThat(auditKlantGeregistreerdEventBinding.getRoutingKey()).isEqualTo(topic);
    }

    @Test
    @DisplayName("Topic binding creates correct binding between queue and kantilever exchange")
    void topicBindingCreatedCorrectly() {
        // Arrange
        // Act
        // Assert
        assertThat(topicKlantGeregistreerdEventBinding.getExchange()).isEqualTo("Kantilever.Eventbus");
        assertThat(topicKlantGeregistreerdEventBinding.getRoutingKey()).isEqualTo(topic);
    }
}