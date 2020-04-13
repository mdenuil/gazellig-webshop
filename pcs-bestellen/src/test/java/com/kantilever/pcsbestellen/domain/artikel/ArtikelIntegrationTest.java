package com.kantilever.pcsbestellen.domain.artikel;

import com.gazellig.amqpservice.amqp.audit.replay.AuditReplayStartListener;
import com.kantilever.pcsbestellen.domain.artikel.amqp.events.ArtikelEvent;
import com.kantilever.pcsbestellen.domain.artikel.amqp.events.VoorraadEvent;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class ArtikelIntegrationTest {
    @MockBean
    private AuditReplayStartListener auditReplayStartListener;
    @Autowired
    private ArtikelController artikelController;
    @Autowired
    private ArtikelRepository artikelRepository;
    @Autowired
    private ArtikelService artikelService;
    private Artikel artikelOne;
    private Artikel artikelTwo;

    @Test
    void contextLoads() {
        assertThat(auditReplayStartListener).isNotNull();
        assertThat(artikelController).isNotNull();
        assertThat(artikelRepository).isNotNull();
        assertThat(artikelService).isNotNull();
    }

    @BeforeEach
    void init() {
        artikelOne = Artikel.builder()
                .artikelNummer(1)
                .aantal(1)
                .afbeeldingUrl("afbeelding.png")
                .beschrijving("Artikel")
                .naam("Artikel")
                .categorieen(List.of("Type"))
                .leverancier("Leverancier")
                .leverancierCode("L")
                .leverbaarTot(LocalDateTime.now())
                .leverbaarVanaf(LocalDateTime.now())
                .prijs(BigDecimal.valueOf(10))
                .build();

        artikelTwo = Artikel.builder()
                .artikelNummer(2)
                .aantal(100)
                .afbeeldingUrl("afbeelding.png")
                .beschrijving("Artikel")
                .naam("Artikel")
                .categorieen(List.of("Type"))
                .leverancier("Leverancier")
                .leverancierCode("L")
                .leverbaarTot(null)
                .leverbaarVanaf(LocalDateTime.now())
                .prijs(BigDecimal.valueOf(10))
                .build();
    }

    @Test
    @DisplayName("New artikelen get added to the database")
    void newArtikelen_getSavedToDatabase() {
        // Arrange
        artikelRepository.save(artikelOne);
        // Act
        Optional<Artikel> artikel = artikelRepository.findById(1);
        // Assert
        assertThat(artikel.isPresent()).isTrue();
        assertThat(artikel.get().equals(artikelOne)).isTrue();
    }

    @Test
    @DisplayName("Aantal is correctly increased and persisted based on VoorraadEvent values")
    void verhoogAantal_increasesAmount_byCorrectAmount() {
        // Arrange
        artikelRepository.save(artikelOne);
        var voorraadDto = VoorraadEvent.builder()
                .aantal(10)
                .nieuweVoorraad(11)
                .artikelNummer(1)
                .build();
        // Act
        artikelService.verhoogAantal(voorraadDto);
        // Assert
        Optional<Artikel> artikel = artikelRepository.findById(1);
        assertThat(artikel.isPresent()).isTrue();
        assertThat(artikel.get().getAantal()).isEqualTo(11);
    }

    @Test
    @DisplayName("Aantal is correctly decreased and persisted based on VoorraadEvent values")
    void verhoogAantal_decreasesAmount_byCorrectAmount() {
        // Arrange
        artikelRepository.save(artikelTwo);
        var voorraadDto = VoorraadEvent.builder()
                .aantal(10)
                .nieuweVoorraad(90)
                .artikelNummer(2)
                .build();
        // Act
        artikelService.verlaagAantal(voorraadDto);
        // Assert
        Optional<Artikel> artikel = artikelRepository.findById(2);
        assertThat(artikel.isPresent()).isTrue();
        assertThat(artikel.get().getAantal()).isEqualTo(90);
    }

    @Test
    @DisplayName("All artikelen stored in the database get return from getAll endpoint")
    void getAllArtikelenEndpoint_returnsAllArtikelen() {
        // Arrange
        artikelRepository.save(artikelOne);
        artikelRepository.save(artikelTwo);
        // Act
        List<Artikel> artikelenFromDatabase = artikelController.getAll();
        // Assert
        assertThat(artikelenFromDatabase.size()).isEqualTo(2);
        assertThat(artikelenFromDatabase.contains(artikelOne)).isTrue();
        assertThat(artikelenFromDatabase.contains(artikelTwo)).isTrue();
    }

    @Test
    @DisplayName("Add artikel sends correct event")
    void addArtikel_correctlyAddsArtikel_fromEvent() {
        // Arrange
        var artikelToegevoegdEvent = ArtikelEvent.builder()
                .topic("topic")
                .afbeeldingUrl("afbeeldingUrl")
                .artikelNummer(1)
                .beschrijving("beschrijving")
                .naam("naam")
                .categorieen(List.of("cat1", "cat2"))
                .prijs(100L)
                .leverancier("leverancier")
                .leverancierCode("l")
                .leverbaarTot(null)
                .leverbaarVanaf("2020-01-11T17:51:20.123")
                .build();
        // Act
        artikelService.addArtikel(artikelToegevoegdEvent);
        Optional<Artikel> optionalArtikel = artikelRepository.findById(1);
        // Assert
        assertThat(optionalArtikel.isPresent()).isTrue();
        Artikel artikel = optionalArtikel.get();
        assertThat(artikel.getAantal()).isEqualTo(0);
        assertThat(artikel.getAfbeeldingUrl()).isEqualTo("afbeeldingUrl");
        assertThat(artikel.getBeschrijving()).isEqualTo("beschrijving");
        assertThat(artikel.getNaam()).isEqualTo("naam");
        assertThat(artikel.getLeverancierCode()).isEqualTo("l");
        assertThat(artikel.getLeverancier()).isEqualTo("leverancier");
        assertThat(artikel.getPrijs()).isEqualTo(BigDecimal.valueOf(121.00).setScale(2, RoundingMode.CEILING));
        assertThat(artikel.getLeverbaarVanaf().getMonthValue()).isEqualTo(1);
        assertThat(artikel.getLeverbaarVanaf().getDayOfMonth()).isEqualTo(11);
    }
}
