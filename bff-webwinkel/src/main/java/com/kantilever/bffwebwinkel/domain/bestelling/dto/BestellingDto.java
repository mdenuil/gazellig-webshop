package com.kantilever.bffwebwinkel.domain.bestelling.dto;

import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kantilever.bffwebwinkel.domain.bestelling.models.BestelStatusType;
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
    @Valid
    private AdresDto afleverAdres;
    @Valid
    @NotNull
    private AdresDto factuurAdres;
    @NotEmpty
    @Valid
    private List<BesteldArtikelDto> artikelen;
}
