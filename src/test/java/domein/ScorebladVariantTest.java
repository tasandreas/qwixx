package domein;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ScorebladVariantTest {

    @Test
    void maakRijen_MaaktVierRijen() {
        ScorebladVariant scoreblad = new ScorebladVariant();

        assertEquals(4, scoreblad.getRijen().size());
    }

    @Test
    void maakRijen_RoodHeeftVariantVolgorde() {
        ScorebladVariant scoreblad = new ScorebladVariant();

        assertArrayEquals(
                new int[]{2, 6, 3, 7, 4, 8, 5, 9, 10, 11, 1, 12},
                scoreblad.getRij(Kleur.ROOD).getWaarden()
        );
    }

    @Test
    void maakRijen_BlauwHeeftVariantVolgorde() {
        ScorebladVariant scoreblad = new ScorebladVariant();

        assertArrayEquals(
                new int[]{12, 1, 11, 10, 9, 5, 8, 4, 7, 3, 6, 2},
                scoreblad.getRij(Kleur.BLAUW).getWaarden()
        );
    }
}
