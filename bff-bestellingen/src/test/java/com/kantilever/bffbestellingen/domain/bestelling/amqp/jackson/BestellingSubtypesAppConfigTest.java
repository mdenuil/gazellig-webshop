package com.kantilever.bffbestellingen.domain.bestelling.amqp.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gazellig.amqpservice.amqp.audit.replay.AuditReplayStartListener;
import com.kantilever.bffbestellingen.domain.bestelling.amqp.events.bestelling.BestellingAanBestellenToegevoegdEvent;
import com.kantilever.bffbestellingen.domain.bestelling.amqp.events.bestelling.BestellingEvent;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
class BestellingSubtypesAppConfigTest {
    @MockBean
    private AuditReplayStartListener auditReplayStartListener;

    @Autowired
    private BestellingSubtypesConfig bestellingSubtypesConfig;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Correct value is loaded for topics from the application.properties")
    void topicLoads() {
        // Arrange
        var expected = "Kantilever.BestellenService.BestellingAanBestellenToegevoegdEvent";
        // Act
        // Assert
        assertThat(bestellingSubtypesConfig.getTopicBestellingAanBestellenToegevoegd())
                .isEqualTo(expected);
    }

    @Test
    @DisplayName("The correct subtypes of BestellingStatusEvent are registered with the module")
    void subtypesRegistered() {
        // Arrange
        Set<Class<?>> registeredSubtypes = bestellingSubtypesConfig.getEventSubtypeModule().getRegisteredSubtypes();
        // Act
        // Assert
        assertThat(registeredSubtypes.contains(BestellingAanBestellenToegevoegdEvent.class)).isTrue();
    }

    @Test
    @DisplayName("The subtype module is registered with the object mapper")
    void subtypeModuleRegistered() {
        // Arrange
        Set<Object> registeredModuleIds = objectMapper.getRegisteredModuleIds();
        // Act
        // Assert
        assertThat(registeredModuleIds.contains(BestellingEvent.class)).isTrue();
    }
}