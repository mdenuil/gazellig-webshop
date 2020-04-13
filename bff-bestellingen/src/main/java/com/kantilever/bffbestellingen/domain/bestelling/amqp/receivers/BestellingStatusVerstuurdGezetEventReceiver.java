package com.kantilever.bffbestellingen.domain.bestelling.amqp.receivers;

import com.gazellig.amqpservice.amqp.audit.receivers.AuditableEventReceiver;
import com.kantilever.bffbestellingen.domain.bestelling.amqp.events.bestellingstatus.BestellingStatusEvent;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * BestellingStatusVerstuurdGezetEventReceiver received events for {@link BestellingStatusEvent} where
 * {@link com.kantilever.bffbestellingen.domain.bestelling.models.BestelStatusType} is VERSTUURD
 * <p>
 * This class implements AuditableEventReceiver and registers itself with the
 * {@link com.gazellig.amqpservice.amqp.audit.replay.AuditReplayEventReceiver}.
 */
@Log4j2
@Component
public class BestellingStatusVerstuurdGezetEventReceiver extends AuditableEventReceiver<BestellingStatusEvent> {
    @Value("${rabbitmq.topics.BestellingStatusVerstuurd}")
    private String topic;
    private BestellingEventReceiverService bestellingEventReceiverService;

    @Autowired
    public BestellingStatusVerstuurdGezetEventReceiver(BestellingEventReceiverService bestellingEventReceiverService) {
        this.bestellingEventReceiverService = bestellingEventReceiverService;
    }

    @Override
    @RabbitListener(
            queues = "#{topicBestellingStatusVerstuurdQueue.name}",
            id = "BestellingStatusVerstuurd",
            autoStartup = "false"
    )
    public void receive(BestellingStatusEvent event) {
        bestellingEventReceiverService.handleBestellingStatusVerstuurdGezetEvent(event);
    }

    @Override
    public String getTopic() {
        return topic;
    }
}
