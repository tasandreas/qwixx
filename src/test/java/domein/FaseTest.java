package domein;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FaseTest {

    @Test
    void enum_BevatTweeFases() {
        assertEquals(2, Fase.values().length);
    }

    @Test
    void enum_BevatOptie1EnOptie2() {
        assertEquals(Fase.OPTIE1, Fase.valueOf("OPTIE1"));
        assertEquals(Fase.OPTIE2, Fase.valueOf("OPTIE2"));
    }
}
