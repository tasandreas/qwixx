package domein;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ScorebladRandomTest {

    @Test
    void maakRijen_MaaktVierRijen() {
        ScorebladRandom scoreblad = new ScorebladRandom();

        assertEquals(4, scoreblad.getRijen().size());
    }

    @Test
    void elkeRij_BevatElkeWaardeVanEenTotTwaalfExactEenKeer() {
        ScorebladRandom scoreblad = new ScorebladRandom();
        int[] verwacht = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};

        for (Rij rij : scoreblad.getRijen()) {
            int[] gesorteerd = Arrays.copyOf(rij.getWaarden(), rij.getWaarden().length);
            Arrays.sort(gesorteerd);
            assertArrayEquals(verwacht, gesorteerd);
        }
    }
}
