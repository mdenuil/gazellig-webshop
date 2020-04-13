package com.kantilever.bffbestellingen.domain.artikel.amqp.queues;

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
class ArtikelToegevoegdQueueTest {
    @MockBean
    private AuditReplayStartListener auditReplayStartListener;
    @Autowired
    private ArtikelToegevoegdQueue artikelToegevoegdQueue;
    @Autowired
    private Binding auditArtikelToegevoegdBinding;
    @Autowired
    private Binding topicArtikelToegevoegdBinding;

    private String topic = "Kantilever.CatalogusService.ArtikelAanCatalogusToegevoegd";
    private String identifier = "ToBffBestellingen";

    @Test
    @DisplayName("Topic for queue gets injected correctly")
    void topicInjectedCorrectly() {
        // Arrange
        var expected = topic;
        // Act
        // Assert
        assertThat(artikelToegevoegdQueue.getTopic()).isEqualTo(expected);
    }

    @Test
    @DisplayName("Identifier for queue gets injected correctly")
    void identifierInjectedCorrectly() {
        // Arrange
        var expected = identifier;
        // Act
        // Assert
        assertThat(artikelToegevoegdQueue.getIdentifier()).isEqualTo(expected);
    }

    @Test
    @DisplayName("Topic queue gets created with the correct topic")
    void queueCreatedCorrectly() {
        // Arrange
        var expected = topic + identifier;
        // Act
        var actual = artikelToegevoegdQueue.topicArtikelToegevoegdQueue();
        // Assert
        assertThat(actual.getActualName()).isEqualTo(expected);
    }

    @Test
    @DisplayName("Audit binding creates correct binding between queue and replay exchange")
    void auditBindingCreatedCorrectly() {
        // Arrange
        // Act
        // Assert
        assertThat(auditArtikelToegevoegdBinding.getExchange()).isEqualTo("Kantilever.Replay.Bestellingen");
        assertThat(auditArtikelToegevoegdBinding.getRoutingKey()).isEqualTo(topic);
    }

    @Test
    @DisplayName("Topic binding creates correct binding between queue and kantilever exchange")
    void topicBindingCreatedCorrectly() {
        // Arrange
        // Act
        // Assert
        assertThat(topicArtikelToegevoegdBinding.getExchange()).isEqualTo("Kantilever.Eventbus");
        assertThat(topicArtikelToegevoegdBinding.getRoutingKey()).isEqualTo(topic);
    }

}