package com.kantilever.bffwebwinkel.domain.artikel.services;

import com.gazellig.amqpservice.amqp.audit.replay.AuditReplayStartListener;
import com.kantilever.bffwebwinkel.domain.artikel.models.Artikel;
import com.kantilever.bffwebwinkel.domain.artikel.repositories.ArtikelRepository;
import com.kantilever.bffwebwinkel.domain.artikel.services.ArtikelService;
import com.kantilever.bffwebwinkel.util.ObjectBuilders;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@SpringBootTest
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class ArtikelServiceFilterTest {

    private List<Artikel> artikelen;

    @Autowired
    private ArtikelService artikelService;

    @MockBean
    private AuditReplayStartListener auditReplayStartListener;

    @MockBean
    private ArtikelRepository artikelRepository;

    @BeforeEach
    void init() {
        artikelen = ObjectBuilders.getDefaultListofThreeArtikelen();
    }

    @Test
    @DisplayName("Returns list whereas hits on field 'naam' are first in the list and " +
                 "hits on field 'beschrijving' are second in the list")
    void canReturnAList_OrderedByNaamFirstAndThenBeschrijving_WithGivenParameter() {
        var filterName = "Alaaf";

        when(artikelRepository.findAllByNaamContainingIgnoreCase(filterName))
                .thenReturn(List.of(artikelen.get(1)));

        when(artikelRepository.findAllByBeschrijvingContainingIgnoreCase(filterName))
                .thenReturn(List.of(artikelen.get(0), artikelen.get(2)));

        var newArtikelen = artikelService.getArtikelenFilteredByNameAndDescription(filterName);

        assertThat(newArtikelen.size()).isEqualTo(3);
        assertThat(artikelen.get(0).getArtikelNummer()).isEqualTo(1);
    }

    @Test
    @DisplayName("ArtikelService makes sure that duplicated Artikelen are not added to the list")
    void artikelServiceDoesNotAddTheSameArtikelTwice() {
        var filterName = "f";

        when(artikelRepository.findAllByNaamContainingIgnoreCase(filterName))
                .thenReturn(artikelen);

        when(artikelRepository.findAllByBeschrijvingContainingIgnoreCase(filterName))
                .thenReturn(artikelen);

        var newArtikelen = artikelService.getArtikelenFilteredByNameAndDescription(filterName);

        // verify findAllBy operations are executed in the correct order:
        var inOrder = Mockito.inOrder(artikelRepository);
        inOrder.verify(artikelRepository, times(1)).findAllByNaamContainingIgnoreCase(any());
        inOrder.verify(artikelRepository, times(1)).findAllByBeschrijvingContainingIgnoreCase(any());

        // assert that artikellist has the correct size
        assertThat(newArtikelen.size()).isEqualTo(3);
    }
}