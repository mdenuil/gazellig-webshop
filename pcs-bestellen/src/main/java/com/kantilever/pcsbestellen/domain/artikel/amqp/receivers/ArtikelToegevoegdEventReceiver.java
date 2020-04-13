package com.kantilever.pcsbestellen.domain.artikel.amqp.receivers;


import com.gazellig.amqpservice.amqp.audit.receivers.AuditableEventReceiver;
import com.kantilever.pcsbestellen.domain.artikel.ArtikelService;
import com.kantilever.pcsbestellen.domain.artikel.amqp.events.ArtikelEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * ArtikelToegevoegEventReceiver receives events for {@link ArtikelEvent}.
 * <p>
 * This class implements AuditableEventReceiver and registers itself with the {@link com.gazellig.amqpservice.amqp.audit.replay.AuditReplayEventReceiver}.

 */
@Component
public class ArtikelToegevoegdEventReceiver extends AuditableEventReceiver<ArtikelEvent> {
    private ArtikelService artikelService;
    private String topic;

    @Autowired
    public ArtikelToegevoegdEventReceiver(ArtikelService artikelService,
                                          @Value("${rabbitmq.topics.ArtikelAanCatalogusToegevoegd}") String topic) {
        this.artikelService = artikelService;
        this.topic = topic;
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
