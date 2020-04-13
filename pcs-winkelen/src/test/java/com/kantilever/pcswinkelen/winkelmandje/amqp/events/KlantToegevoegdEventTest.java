package com.kantilever.pcswinkelen.winkelmandje.amqp.events;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class KlantToegevoegdEventTest {

    @Test
    @DisplayName("KlantToegevoegdEvent is subtype of KlantToegevoegd")
    void checkSuperType() {
        KlantEvent klantToegevoegdEvent = new KlantToegevoegdEvent();

        assertThat(klantToegevoegdEvent).isInstanceOf(KlantEvent.class);
    }
}