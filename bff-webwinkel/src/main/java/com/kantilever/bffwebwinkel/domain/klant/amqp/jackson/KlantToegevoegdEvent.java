package com.kantilever.bffwebwinkel.domain.klant.amqp.jackson;

import com.kantilever.bffwebwinkel.domain.klant.amqp.events.KlantEvent;

/**
 * KlantToegevoegdEvent is simply here to let the ObjectMapper know about subtypes of KlantEvent.
 * <p>
 * The identifier of a KlantEvent is its topic and more than KlantEvent can be bound to a topic. To circumvent this
 * we create subtypes of KlantEvent and bind those to a specific topic.
 */
public class KlantToegevoegdEvent extends KlantEvent { }
