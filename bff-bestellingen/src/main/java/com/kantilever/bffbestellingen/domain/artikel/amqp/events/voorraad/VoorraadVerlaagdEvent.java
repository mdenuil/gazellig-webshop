package com.kantilever.bffbestellingen.domain.artikel.amqp.events.voorraad;

/**
 * VoorraadVerlaagdEvent exists solely because the ObjectMapper can only match a single named Type to a
 * class, and as such all Topics for a EventType have to have a unique class.
 */
public class VoorraadVerlaagdEvent extends VoorraadEvent { }
