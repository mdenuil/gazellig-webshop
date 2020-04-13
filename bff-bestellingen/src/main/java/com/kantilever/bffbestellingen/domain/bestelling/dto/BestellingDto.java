package com.kantilever.bffbestellingen.domain.bestelling.dto;

import java.util.List;
import javax.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kantilever.bffbestellingen.domain.bestelling.models.BestelStatusType;
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
    @NotBlank
    private String initialen;
    @NotBlank
    private String achternaam;
    @Email
    private String email;
    private BestelStatusType status;
    @NotNull
    private AdresDto afleverAdres;
    @NotNull
    private AdresDto factuurAdres;
    @NotEmpty
    private List<BesteldArtikelDto> artikelen;
}
