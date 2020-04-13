package com.kantilever.bffwebwinkel.domain.bestelling.dto;

import javax.validation.constraints.Positive;
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
public class BesteldArtikelDto {
    @Positive
    private int aantal;
    @Positive
    private int artikelNummer;
}
