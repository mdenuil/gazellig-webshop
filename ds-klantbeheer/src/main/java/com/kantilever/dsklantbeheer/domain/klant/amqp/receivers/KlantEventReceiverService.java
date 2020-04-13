package com.kantilever.dsklantbeheer.domain.klant.amqp.receivers;

import com.kantilever.dsklantbeheer.domain.klant.amqp.events.KlantEvent;
import com.kantilever.dsklantbeheer.domain.klant.amqp.sender.KlantEventSenderService;
import com.kantilever.dsklantbeheer.domain.klant.models.Klant;
import com.kantilever.dsklantbeheer.domain.klant.services.KlantService;
import com.kantilever.dsklantbeheer.domain.klant.services.KlantSoortService;
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
    private KlantEventSenderService klantEventSenderService;

    @Autowired
    public KlantEventReceiverService(KlantService klantService,
                                     KlantSoortService klantSoortService,
                                     KlantEventSenderService klantEventSenderService) {
        this.klantService = klantService;
        this.klantSoortService = klantSoortService;
        this.klantEventSenderService = klantEventSenderService;
    }

    void handleKlantGeregistreerdEvent(KlantEvent klantEvent) {
        var klantFromEvent = Klant.from(klantEvent, klantSoortService);
        var klantEntity = klantService.addNewKlant(klantFromEvent);
        klantEventSenderService.sendKlantToegevoegdEvent(klantEntity);
    }
}
