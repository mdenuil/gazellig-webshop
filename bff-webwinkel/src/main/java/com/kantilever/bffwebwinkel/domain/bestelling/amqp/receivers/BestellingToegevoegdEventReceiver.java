package com.kantilever.bffwebwinkel.domain.bestelling.amqp.receivers;

import com.gazellig.amqpservice.amqp.audit.receivers.AuditableEventReceiver;
import com.kantilever.bffwebwinkel.domain.bestelling.amqp.events.BestellingEvent;
import com.kantilever.bffwebwinkel.domain.bestelling.services.BestellingService;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * BestellingToegevoegdEventReceiver listens on the KlantToegevoegdQueue and receives BestellingEvent objects.
 * <p>
 * This class extends {@link AuditableEventReceiver} and registers itself with the AuditLog receiver as a topic that
 * has to be received from the AuditLog on startup of the service.
 */
@Log4j2
@Component
public class BestellingToegevoegdEventReceiver extends AuditableEventReceiver<BestellingEvent> {
    private String topic;
    private BestellingService bestellingService;

    @Autowired
    public BestellingToegevoegdEventReceiver(
            @Value("${rabbitmq.topics.BestellingAanBestellenToegevoegd}") String topic,
            BestellingService bestellingService) {
        this.topic = topic;
        this.bestellingService = bestellingService;
    }

    @Override
    @RabbitListener(
            queues = "#{topicBestellingToegevoegdQueue.name}",
            id = "BestellingAanBestellenToegevoegd",
            autoStartup = "false"
    )
    public void receive(BestellingEvent event) {
        bestellingService.saveBestelling(event);
    }

    @Override
    public String getTopic() {
        return topic;
    }
}
