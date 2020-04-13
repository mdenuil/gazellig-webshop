package com.gazellig.amqpservice.amqp.audit.replay;

import java.util.UUID;
import com.gazellig.amqpservice.amqp.audit.handledevents.HandledEventService;
import com.gazellig.amqpservice.amqp.events.AuditableEvent;
import com.gazellig.amqpservice.implementations.ArtikelToegevoegdEventReceiver;
import com.gazellig.amqpservice.implementations.ArtikelEvent;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuditReplayEventReceiverTest {
    @Mock
    private HandledEventService handledEventService;

    @Mock
    private AuditReplayService auditReplayService;

    private AuditReplayEventReceiver auditReplayEventReceiver;

    private AuditableEvent event;

    @BeforeEach
    void init() {
        this.auditReplayEventReceiver = new AuditReplayEventReceiver(
                handledEventService,
                auditReplayService
        );
    }

    @Test
    @DisplayName("If a message is received without a EventType a nullpointer is thrown")
    void handle_withNullEventType_throwsNullpointerException() {
        // Arrange
        event = ArtikelEvent.builder().build();
        // Act
        // Assert
        assertThatThrownBy(() -> auditReplayEventReceiver.receive(event))
                .isInstanceOf(NullPointerException.class);

    }

    @Test
    @DisplayName("If a message payload body has no correlationId a nullpointer is thrown")
    void handle_withNullCorrelationId_throwsNullpointerException() {
        // Arrange
        event = ArtikelEvent.builder().topic("test.exchange.ArtikelAanCatalogusToegevoegd").build();
        event.setCorrelationId(null);
        // Act
        // Assert
        assertThatThrownBy(() -> auditReplayEventReceiver.receive(event))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("When a new receiver is registered the correct topic gets added to the replayService")
    void registerReceiver_callsAddNewTopic_withCorrectTopic() {
        // Arrange
        var receiverMock = mock(ArtikelToegevoegdEventReceiver.class);
        when(receiverMock.getTopic()).thenReturn("mock.topic");
        when(receiverMock.getPriority()).thenReturn(100);
        // Act
        auditReplayEventReceiver.registerReceiver(receiverMock);
        // Assert
        verify(auditReplayService, times(1)).addNewReplayRequest("mock.topic", 100);
    }

    @Test
    @DisplayName("When a new receiver is registered it is added to the list of registered receivers")
    void registerreceiver_addsRegisteredReceiver_toReceiverList() {
        // Arrange
        var receiverMock = mock(ArtikelToegevoegdEventReceiver.class);
        when(receiverMock.getTopic()).thenReturn("mock.topic");
        // Act
        auditReplayEventReceiver.registerReceiver(receiverMock);
        // Assert
        assertThat(auditReplayEventReceiver.getReceivers().size()).isEqualTo(1);
        Assertions.assertThat(auditReplayEventReceiver.getReceivers().get(0).getTopic()).isEqualTo("mock.topic");
    }

    @Test
    @DisplayName("When receive is called with a known topic the correct receiver is used")
    void receive_withRegisteredTopic_callsCorrectReceiver() {
        // Arrange
        var receiverMock = mock(ArtikelToegevoegdEventReceiver.class);
        var eventMock = mock(ArtikelEvent.class);
        var uuid = UUID.randomUUID();
        when(receiverMock.getTopic()).thenReturn("mock.topic");
        when(eventMock.getTopic()).thenReturn("mock.topic");
        when(eventMock.getCorrelationId()).thenReturn(uuid);
        auditReplayEventReceiver.registerReceiver(receiverMock);
        // Act
        auditReplayEventReceiver.receive(eventMock);
        // Assert
        verify(receiverMock, times(1)).receive(eventMock);
    }

    @Test
    @DisplayName("When the correlationId of an event is already known it is not handled again")
    void receive_withHandledCorrelationId_isNotHandledAgain() {
        // Arrange
        var receiverMock = mock(ArtikelToegevoegdEventReceiver.class);
        var eventMock = mock(ArtikelEvent.class);
        var uuid = UUID.randomUUID();
        when(receiverMock.getTopic()).thenReturn("mock.topic");
        when(eventMock.getTopic()).thenReturn("mock.topic");
        when(eventMock.getCorrelationId()).thenReturn(uuid);
        when(handledEventService.isHandledEvent(uuid)).thenReturn(true);
        auditReplayEventReceiver.registerReceiver(receiverMock);
        // Act
        auditReplayEventReceiver.receive(eventMock);
        // Assert
        verify(receiverMock, times(0)).receive(eventMock);
    }

    @Test
    @DisplayName("When a correlationId is already known decreaseToReceive is still called")
    void receive_callsDecreaseToReceive_withHandledEvent() {
        var receiverMock = mock(ArtikelToegevoegdEventReceiver.class);
        var eventMock = mock(ArtikelEvent.class);
        var uuid = UUID.randomUUID();
        when(receiverMock.getTopic()).thenReturn("mock.topic");
        when(eventMock.getTopic()).thenReturn("mock.topic");
        when(eventMock.getCorrelationId()).thenReturn(uuid);
        when(handledEventService.isHandledEvent(uuid)).thenReturn(true);
        auditReplayEventReceiver.registerReceiver(receiverMock);
        // Act
        auditReplayEventReceiver.receive(eventMock);
        // Assert
        verify(auditReplayService, times(1)).decreaseToReceive();
    }

    @Test
    @DisplayName("When a correlationId is not known decreaseToReceive is called")
    void receive_callsDecreaseToReceive_withNotHandledEvent() {
        var receiverMock = mock(ArtikelToegevoegdEventReceiver.class);
        var eventMock = mock(ArtikelEvent.class);
        var uuid = UUID.randomUUID();
        when(receiverMock.getTopic()).thenReturn("mock.topic");
        when(eventMock.getTopic()).thenReturn("mock.topic");
        when(eventMock.getCorrelationId()).thenReturn(uuid);
        when(handledEventService.isHandledEvent(uuid)).thenReturn(false);
        auditReplayEventReceiver.registerReceiver(receiverMock);
        // Act
        auditReplayEventReceiver.receive(eventMock);
        // Assert
        verify(auditReplayService, times(1)).decreaseToReceive();
    }

    @Test
    @DisplayName("When an Event with unknown topic is received it is handled gracefully")
    void receive_withUnknownEventTopic_isHandledGracefully() {
        var receiverMock = mock(ArtikelToegevoegdEventReceiver.class);
        var eventMock = mock(ArtikelEvent.class);
        var uuid = UUID.randomUUID();
        when(receiverMock.getTopic()).thenReturn("mock.topic");
        when(eventMock.getTopic()).thenReturn("mock.wrong.topic");
        when(eventMock.getCorrelationId()).thenReturn(uuid);
        when(handledEventService.isHandledEvent(uuid)).thenReturn(true);
        auditReplayEventReceiver.registerReceiver(receiverMock);
        // Act
        auditReplayEventReceiver.receive(eventMock);
        // Assert
        verify(auditReplayService, times(1)).decreaseToReceive();

    }
}