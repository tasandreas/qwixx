package domein;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class SoortSpelTest {

    @Test
    void enum_BevatDrieSpeltypes() {
        assertArrayEquals(
                new SoortSpel[]{SoortSpel.BASIS, SoortSpel.VARIANT, SoortSpel.RANDOM},
                SoortSpel.values()
        );
    }
}
