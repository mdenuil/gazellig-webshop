package com.kantilever.bffwebwinkel.domain.winkelmandje.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kantilever.bffwebwinkel.domain.winkelmandje.amqp.events.WinkelmandjeEvent;
import com.kantilever.bffwebwinkel.domain.winkelmandje.amqp.senders.WinkelmandjeAangepastEventSender;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class WinkelmandjeEventService {

    private WinkelmandjeAangepastEventSender winkelmandjeAangepastEventSender;

    @Autowired
    public WinkelmandjeEventService(WinkelmandjeAangepastEventSender winkelmandjeAangepastEventSender) {
        this.winkelmandjeAangepastEventSender = winkelmandjeAangepastEventSender;
    }

    public void sendWinkelmandjeAangepastEvent(WinkelmandjeEvent winkelmandjeEvent) {
        try {
            winkelmandjeAangepastEventSender.send(winkelmandjeEvent);
        } catch (JsonProcessingException e) {
            log.error("Error during processing WinkelmandjeAangepastEvent", e);
        }
    }
}
