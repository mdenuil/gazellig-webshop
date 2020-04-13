package com.kantilever.bffbestellingen.domain.artikel.amqp.receivers;

import com.gazellig.amqpservice.amqp.audit.receivers.AuditableEventReceiver;
import com.kantilever.bffbestellingen.domain.artikel.amqp.events.voorraad.VoorraadEvent;
import com.kantilever.bffbestellingen.domain.artikel.services.ArtikelService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * VoorraadVerlaagdEventReceiver received events for {@link VoorraadEvent} where the amount goes down.
 * <p>
 * This class implements AuditableEventReceiver and registers itself with the
 * {@link com.gazellig.amqpservice.amqp.audit.replay.AuditReplayEventReceiver}.
 */
@Component
public class VoorraadVerlaagdEventReceiver extends AuditableEventReceiver<VoorraadEvent> {
    @Value("${rabbitmq.topics.VoorraadVerlaagd}")
    private String topic;
    private ArtikelService artikelService;

    @Autowired
    public VoorraadVerlaagdEventReceiver(ArtikelService artikelService) {
        this.artikelService = artikelService;
    }

    @Override
    @RabbitListener(
            queues = "#{topicVoorraadVerlaagdQueue.name}",
            id = "VoorraadVerlaagd",
            autoStartup = "false"
    )
    public void receive(VoorraadEvent event) {
        artikelService.verlaagAantal(event);
    }

    @Override
    public String getTopic() {
        return topic;
    }
}
