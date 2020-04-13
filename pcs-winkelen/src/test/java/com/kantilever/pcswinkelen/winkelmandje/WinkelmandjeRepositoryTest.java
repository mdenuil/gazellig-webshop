package com.kantilever.pcswinkelen.winkelmandje;

import com.kantilever.pcswinkelen.winkelmandje.models.BesteldArtikel;
import com.kantilever.pcswinkelen.winkelmandje.models.Winkelmandje;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class WinkelmandjeRepositoryTest {

    private Winkelmandje winkelmandje1, winkelmandje2;

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private WinkelmandjeRepository winkelmandjeRepository;

    @BeforeEach
    void init() {
        winkelmandje1 = Winkelmandje.builder()
                    .klantNummer(10L)
                    .artikelen(List.of(
                            new BesteldArtikel(1, 1),
                            new BesteldArtikel(2, 2),
                            new BesteldArtikel(3, 3)
                    ))
                    .build();

        winkelmandje2 = Winkelmandje.builder()
                .klantNummer(13L)
                .artikelen(List.of(
                        new BesteldArtikel(1, 1),
                        new BesteldArtikel(2, 2),
                        new BesteldArtikel(3, 3),
                        new BesteldArtikel(55, 3)
                ))
                .build();
    }

    @Test
    @DisplayName("All Winkelmandjes can be saved and retrieved from the WinkelmandjeRepository")
    void retrieveAllSavedWinkelmandjes() {
        testEntityManager.persistAndGetId(winkelmandje1);
        testEntityManager.persistAndGetId(winkelmandje2);

        List<Winkelmandje> winkelmandjes = (List<Winkelmandje>) winkelmandjeRepository.findAll();

        assertThat(winkelmandjes.size()).isEqualTo(2);
        assertThat(winkelmandjes.get(0)).isEqualTo(winkelmandje1);
        assertThat(winkelmandjes.get(1).getKlantNummer()).isEqualTo(winkelmandje2.getKlantNummer());
    }

    @Test
    @DisplayName("A Winkelmandje can be updated via WinkelmandjeRepository and retrieved via the ID")
    void updateWinkelmandje() {
        List<BesteldArtikel> artikelen = new ArrayList<>();
        artikelen.add(new BesteldArtikel(1, 1));
        artikelen.add(new BesteldArtikel(2, 2));
        artikelen.add(new BesteldArtikel(3, 3));
        artikelen.add(new BesteldArtikel(55, 3));
        artikelen.add(new BesteldArtikel(123, 6));

        testEntityManager.persistAndFlush(winkelmandje1);
        testEntityManager.persistAndFlush(winkelmandje2);

        var expectedSize = winkelmandje2.getArtikelen().size() + 1;

        var winkelmandje = winkelmandjeRepository.findById(13L);
        winkelmandje.orElseThrow().setArtikelen(artikelen);

        Winkelmandje w = winkelmandjeRepository.save(winkelmandje.orElseThrow());

        assertThat(w.getArtikelen().size()).isEqualTo(expectedSize);
    }
}