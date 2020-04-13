package com.kantilever.bffwebwinkel.domain.bestelling.amqp.jackson;

import com.kantilever.bffwebwinkel.domain.bestelling.amqp.events.BestellingEvent;

/**
 * BestellingAanBestellenToegevoegdEvent is simply here to let the ObjectMapper know about subtypes of BestellingEvent.
 * <p>
 * The identifier of a BestellingEvent is its topic and more than BestellingEvent can be bound to a topic. To circumvent this
 * we create subtypes of BestellingEvent and bind those to a specific topic.
 */
public class BestellingAanBestellenToegevoegdEvent extends BestellingEvent {
}
