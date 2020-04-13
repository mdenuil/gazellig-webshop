package com.kantilever.bffbestellingen.domain.bestelling.amqp.senders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gazellig.amqpservice.amqp.audit.replay.AuditReplayStartListener;
import com.kantilever.bffbestellingen.domain.bestelling.amqp.events.bestellingstatus.BestellingStatusEvent;
import com.kantilever.bffbestellingen.domain.bestelling.models.BestelStatusType;
import com.kantilever.bffbestellingen.util.ObjectBuilders;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class BestellingEventSenderServiceTest {
    @MockBean
    private AuditReplayStartListener auditReplayStartListener;
    @MockBean
    private BestellingStatusEventSender bestellingStatusEventSender;
    @Autowired
    private BestellingEventSenderService bestellingEventSenderService;
    private String bestellingVerstuurdTopic = "Kantilever.BestellenService.BestellingVerstuurdEvent";

    @Test
    @DisplayName("Correct topic gets loaded from application.properties")
    void topicLoaded() {
        // Arrange
        // Act
        var actual = bestellingEventSenderService.getBestellingVerstuurdTopic();
        // Assert
        assertThat(actual).isEqualTo(bestellingVerstuurdTopic);
    }

    @Test
    @DisplayName("sendBestellingVerstuurd sends correctly build event")
    void sendBestellingVerstuurd_sendsCorrectEvent() throws JsonProcessingException {
        // Arrange
        long bestelNummer = 1L;
        var expected = BestellingStatusEvent.builder()
                .bestelNummer(bestelNummer)
                .topic(bestellingVerstuurdTopic)
                .status(BestelStatusType.VERSTUURD)
                .build();
        // Act
        bestellingEventSenderService.sendBestellingVerstuurd(bestelNummer);
        // Assert
        verify(bestellingStatusEventSender, times(1))
                .send(expected);
    }
}