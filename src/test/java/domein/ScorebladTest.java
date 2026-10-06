package domein;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ScorebladTest {

    private Scoreblad scoreblad;
    @BeforeEach
    void setUp() {
        scoreblad = new ScorebladBasis();
    }

    @Test
    void constructor_MaaktVierRijen() {

        assertEquals(4, scoreblad.getRijen().size());
    }

    @Test
    void getRij_GeeftCorrecteRij() {

        Rij rij = scoreblad.getRij(Kleur.ROOD);

        assertNotNull(rij);
        assertEquals(Kleur.ROOD, rij.getKleur());
    }

    @Test
    void verhoogMislukteWorpen_Werkt() {

        scoreblad.verhoogMislukteWorpen();

        assertEquals(1, scoreblad.getMislukteWorpen());
    }

    @Test
    void kruisAan_ViaScoreblad_Werkt() {

        scoreblad.kruisAan(Kleur.ROOD, 3);

        Rij rij = scoreblad.getRij(Kleur.ROOD);

        assertTrue(rij.getAangekruist().contains(3));
    }

    @Test
    void constructor_VariantMaaktVierRijen() {
        Scoreblad scoreblad = new ScorebladVariant();

        assertEquals(4, scoreblad.getRijen().size());
    }

    @Test
    void verhoogMislukteWorpen_meerdereKeren() {
        scoreblad.verhoogMislukteWorpen();
        scoreblad.verhoogMislukteWorpen();
        scoreblad.verhoogMislukteWorpen();

        assertEquals(3, scoreblad.getMislukteWorpen());
    }

    @Test
    void kruisAan_OngeldigeWaarde_werptException() {
        assertThrows(IllegalArgumentException.class, () -> scoreblad.kruisAan(99));
    }

    @Test
    void kruisAan_VerkeerdeKleur_werptException() {
        assertThrows(IllegalArgumentException.class, () -> scoreblad.kruisAan(null, 5));
    }

    @Test
    void berekenScore_zonderMislukteWorpen() {
        scoreblad.kruisAan(Kleur.ROOD, 1);
        scoreblad.kruisAan(Kleur.ROOD, 2);

        int score = scoreblad.berekenScore();

        assertTrue(score > 0);
    }

    @Test
    void berekenScore_metMislukteWorpen_verlaagtScore() {
        scoreblad.kruisAan(Kleur.ROOD, 1);
        scoreblad.kruisAan(Kleur.ROOD, 2);

        int scoreZonder = scoreblad.berekenScore();

        scoreblad.verhoogMislukteWorpen();

        int scoreMet = scoreblad.berekenScore();

        assertTrue(scoreMet < scoreZonder);
    }

}