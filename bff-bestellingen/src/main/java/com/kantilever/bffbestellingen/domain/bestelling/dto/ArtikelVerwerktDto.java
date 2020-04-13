package com.kantilever.bffbestellingen.domain.bestelling.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
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
public class ArtikelVerwerktDto {
    @NotNull
    @Positive
    private long bestelNummer;
    @NotNull
    @Positive
    private int artikelNummer;
}
