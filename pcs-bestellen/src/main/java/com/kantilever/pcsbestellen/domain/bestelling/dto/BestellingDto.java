package com.kantilever.pcsbestellen.domain.bestelling.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kantilever.pcsbestellen.domain.bestelling.models.BestelStatusType;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class BestellingDto {
    private String initialen;
    private String achternaam;
    private String email;

    private BestelStatusType status;

    private AdresDto afleverAdres;
    private AdresDto factuurAdres;

    private List<BesteldArtikelDto> artikelen;
}
