package com.kantilever.pcswinkelen.winkelmandje;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kantilever.pcswinkelen.winkelmandje.amqp.events.WinkelmandjeEvent;
import com.kantilever.pcswinkelen.winkelmandje.amqp.senders.WinkelmandjeAanWinkelenToegevoegdEventSender;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class WinkelmandjeEventService {

    private WinkelmandjeAanWinkelenToegevoegdEventSender winkelmandjeToegevoegdSender;

    @Autowired
    public WinkelmandjeEventService(WinkelmandjeAanWinkelenToegevoegdEventSender winkelmandjeToegevoegdSender) {
        this.winkelmandjeToegevoegdSender = winkelmandjeToegevoegdSender;
    }

    public void sendWinkelmandjeToegevoegdEvent(WinkelmandjeEvent winkelmandjeEvent) {
        try {
            winkelmandjeToegevoegdSender.send(winkelmandjeEvent);
        } catch (JsonProcessingException e) {
            log.error("Error during processing WinkelmandjeAanWinkelenToegevoegdEvent", e);
        }
    }
}


