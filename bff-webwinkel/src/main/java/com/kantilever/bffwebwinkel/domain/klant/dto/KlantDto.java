package com.kantilever.bffwebwinkel.domain.klant.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kantilever.bffwebwinkel.domain.klant.models.Klant;
import java.math.BigDecimal;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class KlantDto {
    @NotNull
    private Long klantNummer;
    @Email
    private String email;
    @NotBlank
    private String initialen;
    @NotBlank
    private String achternaam;
    private BigDecimal krediet;

    public static KlantDto from(Klant klant) {
        return KlantDto.builder()
                .klantNummer(klant.getKlantNummer())
                .email(klant.getEmail())
                .initialen(klant.getInitialen())
                .achternaam(klant.getAchternaam())
                .krediet(klant.getKrediet())
                .build();
    }
}
