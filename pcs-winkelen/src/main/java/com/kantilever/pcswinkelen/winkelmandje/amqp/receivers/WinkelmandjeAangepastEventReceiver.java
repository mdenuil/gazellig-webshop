package com.kantilever.pcswinkelen.winkelmandje.amqp.receivers;

import com.gazellig.amqpservice.amqp.audit.receivers.AuditableEventReceiver;
import com.kantilever.pcswinkelen.winkelmandje.WinkelmandjeService;
import com.kantilever.pcswinkelen.winkelmandje.amqp.events.WinkelmandjeEvent;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Receiver that listens to WinkelmandjeAangepastEvent and then updates the existing Winkelmandje based on
 * the incoming klantNummer.
 */
@Component
@Log4j2
public class WinkelmandjeAangepastEventReceiver extends AuditableEventReceiver<WinkelmandjeEvent> {

    @Value("${rabbitmq.topics.WinkelmandjeAangepast}")
    private String topic;

    private WinkelmandjeService winkelmandjeService;

    @Autowired
    public WinkelmandjeAangepastEventReceiver(WinkelmandjeService winkelmandjeService) {
        this.winkelmandjeService = winkelmandjeService;
    }

    @Override
    @RabbitListener(
            queues = "#{topicWinkelmandjeAangepastQueue.name}",
            id = "topicWinkelmandjeAangepastPcsWinkelen",
            autoStartup = "false"
    )
    public void receive(WinkelmandjeEvent event) {
        log.info(String.format("Receiving WinkelmandjeAangepastEvent: %s", event));

        winkelmandjeService.updateExistingWinkelmandje(event);
    }

    @Override
    public String getTopic() {
        return topic;
    }
}
