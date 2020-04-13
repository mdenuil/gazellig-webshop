package com.kantilever.bffwebwinkel.domain.klant.amqp.receivers;

import com.gazellig.amqpservice.amqp.audit.receivers.AuditableEventReceiver;
import com.kantilever.bffwebwinkel.domain.klant.amqp.events.KlantEvent;
import com.kantilever.bffwebwinkel.domain.winkelmandje.services.WinkelmandjeService;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * KlantToegevoegdEventReceiver listens on the KlantToegevoegdQueue and receives KlantEvent objects.
 * <p>
 * This class extends {@link AuditableEventReceiver} and registers itself with the AuditLog receiver as a topic that
 * has to be received from the AuditLog on startup of the service.
 */
@Log4j2
@Component
public class KlantToegevoegdEventReceiver extends AuditableEventReceiver<KlantEvent> {
    @Value("${rabbitmq.topics.KlantToegevoegdEvent}")
    private String topic;

    private KlantEventReceiverService klantEventReceiverService;
    private WinkelmandjeService winkelmandjeService;

    @Autowired
    public KlantToegevoegdEventReceiver(KlantEventReceiverService klantEventReceiverService,
                                        WinkelmandjeService winkelmandjeService) {
        this.klantEventReceiverService = klantEventReceiverService;
        this.winkelmandjeService = winkelmandjeService;
    }

    @Override
    @RabbitListener(
            queues = "#{topicKlantToegevoegdEventQueue.name}",
            id = "KlantToegevoegd",
            autoStartup = "false"
    )
    public void receive(KlantEvent event) {
        klantEventReceiverService.handleKlantToegevoegdEvent(event);
        winkelmandjeService.saveNewWinkelmandje(event.getKlantNummer());
    }

    @Override
    public String getTopic() {
        return topic;
    }
}
