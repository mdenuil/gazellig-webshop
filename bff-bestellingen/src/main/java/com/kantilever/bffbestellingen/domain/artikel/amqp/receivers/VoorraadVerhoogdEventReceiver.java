package com.kantilever.bffbestellingen.domain.artikel.amqp.receivers;

import com.gazellig.amqpservice.amqp.audit.receivers.AuditableEventReceiver;
import com.kantilever.bffbestellingen.domain.artikel.services.ArtikelService;
import com.kantilever.bffbestellingen.domain.artikel.amqp.events.voorraad.VoorraadEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * ArtikelToegevoegEventReceiver received events for {@link VoorraadEvent} where the amount goes up.
 * <p>
 * This class implements AuditableEventReceiver and registers itself with the
 * {@link com.gazellig.amqpservice.amqp.audit.replay.AuditReplayEventReceiver}.
 */
@Component
public class VoorraadVerhoogdEventReceiver extends AuditableEventReceiver<VoorraadEvent> {
    @Value("${rabbitmq.topics.VoorraadVerhoogd}")
    private String topic;
    private ArtikelService artikelService;

    @Autowired
    public VoorraadVerhoogdEventReceiver(ArtikelService artikelService) {
        this.artikelService = artikelService;
    }

    @Override
    @RabbitListener(
            queues = "#{topicVoorraadVerhoogdQueue.name}",
            id = "VoorraadVerhoogd",
            autoStartup = "false"
    )
    public void receive(VoorraadEvent event) {
        artikelService.verhoogAantal(event);
    }

    @Override
    public String getTopic() {
        return topic;
    }

}
