package com.gazellig.amqpservice.implementations;


import com.gazellig.amqpservice.amqp.audit.receivers.AuditableEventReceiver;
import com.gazellig.amqpservice.amqp.audit.replay.AuditReplayEventReceiver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * ArtikelToegevoegEventReceiver receives events for {@link ArtikelEvent}.
 * <p>
 * This class implements {@link AuditableEventReceiver} and registers itself with the {@link AuditReplayEventReceiver}.
 */
@Component
public class ArtikelToegevoegdEventReceiver extends AuditableEventReceiver<ArtikelEvent> {
    private String topic;

    @Autowired
    public ArtikelToegevoegdEventReceiver() {
        this.topic = topic;
    }

    @Override

    public void receive(ArtikelEvent event) {
        return;
    }

    @Override
    public String getTopic() {
        return topic;
    }

}
