package com.kantilever.bffwebwinkel.domain.klant.amqp.receivers;

import com.kantilever.bffwebwinkel.domain.klant.amqp.events.KlantEvent;
import com.kantilever.bffwebwinkel.domain.klant.models.Klant;
import com.kantilever.bffwebwinkel.domain.klant.services.KlantService;
import com.kantilever.bffwebwinkel.domain.klant.services.KlantSoortService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * KlantEventReceiverService is the service that handles all events received. All receivers inject this class and let
 * it handle events.
 */
@Service
public class KlantEventReceiverService {
    private KlantService klantService;
    private KlantSoortService klantSoortService;

    @Autowired
    public KlantEventReceiverService(KlantService klantService,
                                     KlantSoortService klantSoortService) {
        this.klantService = klantService;
        this.klantSoortService = klantSoortService;
    }

    void handleKlantToegevoegdEvent(KlantEvent klantEvent) {
        var klantFromEvent = Klant.from(klantEvent, klantSoortService);
        klantService.save(klantFromEvent);
    }
}
