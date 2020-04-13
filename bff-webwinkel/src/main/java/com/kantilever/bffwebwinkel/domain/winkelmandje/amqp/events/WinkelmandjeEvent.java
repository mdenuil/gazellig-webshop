package com.kantilever.bffwebwinkel.domain.winkelmandje.amqp.events;

import com.gazellig.amqpservice.amqp.events.AuditableEvent;
import com.kantilever.bffwebwinkel.domain.winkelmandje.dto.WinkelmandjeArtikelDto;
import com.kantilever.bffwebwinkel.domain.winkelmandje.dto.WinkelmandjeDto;
import java.util.List;
import lombok.*;

/**
 * WinkelmandjeEvent represents the payload of an incoming request to create a WinkelmandjeAangepastEvent.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class WinkelmandjeEvent extends AuditableEvent {

    private Long klantNummer;
    private List<WinkelmandjeArtikelDto> artikelen;

    @Builder
    public WinkelmandjeEvent(String topic, Long klantNummer, List<WinkelmandjeArtikelDto> artikelen) {
        super(topic);
        this.klantNummer = klantNummer;
        this.artikelen = artikelen;
    }

    public static WinkelmandjeEvent from(WinkelmandjeDto winkelmandjeDto, Long klantNummer) {
        return WinkelmandjeEvent.builder()
                .klantNummer(klantNummer)
                .artikelen(winkelmandjeDto.getArtikelen())
                .build();
    }
}
