package com.kantilever.pcsbestellen.domain.bestelling.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class AdresDto {
    private String straatnaam;
    private String huisnummer;
    private String postcode;
    private String woonplaats;
}
