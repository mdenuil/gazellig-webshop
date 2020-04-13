package com.kantilever.bffwebwinkel.domain.artikel.repositories;

import com.kantilever.bffwebwinkel.domain.artikel.models.Artikel;
import com.kantilever.bffwebwinkel.domain.artikel.repositories.ArtikelRepository;
import com.kantilever.bffwebwinkel.util.ObjectBuilders;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
class ArtikelRepositoryTest {

    private List<Artikel> artikelen;

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private ArtikelRepository artikelRepository;

    @BeforeEach
    void init() {
        artikelen = ObjectBuilders.getDefaultListofThreeArtikelen();
    }

    @Test
    @DisplayName("All Artikelen that contains the value 'Alaaf' as field 'naam'" +
                 " are returned as list from the ArtikelRepository")
    void allArtikelenContainingGivenValue_AsNaam_AreRetrievedFromRepository() {
        var containingValue = "Alaaf";
        artikelen.forEach(testEntityManager::persistAndFlush);

        List<Artikel> artikelen = artikelRepository.findAllByNaamContainingIgnoreCase(containingValue);

        assertThat(artikelen.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("All Artikelen that contains the value 'Alaaf' as field 'beschrijving'" +
        " are returned as list from the ArtikelRepository")
    void allArtikelenContainingGivenValue_AsBeschrijving_AreRetrievedFromRepository() {
        var containingValue = "Alaaf";
        artikelen.forEach(testEntityManager::persistAndFlush);

        List<Artikel> artikelen = artikelRepository.findAllByBeschrijvingContainingIgnoreCase(containingValue);

        assertThat(artikelen.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("ArtikelRepository ignores Upper/Lowercase on the field 'naam'")
    void repositoryIsNotCaseSensitive_OnFieldNaam() {
        var containingValue = "ignoreCase";

        // set artikelNaam to uppercase:
        artikelen.get(1).setNaam(containingValue.toUpperCase());

        artikelen.forEach(testEntityManager::persistAndFlush);

        assertThat(artikelRepository.findAllByNaamContainingIgnoreCase(containingValue).size())
                .isEqualTo(1);
    }

    @Test
    @DisplayName("ArtikelRepository ignores Upper/Lowercase on the field 'naam'")
    void repositoryIsNotCaseSensitive_OnFieldBeschrijving() {
        var containingValue = "ignoreCase";

        // set artikelBeschrijving to uppercase:
        artikelen.get(1).setBeschrijving(containingValue.toUpperCase());

        artikelen.forEach(testEntityManager::persistAndFlush);

        assertThat(artikelRepository.findAllByBeschrijvingContainingIgnoreCase(containingValue).size())
                .isEqualTo(1);
    }
}