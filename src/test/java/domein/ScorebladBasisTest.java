package domein;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ScorebladBasisTest {

    @Test
    void maakRijen_MaaktVierRijenMetJuisteKleuren() {
        ScorebladBasis scoreblad = new ScorebladBasis();

        assertEquals(Kleur.ROOD, scoreblad.getRijen().get(0).getKleur());
        assertEquals(Kleur.GEEL, scoreblad.getRijen().get(1).getKleur());
        assertEquals(Kleur.GROEN, scoreblad.getRijen().get(2).getKleur());
        assertEquals(Kleur.BLAUW, scoreblad.getRijen().get(3).getKleur());
    }

    @Test
    void maakRijen_RoodLooptVanEenTotTwaalf() {
        ScorebladBasis scoreblad = new ScorebladBasis();

        assertArrayEquals(
                new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12},
                scoreblad.getRij(Kleur.ROOD).getWaarden()
        );
    }

    @Test
    void maakRijen_GroenLooptVanTwaalfTotEen() {
        ScorebladBasis scoreblad = new ScorebladBasis();

        assertArrayEquals(
                new int[]{12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1},
                scoreblad.getRij(Kleur.GROEN).getWaarden()
        );
    }
}
