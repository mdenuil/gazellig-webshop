package com.kantilever.pcsbestellen.domain.bestelling.models;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Embeddable
public class KlantGegevens {
    @Column(name = "INITIALEN")
    private String initialen;

    @Column(name = "ACHTERNAAM")
    private String achternaam;

    @Column(name = "EMAIL")
    private String email;
}
