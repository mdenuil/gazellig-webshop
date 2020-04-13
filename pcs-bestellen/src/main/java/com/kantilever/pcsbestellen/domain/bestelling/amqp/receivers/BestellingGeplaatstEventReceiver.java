package com.kantilever.pcsbestellen.domain.bestelling.amqp.receivers;

import com.gazellig.amqpservice.amqp.audit.receivers.AuditableEventReceiver;
import com.kantilever.pcsbestellen.domain.bestelling.amqp.events.BestellingEvent;
import com.kantilever.pcsbestellen.domain.bestelling.services.BestellingProcessorService;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * BestellingGeplaatsEventReceiver receives events on the ${rabbitmq.topics.BestellingGeplaatstEvent}
 * topic. Received events have their Bestelling body saved as a new event. This receiver will also check for the
 * Bestelling wether it is ready to be picked or not.
 *
 * This class extends {@link AuditableEventReceiver} to ensure the Auditlog is read before initializing the listener.
 *
 */
@Component
@Log4j2
public class BestellingGeplaatstEventReceiver extends AuditableEventReceiver<BestellingEvent> {
    private String topic;
    private BestellingProcessorService bestellingProcessorService;

    @Autowired
    public BestellingGeplaatstEventReceiver(@Value("${rabbitmq.topics.BestellingGeplaatstEvent}") String topic,
                                            BestellingProcessorService bestellingProcessorService) {
        this.topic = topic;
        this.bestellingProcessorService = bestellingProcessorService;
    }

    @Override
    @RabbitListener(
            queues = "#{topicBestellingGeplaatstQueue.name}",
            id = "topicBestellingGeplaatst",
            autoStartup = "false"
    )
    public void receive(BestellingEvent event) {
        log.info("Receiving BestellingGeplaatstEvent: " + event);

        bestellingProcessorService.handleBestellingEvent(event);
    }

    @Override
    public String getTopic() {
        return topic;
    }
}
