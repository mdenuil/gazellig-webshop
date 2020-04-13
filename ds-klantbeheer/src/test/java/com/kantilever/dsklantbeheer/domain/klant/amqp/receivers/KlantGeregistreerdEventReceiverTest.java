package com.kantilever.dsklantbeheer.domain.klant.amqp.receivers;

import com.gazellig.amqpservice.amqp.audit.replay.AuditReplayStartListener;
import com.kantilever.dsklantbeheer.util.ObjectBuilders;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@ExtendWith(MockitoExtension.class)
class KlantGeregistreerdEventReceiverTest {
    @MockBean
    private AuditReplayStartListener auditReplayStartListener;

    @MockBean
    private KlantEventReceiverService klantEventReceiverService;

    @Autowired
    private KlantGeregistreerdEventReceiver klantGeregistreerdEventReceiver;

    @Test
    @DisplayName("Correct topic gets injected into the class")
    void correctTopicIsLoaded() {
        // Arrange
        var expected = "Kantilever.BffWebwinkel.KlantGeregistreerdEvent";
        // Act
        var actual = klantGeregistreerdEventReceiver.getTopic();
        // Assert
        assertThat(expected).isEqualTo(actual);
    }

    @Test
    @DisplayName("Receive calls the correct method in the service")
    void callsTheCorrectServiceMethod() {
        // Arrange
        var klantEvent = ObjectBuilders.getDefaultKlantEvent().build();
        // Act
        klantGeregistreerdEventReceiver.receive(klantEvent);
        // Assert
        verify(klantEventReceiverService, times(1)).handleKlantGeregistreerdEvent(klantEvent);
    }
}