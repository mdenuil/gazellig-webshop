package com.gazellig.amqpservice.amqp.audit.aop;

import com.gazellig.amqpservice.amqp.audit.handledevents.HandledEventService;
import com.gazellig.amqpservice.amqp.receivers.EventReceiver;
import com.gazellig.amqpservice.implementations.ArtikelToegevoegdEventReceiver;
import com.gazellig.amqpservice.implementations.ArtikelEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SaveEventAfterConsumingAspectTest {
    @Mock
    private HandledEventService handledEventService;
    private String topic;
    private SaveEventAfterConsumingAspect saveEventAfterConsumingAspect;
    private EventReceiver<ArtikelEvent> proxy;

    @BeforeEach
    void init() {
        topic = "mock.topic";
        saveEventAfterConsumingAspect = new SaveEventAfterConsumingAspect(handledEventService);
        var target = new ArtikelToegevoegdEventReceiver();
        var factory = new AspectJProxyFactory(target);
        factory.addAspect(saveEventAfterConsumingAspect);
        proxy = factory.getProxy();
    }

    @Test
    @DisplayName("receive functions correctly trigger the Aspect advice")
    void receiveFunction_correctlyTriggersAdvice() {
        // Arrange
        var event = ArtikelEvent.builder().build();
        var uuid = event.getCorrelationId();
        // Act
        proxy.receive(event);
        // Assert
        verify(handledEventService, times(1)).saveEvent(uuid);
    }
}