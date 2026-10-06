package domein;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.Year;

import static org.junit.jupiter.api.Assertions.*;

class SpelerTest {
    private static final String DEFAULT_GEBRUIKERSNAAM = "pieter";
    private static final int DEFAULT_GEBOORTEJAAR = 2007;


    @ParameterizedTest
    @ValueSource(strings = {" ", "piet"})
        //doing
    void maakSpeler_OngeldigeWaardesGebruikersnaam_werptException(String username) {
        assertThrows(IllegalArgumentException.class, () -> new Speler(username, DEFAULT_GEBOORTEJAAR));
    }

    @Test
    void maakSpeler_geldigeWaardes_maaktEenSpeler() { //doing
        Speler speler = new Speler(DEFAULT_GEBRUIKERSNAAM, DEFAULT_GEBOORTEJAAR);
        assertEquals(DEFAULT_GEBRUIKERSNAAM, speler.getGebruikersnaam());
        assertEquals(DEFAULT_GEBOORTEJAAR, speler.getGeboortejaar());

    }

    @Test
    void maakSpeler_JongerDanVijfJaar_werptException() {
        int huidigJaar = Year.now().getValue();

        assertThrows(IllegalArgumentException.class,
                () -> new Speler(DEFAULT_GEBRUIKERSNAAM, huidigJaar - 4));
    }

    @Test
    void maakSpeler_OuderDanNegenennegentigJaar_werptException() {
        int huidigJaar = Year.now().getValue();

        assertThrows(IllegalArgumentException.class,
                () -> new Speler(DEFAULT_GEBRUIKERSNAAM, huidigJaar - 100));
    }

    @Test
    void verhoogMislukteWorpen_verhoogtAantal() { //doing
        Speler speler = new Speler(DEFAULT_GEBRUIKERSNAAM, DEFAULT_GEBOORTEJAAR);

          speler.setMislukteWorpen();
          speler.setMislukteWorpen();

        assertEquals(2, speler.getMislukteWorpen());
    }

    @Test
    void getAantalGeslotenRijen_bijNieuweSpeler_IsNul() { //knowing
        Speler speler = new Speler(DEFAULT_GEBRUIKERSNAAM, DEFAULT_GEBOORTEJAAR);

        assertEquals(0, speler.getAantalGeslotenRijen());
    }

    @Test
    void berekenScore_mislukteWorpen_verlaagtScore() { //knowing
        Speler speler = new Speler(DEFAULT_GEBRUIKERSNAAM, DEFAULT_GEBOORTEJAAR);

        speler.voegKruisjeToe(Kleur.ROOD, 1);
        speler.voegKruisjeToe(Kleur.ROOD, 2);

        int scoreZonderMislukteWorpen = speler.getScoreblad().berekenScore();
        speler.setMislukteWorpen();
        int scoreMetMislukteWorpen = speler.getScoreblad().berekenScore();
        assertTrue(scoreMetMislukteWorpen < scoreZonderMislukteWorpen);
    }

    @ParameterizedTest
    @ValueSource(strings = {"pieter123", "gebruikersnaam", "langegebruikersnaam123"})
    void maakSpeler_GeldigeGebruikersnaam_maaktSpeler(String username) {
        Speler speler = new Speler(username, DEFAULT_GEBOORTEJAAR);
        assertEquals(username, speler.getGebruikersnaam());

    }

    @Test
    void maakSpeler_GeboortejaarVoorVijfjarige_maaktSpeler() {
        int jaar = Year.now().getValue() - 5;

        Speler speler = new Speler(DEFAULT_GEBRUIKERSNAAM, jaar);

        assertEquals(jaar, speler.getGeboortejaar());
    }

    @Test
    void maakSpeler_GeboortejaarVoorNegenennegentigjarige_maaktSpeler() {
        int jaar = Year.now().getValue() - 99;

        Speler speler = new Speler(DEFAULT_GEBRUIKERSNAAM, jaar);

        assertEquals(jaar, speler.getGeboortejaar());
    }

    @Test
    void toString_GeeftGebruikersnaam() {
        Speler speler = new Speler(DEFAULT_GEBRUIKERSNAAM, DEFAULT_GEBOORTEJAAR);

        assertEquals(DEFAULT_GEBRUIKERSNAAM, speler.toString());
    }


}
