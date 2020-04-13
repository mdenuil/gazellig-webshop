package com.kantilever.bffwebwinkel.domain.bestelling.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kantilever.bffwebwinkel.util.Validation;
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
    @NotBlank
    private String straatnaam;
    @NotBlank
    private String huisnummer;
    @Pattern(regexp = Validation.POSTCODE_REGEX)
    private String postcode;
    @NotBlank
    private String woonplaats;
}
