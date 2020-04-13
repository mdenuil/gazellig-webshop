package com.kantilever.bffwebwinkel.domain.artikel.amqp.jackson;

import com.kantilever.bffwebwinkel.domain.artikel.amqp.events.VoorraadEvent;

/**
 * VoorraadVerhoogdEvent exists solely because the ObjectMapper can only match a single named Type to a
 * class, and as such all Topics for a EventType have to have a unique class.
 */
class VoorraadVerhoogdEvent extends VoorraadEvent { }
