package com.kantilever.pcswinkelen.winkelmandje.amqp.senders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gazellig.amqpservice.amqp.audit.replay.AuditReplayStartListener;
import com.gazellig.amqpservice.amqp.audit.senders.AuditableEventSender;
import com.kantilever.pcswinkelen.winkelmandje.WinkelmandjeEventService;
import com.kantilever.pcswinkelen.winkelmandje.amqp.events.WinkelmandjeEvent;
import com.kantilever.pcswinkelen.winkelmandje.models.BesteldArtikel;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@SpringBootTest
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class WinkelmandjeAanWinkelenToegevoegdEventSenderTest {

    @Value("${rabbitmq.topics.WinkelmandjeAanWinkelenToegevoegd}")
    private String winkelmandjeTopic;

    @Autowired
    private WinkelmandjeAanWinkelenToegevoegdEventSender winkelmandjeAanWinkelenToegevoegdEventSender;

    @MockBean
    private WinkelmandjeEventService winkelmandjeEventService;

    @MockBean
    private AuditReplayStartListener auditReplayStartListener;

    @Mock
    private AuditableEventSender<WinkelmandjeEvent> auditableEventSender;

    @Test
    @DisplayName("Correct topic gets loaded from application.properties")
    void topicLoaded() {
        var actual = winkelmandjeAanWinkelenToegevoegdEventSender.getTopic();

        assertThat(actual).isEqualTo(winkelmandjeTopic);
    }
}