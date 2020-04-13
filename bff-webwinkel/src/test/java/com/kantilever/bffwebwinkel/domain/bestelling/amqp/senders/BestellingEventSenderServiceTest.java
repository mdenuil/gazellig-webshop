package com.kantilever.bffwebwinkel.domain.bestelling.amqp.senders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gazellig.amqpservice.amqp.audit.replay.AuditReplayStartListener;
import com.kantilever.bffwebwinkel.domain.klant.services.KlantService;
import com.kantilever.bffwebwinkel.util.ObjectBuilders;
import javassist.NotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class BestellingEventSenderServiceTest {
    @MockBean
    private AuditReplayStartListener auditReplayStartListener;
    @MockBean
    private BestellingEventSender bestellingEventSender;
    @Autowired
    private BestellingEventSenderService bestellingEventSenderService;

    private String topic = "Kantilever.BffWebwinkel.BestellingGeplaatstEvent";

    @Test
    @DisplayName("Topic for queue gets injected correctly")
    void topicInjectedCorrectly() {
        // Arrange
        var expected = topic;
        // Act
        // Assert
        assertThat(bestellingEventSenderService.getTopicVoegBestellingToeAanBestellen()).isEqualTo(expected);
    }

    @Test
    @DisplayName("sendBestellingGeplaatstEvent builds and sends the correct event")
    void sendBestelling_buildsBestellingCorrectly() throws JsonProcessingException {
        // Arrange
        var klantId = 1L;
        var bestellingDto = ObjectBuilders.getDefaultBestellingDtoBuilder().build();
        var expectedEvent = ObjectBuilders.getDefaultBestellingEventBuilder()
                .bestelNummer(null)
                .status(null)
                .klantNummer(klantId)
                .build();
        // Act
        bestellingEventSenderService.sendBestellingGeplaatstEvent(bestellingDto, klantId);
        // Assert
        verify(bestellingEventSender, times(1)).send(expectedEvent);
    }
}