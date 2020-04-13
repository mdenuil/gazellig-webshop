package com.gazellig.amqpservice.amqp.audit.handledevents;

import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * HandledEventService handles operations on {@link HandledEvent}.
 */
@Service
public class HandledEventService {
    private HandledEventRepository handledEventRepository;

    @Autowired
    public HandledEventService(HandledEventRepository handledEventRepository) {
        this.handledEventRepository = handledEventRepository;
    }

    public boolean isHandledEvent(UUID correlationId) {
        var replay = handledEventRepository.findById(correlationId.toString());
        return replay.isPresent();
    }

    public void saveEvent(UUID correlationId) {
        handledEventRepository.save(HandledEvent.builder()
                .correlationId(correlationId.toString())
                .build());
    }

    public boolean isExistingEvent(UUID correlationId) {
        return handledEventRepository.existsById(correlationId.toString());
    }
}
