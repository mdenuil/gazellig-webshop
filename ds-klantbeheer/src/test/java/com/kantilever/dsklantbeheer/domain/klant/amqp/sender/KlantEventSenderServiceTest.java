package com.kantilever.dsklantbeheer.domain.klant.amqp.sender;

import com.fasterxml.jackson.core.JsonProcessingException;
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
class KlantEventSenderServiceTest {

    @MockBean
    private AuditReplayStartListener auditReplayStartListener;

    @MockBean
    private KlantEventSender klantEventSender;

    @Autowired
    private KlantEventSenderService klantEventSenderService;

    @Test
    @DisplayName("Correct topic is injected into the service")
    void correctTopicInjected() {
        // Arrange
        var expect = "Kantilever.KlantService.KlantToegevoegdEvent";
        // Act
        // Assert
        assertThat(klantEventSenderService.getTopicKlantToegevoegd()).isEqualTo(expect);
    }

    @Test
    @DisplayName("SendKlantToegevoegd Calls klantEventSender with correct Topic")
    void sendKlantToegevoegd_callsSendWithCorrect() throws JsonProcessingException {
        // Arrange
        var klant = ObjectBuilders.getDefaultKlant().build();
        var expected = ObjectBuilders.getDefaultKlantEvent()
                .topic("Kantilever.KlantService.KlantToegevoegdEvent")
                .build();
        // Act
        klantEventSenderService.sendKlantToegevoegdEvent(klant);
        // Assert
        verify(klantEventSender, times(1)).send(expected);
    }
}