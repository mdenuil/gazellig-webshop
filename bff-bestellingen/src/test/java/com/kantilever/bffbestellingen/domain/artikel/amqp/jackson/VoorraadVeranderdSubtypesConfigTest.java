package com.kantilever.bffbestellingen.domain.artikel.amqp.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gazellig.amqpservice.amqp.audit.replay.AuditReplayStartListener;
import com.kantilever.bffbestellingen.domain.artikel.amqp.events.voorraad.VoorraadEvent;
import com.kantilever.bffbestellingen.domain.artikel.amqp.events.voorraad.VoorraadVerhoogdEvent;
import com.kantilever.bffbestellingen.domain.artikel.amqp.events.voorraad.VoorraadVerlaagdEvent;
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
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class VoorraadVeranderdSubtypesConfigTest {
    @MockBean
    private AuditReplayStartListener auditReplayStartListener;

    @Autowired
    private VoorraadVeranderdSubtypesConfig voorraadVeranderdSubtypesConfig;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Correct value is loaded for topics from the application.properties")
    void topicLoads() {
        // Arrange
        var expected = List.of(
                "Kantilever.MagazijnService.VoorraadVerlaagdEvent",
                "Kantilever.MagazijnService.VoorraadVerhoogdEvent"
        );
        // Act
        // Assert
        assertThat(expected
                .contains(voorraadVeranderdSubtypesConfig.getTopicVoorraadVerhoogd()))
                .isTrue();
        assertThat(expected
                .contains(voorraadVeranderdSubtypesConfig.getTopicVoorraadVerlaagd()))
                .isTrue();
    }

    @Test
    @DisplayName("The correct subtypes of BestellingStatusEvent are registered with the module")
    void subtypesRegistered() {
        // Arrange
        Set<Class<?>> registeredSubtypes = voorraadVeranderdSubtypesConfig.getEventSubtypeModule().getRegisteredSubtypes();
        // Act
        // Assert
        assertThat(registeredSubtypes.contains(VoorraadVerhoogdEvent.class)).isTrue();
        assertThat(registeredSubtypes.contains(VoorraadVerlaagdEvent.class)).isTrue();
    }

    @Test
    @DisplayName("The subtype module is registered with the object mapper")
    void subtypeModuleRegistered() {
        // Arrange
        Set<Object> registeredModuleIds = objectMapper.getRegisteredModuleIds();
        // Act
        // Assert
        assertThat(registeredModuleIds.contains(VoorraadEvent.class)).isTrue();
    }

}