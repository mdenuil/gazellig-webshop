package com.kantilever.bffwebwinkel.domain.winkelmandje.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class WinkelmandjeDto {
    @Valid
    private List<WinkelmandjeArtikelDto> artikelen;
}
