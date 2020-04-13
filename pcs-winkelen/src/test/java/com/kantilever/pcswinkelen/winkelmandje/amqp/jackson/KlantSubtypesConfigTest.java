package com.kantilever.pcswinkelen.winkelmandje.amqp.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gazellig.amqpservice.amqp.audit.replay.AuditReplayStartListener;
import com.kantilever.pcswinkelen.winkelmandje.amqp.events.KlantEvent;
import com.kantilever.pcswinkelen.winkelmandje.amqp.events.KlantToegevoegdEvent;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
class KlantSubtypesConfigTest {

    @Value("${rabbitmq.topics.KlantToegevoegd}")
    private String klantToegevoegdTopic;

    @Autowired
    private KlantSubtypesConfig klantSubtypesConfig;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuditReplayStartListener auditReplayStartListener;

    @Test
    @DisplayName("Correct value is loaded for topics from the application.properties")
    void topicLoads() {
        var expected = List.of("Kantilever.KlantService.KlantToegevoegdEvent");

        assertThat(expected
                .contains(klantSubtypesConfig.getTopicKlantToegevoegd()))
                .isTrue();

        assertThat(klantSubtypesConfig.getTopicKlantToegevoegd()).isEqualTo(klantToegevoegdTopic);
    }

    @Test
    @DisplayName("The correct subtypes of BestellingStatusEvent are registered with the module")
    void subtypesRegistered() {
        Set<Class<?>> registeredSubtypes = klantSubtypesConfig.getEventSubtypeModule().getRegisteredSubtypes();

        assertThat(registeredSubtypes.contains(KlantToegevoegdEvent.class)).isTrue();
    }

    @Test
    @DisplayName("The subtype module is registered with the object mapper")
    void subtypeModuleRegistered() {
        Set<Object> registeredModuleIds = objectMapper.getRegisteredModuleIds();

        assertThat(registeredModuleIds.contains(KlantEvent.class)).isTrue();
    }
}