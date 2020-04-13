package com.kantilever.bffbestellingen.domain.artikel.amqp.receivers;


import com.gazellig.amqpservice.amqp.audit.receivers.AuditableEventReceiver;
import com.kantilever.bffbestellingen.domain.artikel.services.ArtikelService;
import com.kantilever.bffbestellingen.domain.artikel.amqp.events.artikel.ArtikelEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * ArtikelToegevoegEventReceiver receives events for {@link ArtikelEvent}.
 * <p>
 * This class implements AuditableEventReceiver and registers itself with the
 * {@link com.gazellig.amqpservice.amqp.audit.replay.AuditReplayEventReceiver}.
 */
@Component
public class ArtikelToegevoegdEventReceiver extends AuditableEventReceiver<ArtikelEvent> {
    @Value("${rabbitmq.topics.ArtikelAanCatalogusToegevoegd}")
    private String topic;
    private ArtikelService artikelService;

    @Autowired
    public ArtikelToegevoegdEventReceiver(ArtikelService artikelService) {
        this.artikelService = artikelService;
    }

    @Override
    public int getPriority() {
        return 1;
    }

    @Override
    @RabbitListener(
            queues = "#{topicArtikelToegevoegdQueue.name}",
            id = "ArtikelAanCatalogusToegevoegd",
            autoStartup = "false"
    )
    public void receive(ArtikelEvent event) {
        artikelService.addArtikel(event);
    }

    @Override
    public String getTopic() {
        return topic;
    }

}
