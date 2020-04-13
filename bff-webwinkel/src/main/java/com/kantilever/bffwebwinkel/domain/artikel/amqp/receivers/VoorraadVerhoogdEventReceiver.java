package com.kantilever.bffwebwinkel.domain.artikel.amqp.receivers;

import com.gazellig.amqpservice.amqp.audit.receivers.AuditableEventReceiver;
import com.kantilever.bffwebwinkel.domain.artikel.services.ArtikelService;
import com.kantilever.bffwebwinkel.domain.artikel.amqp.events.VoorraadEvent;
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
    private ArtikelService artikelService;
    private String topic;

    @Autowired
    public VoorraadVerhoogdEventReceiver(ArtikelService artikelService,
                                         @Value("${rabbitmq.topics.VoorraadVerhoogd}") String topic) {
        this.artikelService = artikelService;
        this.topic = topic;
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
