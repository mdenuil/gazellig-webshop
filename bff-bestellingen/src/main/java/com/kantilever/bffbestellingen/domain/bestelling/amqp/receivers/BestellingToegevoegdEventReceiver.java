package com.kantilever.bffbestellingen.domain.bestelling.amqp.receivers;

import com.gazellig.amqpservice.amqp.audit.receivers.AuditableEventReceiver;
import com.kantilever.bffbestellingen.domain.bestelling.amqp.events.bestelling.BestellingEvent;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * BestellingToegevoegdEventReceiver received events for {@link BestellingEvent} where a new
 * {@link com.kantilever.bffbestellingen.domain.bestelling.models.Bestelling} was added.
 * <p>
 * This class implements AuditableEventReceiver and registers itself with the
 * {@link com.gazellig.amqpservice.amqp.audit.replay.AuditReplayEventReceiver}.
 */
@Log4j2
@Component
public class BestellingToegevoegdEventReceiver extends AuditableEventReceiver<BestellingEvent> {
    @Value("${rabbitmq.topics.BestellingAanBestellenToegevoegd}")
    private String topic;
    private BestellingEventReceiverService bestellingEventReceiverService;

    @Autowired
    public BestellingToegevoegdEventReceiver(BestellingEventReceiverService bestellingEventReceiverService) {
        this.bestellingEventReceiverService = bestellingEventReceiverService;
    }

    @Override
    @RabbitListener(
            queues = "#{topicBestellingToegevoegdQueue.name}",
            id = "BestellingAanBestellenToegevoegd",
            autoStartup = "false"
    )
    public void receive(BestellingEvent event) {
        bestellingEventReceiverService.handleBestellingToegevoegdEvent(event);
    }

    @Override
    public String getTopic() {
        return topic;
    }
}
