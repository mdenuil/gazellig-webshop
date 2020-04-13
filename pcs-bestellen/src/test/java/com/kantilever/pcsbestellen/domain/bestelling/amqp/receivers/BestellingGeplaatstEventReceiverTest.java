package com.kantilever.pcsbestellen.domain.bestelling.amqp.receivers;

import com.gazellig.amqpservice.amqp.audit.replay.AuditReplayStartListener;
import com.kantilever.pcsbestellen.domain.bestelling.services.BestellingProcessorService;
import com.kantilever.pcsbestellen.domain.bestelling.services.BestellingService;
import com.kantilever.pcsbestellen.domain.bestelling.amqp.events.BestellingEvent;
import com.kantilever.pcsbestellen.domain.bestelling.dto.AdresDto;
import com.kantilever.pcsbestellen.domain.bestelling.dto.BesteldArtikelDto;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONException;
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
class BestellingGeplaatstEventReceiverTest {

    @Value("${rabbitmq.topics.BestellingGeplaatstEvent}")
    private String topic;

    private BestellingEvent bestellingEvent;

    @MockBean
    private BestellingService bestellingService;

    @MockBean
    private AuditReplayStartListener auditReplayStartListener;

    @MockBean
    private BestellingProcessorService bestellingProcessorService;

    @Autowired
    private BestellingGeplaatstEventReceiver bestellingGeplaatstEventReceiver;

    @BeforeEach
    void init() {
        bestellingEvent = BestellingEvent.builder()
                .topic(topic)
                .bestelNummer(1L)
                .initialen("")
                .achternaam("")
                .email("")
                .afleverAdres(new AdresDto())
                .factuurAdres(new AdresDto())
                .artikelen(List.of(new BesteldArtikelDto(1, 1)))
                .build();
    }

    @Test
    @DisplayName("BestellingGeplaatstEventReceiver geeft de juiste topic terug waarop de Queue luistert")
    void receiverReturnsCorrectTopic() {
        assertThat(topic).isEqualTo(bestellingGeplaatstEventReceiver.getTopic());
    }

    @Test
    @DisplayName("BestellingGeplaatstEventReceiver kan een Event ontvangen met de juiste topic")
    void receiverSavesBestellingWithoutErrors() throws JSONException {
        bestellingGeplaatstEventReceiver.receive(bestellingEvent);

        verify(bestellingProcessorService, times(1)).handleBestellingEvent(bestellingEvent);
    }
}