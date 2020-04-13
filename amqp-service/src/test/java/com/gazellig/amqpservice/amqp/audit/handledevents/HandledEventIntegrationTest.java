package com.gazellig.amqpservice.amqp.audit.handledevents;

import java.util.UUID;

import com.gazellig.amqpservice.amqp.audit.replay.AuditReplayStartListener;
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
class HandledEventIntegrationTest {
    @MockBean
    private AuditReplayStartListener auditReplayStartListener;

    @Autowired
    private HandledEventService handledEventService;

    @Autowired
    private HandledEventRepository handledEventRepository;

    private HandledEvent handledEvent;

    @Test
    void contextLoads() {
        assertThat(auditReplayStartListener).isNotNull();
        assertThat(handledEventService).isNotNull();
        assertThat(handledEventRepository).isNotNull();
    }

    @Test
    @DisplayName("New HandledEvents get saved in the database")
    void newHandledEvents_getAddedToDatabase() {
        // Arrange
        UUID uuid = UUID.randomUUID();
        // Act
        handledEventService.saveEvent(uuid);
        // Assert
        assertThat(handledEventRepository.findById(uuid.toString()).isPresent()).isTrue();

    }

    @Test
    @DisplayName("isHandledEvents correctly signals an event has been saved")
    void isHandledEvents_correctlySignals_EventIsSaved() {
        // Arrange
        UUID uuid = UUID.randomUUID();
        handledEventService.saveEvent(uuid);
        // Act
        boolean expected = handledEventService.isHandledEvent(uuid);
        // Assert
        assertThat(expected).isEqualTo(true);
    }

    @Test
    @DisplayName("isHandledEvents correctly signals an event has not been saved")
    void isHandledEvents_correctlySignals_EventIsNotSaved() {
        // Arrange
        UUID uuid = UUID.randomUUID();
        // Act
        boolean expected = handledEventService.isHandledEvent(uuid);
        // Assert
        assertThat(expected).isEqualTo(false);
    }
}
