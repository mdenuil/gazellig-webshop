package com.kantilever.dsklantbeheer.domain.klant.amqp.receivers;

import com.gazellig.amqpservice.amqp.audit.receivers.AuditableEventReceiver;
import com.kantilever.dsklantbeheer.domain.klant.amqp.events.KlantEvent;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * KlantGeregistreerdEventReceiver listens on the KlantGeregistreerdQueue and receives KlantEvent objects.
 * <p>
 * This class extends {@link AuditableEventReceiver} and registers itself with the AuditLog receiver as a topic that
 * has to be received from the AuditLog on startup of the service.
 */
@Log4j2
@Component
class KlantGeregistreerdEventReceiver extends AuditableEventReceiver<KlantEvent> {
    @Value("${rabbitmq.topics.KlantGeregistreerdEvent}")
    private String topic;
    private KlantEventReceiverService klantEventReceiverService;

    @Autowired
    public KlantGeregistreerdEventReceiver(KlantEventReceiverService klantEventReceiverService) {
        this.klantEventReceiverService = klantEventReceiverService;
    }

    @Override
    @RabbitListener(
            queues = "#{topicKlantGeregistreerdEventQueue.name}",
            id = "KlantGeregistreerd",
            autoStartup = "false"
    )
    public void receive(KlantEvent event) {
        klantEventReceiverService.handleKlantGeregistreerdEvent(event);
    }

    @Override
    public String getTopic() {
        return topic;
    }
}
