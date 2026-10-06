package domein;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class KleurTest {

    @Test
    void enum_BevatAlleQwixxKleuren() {
        assertArrayEquals(
                new Kleur[]{Kleur.ROOD, Kleur.GEEL, Kleur.GROEN, Kleur.BLAUW, Kleur.WIT},
                Kleur.values()
        );
    }

    @Test
    void valueOf_GeeftCorrecteKleur() {
        assertEquals(Kleur.ROOD, Kleur.valueOf("ROOD"));
    }
}
