package com.kantilever.bffbestellingen.domain.bestelling.amqp.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gazellig.amqpservice.amqp.audit.replay.AuditReplayStartListener;
import com.kantilever.bffbestellingen.domain.bestelling.amqp.events.bestellingstatus.BestellingStatusBehandelbaarGezetEvent;
import com.kantilever.bffbestellingen.domain.bestelling.amqp.events.bestellingstatus.BestellingStatusEvent;
import com.kantilever.bffbestellingen.domain.bestelling.amqp.events.bestellingstatus.BestellingStatusVerstuurdGezetEvent;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class BestellingStatusSubtypesConfigTest {
    @MockBean
    private AuditReplayStartListener auditReplayStartListener;

    @Autowired
    private BestellingStatusSubtypesConfig bestellingStatusSubtypesConfig;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Correct value is loaded for topics from the application.properties")
    void topicLoads() {
        // Arrange
        var expected = List.of(
                "Kantilever.BestellenService.BestellingStatusBehandelbaarGezetEvent",
                "Kantilever.BestellenService.BestellingStatusVerstuurdGezetEvent"
        );
        // Act
        // Assert
        assertThat(expected
                .contains(bestellingStatusSubtypesConfig.getTopicBestellingStatusBehandelbaarGezet()))
                .isTrue();
        assertThat(expected
                .contains(bestellingStatusSubtypesConfig.getTopicBestellingStatusVerstuurd()))
                .isTrue();
    }

    @Test
    @DisplayName("The correct subtypes of BestellingStatusEvent are registered with the module")
    void subtypesRegistered() {
        // Arrange
        Set<Class<?>> registeredSubtypes = bestellingStatusSubtypesConfig.getEventSubtypeModule().getRegisteredSubtypes();
        // Act
        // Assert
        assertThat(registeredSubtypes.contains(BestellingStatusBehandelbaarGezetEvent.class)).isTrue();
        assertThat(registeredSubtypes.contains(BestellingStatusVerstuurdGezetEvent.class)).isTrue();
    }

    @Test
    @DisplayName("The subtype module is registered with the object mapper")
    void subtypeModuleRegistered() {
        // Arrange
        Set<Object> registeredModuleIds = objectMapper.getRegisteredModuleIds();
        // Act
        // Assert
        assertThat(registeredModuleIds.contains(BestellingStatusEvent.class)).isTrue();
    }
}