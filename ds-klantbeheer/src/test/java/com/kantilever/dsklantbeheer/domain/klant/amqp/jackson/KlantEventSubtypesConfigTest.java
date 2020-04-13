package com.kantilever.dsklantbeheer.domain.klant.amqp.jackson;

import java.util.Set;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gazellig.amqpservice.amqp.audit.replay.AuditReplayStartListener;
import com.kantilever.dsklantbeheer.domain.klant.amqp.events.KlantEvent;
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
class KlantEventSubtypesConfigTest {
    @MockBean
    AuditReplayStartListener auditReplayStartListener;

    @Autowired
    KlantEventSubtypesConfig klantEventSubtypesConfig;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("Correct value is loaded as a topic from the application.properties")
    void topicLoads() {
        // Arrange
        // Act
        String topic = klantEventSubtypesConfig.getTopicKlantGeregistreerd();
        // Assert
        assertThat(topic).isEqualTo("Kantilever.BffWebwinkel.KlantGeregistreerdEvent");
    }

    @Test
    @DisplayName("The correct subtypes of KlantEvent are registered with the module")
    void subtypesGetRegistered() {
        // Arrange
        Set<Class<?>> registeredSubtypes = klantEventSubtypesConfig.getEventSubtypeModule().getRegisteredSubtypes();
        // Act
        // Assert
        assertThat(registeredSubtypes.contains(KlantGeregistreerd.class)).isTrue();
    }

    @Test
    @DisplayName("The subtype module is registered with the object mapper")
    void moduleRegistered() {
        // Arrange
        Set<Object> registeredModuleIds = objectMapper.getRegisteredModuleIds();
        // Act
        // Assert
        assertThat(registeredModuleIds.contains(KlantEvent.class)).isTrue();
    }
}