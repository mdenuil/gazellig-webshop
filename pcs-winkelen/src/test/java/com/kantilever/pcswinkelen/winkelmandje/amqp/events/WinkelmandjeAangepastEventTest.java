package com.kantilever.pcswinkelen.winkelmandje.amqp.events;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class WinkelmandjeAangepastEventTest {

    @Test
    @DisplayName("WinkelmandjeAangepastEvent is subtype of WinkelmandjeEvent")
    void checkSuperType() {
        WinkelmandjeEvent klantToegevoegdEvent = new WinkelmandjeAangepastEvent();

        assertThat(klantToegevoegdEvent).isInstanceOf(WinkelmandjeEvent.class);
    }
}