package com.kantilever.bffwebwinkel.domain.winkelmandje.amqp.receivers;

import com.gazellig.amqpservice.amqp.audit.receivers.AuditableEventReceiver;
import com.kantilever.bffwebwinkel.domain.winkelmandje.amqp.events.WinkelmandjeEvent;
import com.kantilever.bffwebwinkel.domain.winkelmandje.services.WinkelmandjeService;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Receiver that listens to WinkelmandjeAanWinkelenToegevoegd and then updates the existing Winkelmandje based on
 * the incoming klantNummer.
 */
@Component
@Log4j2
public class WinkelmandjeAanWinkelenToegevoegdEventReceiver extends AuditableEventReceiver<WinkelmandjeEvent> {

    @Value("${rabbitmq.topics.WinkelmandjeAanWinkelenToegevoegd}")
    private String topic;

    private WinkelmandjeService winkelmandjeService;

    @Autowired
    public WinkelmandjeAanWinkelenToegevoegdEventReceiver(WinkelmandjeService winkelmandjeService) {
        this.winkelmandjeService = winkelmandjeService;
    }

    @Override
    @RabbitListener(
            queues = "#{topicWinkelmandjeAanWinkelenToegevoegdQueue.name}",
            id = "topicWinkelmandjeAanWinkelenToegevoegdBffWebwinkel",
            autoStartup = "false"
    )
    public void receive(WinkelmandjeEvent event) {
        log.info(String.format("Receiving WinkelmandjeAangepastEvent: %s", event));

        winkelmandjeService.updateWinkelmandje(event);
    }

    @Override
    public String getTopic() {
        return topic;
    }
}
