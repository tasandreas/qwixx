package domein;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DomeinControllerTest {

    @Test
    void selecteerSpelersVoorNieuwSpel_MinderDanTweeSpelers_WerptException() {
        DomeinController dc = new DomeinController();

        assertThrows(IllegalArgumentException.class,
                () -> dc.selecteerSpelersVoorNieuwSpel(List.of(new Speler("speler1", 2000))));
    }

    @Test
    void startSpel_Basis_GebruiktGeselecteerdeSpelers() {
        DomeinController dc = new DomeinController();
        List<Speler> spelers = List.of(
                new Speler("speler1", 2000),
                new Speler("speler2", 2001)
        );

        dc.selecteerSpelersVoorNieuwSpel(spelers);
        dc.startSpel(SoortSpel.BASIS);

        assertEquals(2, dc.getSpelers().size());
        assertInstanceOf(ScorebladBasis.class, dc.getSpelers().get(0).getScoreblad());
    }

    @Test
    void startSpel_Variant_GeeftSpelersVariantScoreblad() {
        DomeinController dc = new DomeinController();
        List<Speler> spelers = List.of(
                new Speler("speler1", 2000),
                new Speler("speler2", 2001)
        );

        dc.selecteerSpelersVoorNieuwSpel(spelers);
        dc.startSpel(SoortSpel.VARIANT);

        assertInstanceOf(ScorebladVariant.class, dc.getSpelers().get(0).getScoreblad());
    }

    @Test
    void startSpel_Random_GeeftSpelersRandomScoreblad() {
        DomeinController dc = new DomeinController();
        List<Speler> spelers = List.of(
                new Speler("speler1", 2000),
                new Speler("speler2", 2001)
        );

        dc.selecteerSpelersVoorNieuwSpel(spelers);
        dc.startSpel(SoortSpel.RANDOM);

        assertInstanceOf(ScorebladRandom.class, dc.getSpelers().get(0).getScoreblad());
    }
}
