package com.kantilever.pcswinkelen.winkelmandje.amqp.receivers;

import com.gazellig.amqpservice.amqp.audit.receivers.AuditableEventReceiver;
import com.kantilever.pcswinkelen.winkelmandje.WinkelmandjeService;
import com.kantilever.pcswinkelen.winkelmandje.amqp.events.KlantEvent;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Receiver that listens to WinkelmandjeAangemaaktEvent and then creates a new Winkelmandje based on
 * the incoming klantNummer.
 */
@Component
@Log4j2
public class KlantToegevoegdEventReceiver extends AuditableEventReceiver<KlantEvent> {

    @Value("${rabbitmq.topics.KlantToegevoegd}")
    private String topic;

    private WinkelmandjeService winkelmandjeService;

    @Autowired
    public KlantToegevoegdEventReceiver(WinkelmandjeService winkelmandjeService) {
        this.winkelmandjeService = winkelmandjeService;
    }

    @Override
    @RabbitListener(
        queues = "#{topicKlantToegevoegdQueue.name}",
        id = "topicKlantToegevoegdPcsWinkelen",
        autoStartup = "false"
    )
    public void receive(KlantEvent event) {
        log.info(String.format("Receiving KlantToegevoegdEvent: %s", event));

        winkelmandjeService.saveNewWinkelmandje(event);
    }

    @Override
    public String getTopic() {
        return topic;
    }
}
