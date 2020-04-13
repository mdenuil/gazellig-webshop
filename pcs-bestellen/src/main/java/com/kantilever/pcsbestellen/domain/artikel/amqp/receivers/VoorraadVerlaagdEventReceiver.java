package com.kantilever.pcsbestellen.domain.artikel.amqp.receivers;

import com.gazellig.amqpservice.amqp.audit.receivers.AuditableEventReceiver;
import com.kantilever.pcsbestellen.domain.artikel.ArtikelService;
import com.kantilever.pcsbestellen.domain.artikel.amqp.events.VoorraadEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * VoorraadVerlaagdEventReceiver received events for {@link VoorraadEvent} where the amount goes down.
 * <p>
 * This class implements AuditableEventReceiver and registers itself with the {@link com.gazellig.amqpservice.amqp.audit.replay.AuditReplayEventReceiver}.
 */
@Component
public class VoorraadVerlaagdEventReceiver extends AuditableEventReceiver<VoorraadEvent> {
    private ArtikelService artikelService;
    private String topic;

    @Autowired
    public VoorraadVerlaagdEventReceiver(ArtikelService artikelService,
                                         @Value("${rabbitmq.topics.VoorraadVerlaagd}") String topic) {
        this.artikelService = artikelService;
        this.topic = topic;
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
