package com.gazellig.amqpservice.amqp.audit.replay;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import com.gazellig.amqpservice.amqp.audit.handledevents.HandledEventService;
import com.gazellig.amqpservice.amqp.audit.receivers.AuditableEventReceiver;
import com.gazellig.amqpservice.amqp.events.AuditableEvent;
import com.gazellig.amqpservice.amqp.receivers.EventReceiver;
import com.google.common.annotations.VisibleForTesting;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * AuditReplayEventReceiver is the Event handler for all events coming from the AuditReplayQueue.
 * <p>
 * All events coming into the AuditReplayQueue are based on other receivers.
 * <p>
 * Every Receiver that extends {@link AuditableEventReceiver} registers itself with
 * this class and a @Configuration has to be created that binds the receivers Topic to the AuditReplayQueue.
 * <p>
 * From that point all on Events that the receiver handles also get handled by the AuditReplay
 */
@Log4j2
@Component
public class AuditReplayEventReceiver implements EventReceiver<AuditableEvent> {
    private HandledEventService handledEventService;
    private AuditReplayService auditReplayService;
    private List<AuditableEventReceiver<AuditableEvent>> receivers;

    @Autowired
    public AuditReplayEventReceiver(HandledEventService handledEventService,
                                    AuditReplayService auditReplayService) {
        this.handledEventService = handledEventService;
        this.auditReplayService = auditReplayService;
        this.receivers = new ArrayList<>();
    }

    /**
     * Receive a generic event from the AuditlogReplay and see if a receiver is present to handle the event.
     * <p>
     * If a receiver is present call that receiver with the received event.
     *
     * @param event Event to receive.
     */
    @Override
    @RabbitListener(
            queues = "#{auditQueue.name}",
            id = "AuditReplayQueue",
            autoStartup = "true"
    )
    public void receive(AuditableEvent event) {
        checkNotNull(event.getCorrelationId());
        checkNotNull(event.getTopic());
        auditReplayService.decreaseToReceive();

        if (handledEventService.isHandledEvent(event.getCorrelationId())) {
            return;
        }

        // Get the receiver for the received event
        Optional<AuditableEventReceiver<AuditableEvent>> receiver = receivers.stream()
                .filter(auditableEventReceiver -> auditableEventReceiver.getTopic().equals(event.getTopic()))
                .findFirst();

        receiver.ifPresentOrElse(
                auditableEventReceiver -> auditableEventReceiver.receive(event),
                () -> log.info(String.format("Unknown event: %s", event))
        );

    }

    @VisibleForTesting
    List<AuditableEventReceiver<AuditableEvent>> getReceivers() {
        return receivers;
    }

    @SuppressWarnings(value = "unchecked")
    public void registerReceiver(AuditableEventReceiver<? extends AuditableEvent> receiver) {
        auditReplayService.addNewReplayRequest(receiver.getTopic(), receiver.getPriority());
        receivers.add((AuditableEventReceiver<AuditableEvent>) receiver);
    }
}
