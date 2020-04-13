package com.gazellig.amqpservice.amqp.audit.aop;

import java.util.UUID;
import com.gazellig.amqpservice.amqp.audit.handledevents.HandledEventService;
import com.gazellig.amqpservice.amqp.events.AuditableEvent;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * SaveEventAfterConsumingAdvice ensures that all events that are received also have their correlationId
 * saved to the database to ensure events get only read once.
 */
@Aspect
@Component
@Log4j2
public class SaveEventAfterConsumingAspect {
    private HandledEventService handledEventService;

    @Autowired
    public SaveEventAfterConsumingAspect(HandledEventService handledEventService) {
        this.handledEventService = handledEventService;
    }

    @After("execution(* receive(..) ) && args(event)")
    public void saveEventAfterConsuming(AuditableEvent event) {
        UUID correlationId = event.getCorrelationId();

        if (!handledEventService.isExistingEvent(correlationId)) {
            handledEventService.saveEvent(correlationId);
            log.info(String.format("New event saved as handled on topic: %s. Event: %s", event.getTopic(), event));
        }
    }
}

