package domein;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RijTest {
    private Rij rij;
    @BeforeEach
    void setup() {
        rij = new Rij(Kleur.GROEN, new int[]{1,2,3,4,5,6,7,8,9,10,11,12});
    }

    @Test
    void kruisAan_EersteVakje_Gelukt() {

        // doing
        rij.kruisAan(1);

        // knowing
        assertEquals(1, rij.getAangekruist().size());
        assertTrue(rij.getAangekruist().contains(1));
    }

    @Test
    void kruisAan_VakjeLinks_NietToegelaten() {

        // doing
        rij.kruisAan(5);

        // knowing (exception controleren)
        assertThrows(IllegalArgumentException.class,
                () -> rij.kruisAan(3));
    }

    @Test
    void berekenScore_Correct() {

        // doing
        rij.kruisAan(1);
        rij.kruisAan(2);
        rij.kruisAan(3);

        // knowing
        assertEquals(6, rij.berekenScore());
    }

    @Test
    void rijSluit_BijLaatsteVakje() {

        // doing
        rij.kruisAan(1);
        rij.kruisAan(2);
        rij.kruisAan(3);
        rij.kruisAan(4);
        rij.kruisAan(5);
        rij.kruisAan(12);

        // knowing
        assertTrue(rij.isGesloten());
    }

    @Test
    void kruisAan_WaardeNietInRij_GooitException() {

        // knowing
        assertThrows(IllegalArgumentException.class,
                () -> rij.kruisAan(20));
    }

    @Test
    void kruisAan_LaatsteVakjeMeteen_ToegelatenEnRijGesloten() {
        rij.kruisAan(12);

        assertTrue(rij.getAangekruist().contains(12));
        assertTrue(rij.isGesloten());
    }
}
