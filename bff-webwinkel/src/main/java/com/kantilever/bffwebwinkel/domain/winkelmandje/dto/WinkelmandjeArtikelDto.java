package com.kantilever.bffwebwinkel.domain.winkelmandje.dto;

import javax.validation.constraints.Positive;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class WinkelmandjeArtikelDto {
    @Positive
    private int artikelNummer;
    @Positive
    private int aantal;

}
