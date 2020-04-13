package com.kantilever.bffbestellingen.domain.artikel.amqp.receivers;

import com.gazellig.amqpservice.amqp.audit.replay.AuditReplayStartListener;
import com.kantilever.bffbestellingen.domain.artikel.services.ArtikelService;
import com.kantilever.bffbestellingen.util.ObjectBuilders;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
class VoorraadVerlaagdEventReceiverTest {
    @MockBean
    private AuditReplayStartListener auditReplayStartListener;
    @MockBean
    private ArtikelService artikelService;
    @Autowired
    private VoorraadVerlaagdEventReceiver voorraadVerlaagdEventReceiver;

    private String topic = "Kantilever.MagazijnService.VoorraadVerlaagdEvent";

    @Test
    @DisplayName("Correct topic is injected into the class")
    void topicLoaded() {
        // Arrange
        // Act
        var actual = voorraadVerlaagdEventReceiver.getTopic();
        // Assert
        assertThat(actual).isEqualTo(topic);
    }

    @Test
    @DisplayName("Receive calls the correct service function")
    void correctServiceFunctionCalled() {
        // Arrange
        var voorraadEvent = ObjectBuilders.getDefaultVoorraadEvent().build();
        // Act
        voorraadVerlaagdEventReceiver.receive(voorraadEvent);
        // Assert
        verify(artikelService, times(1)).verlaagAantal(voorraadEvent);
    }

}