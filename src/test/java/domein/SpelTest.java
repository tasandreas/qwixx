package domein;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

class SpelTest {

    private Spel spel;
    private SpelerRepository spelerRepository;
    @BeforeEach
    void setup(){
        spelerRepository = new SpelerRepository();
        spel  = new Spel(spelerRepository);
    }
    @Test
    void constructor_MaaktZesDobbelstenen() {
        assertEquals(6, spel.getDobbelstenen().size());
    }

    @Test
    void constructor_BevatTweeWitteDobbelstenen() {

        assertEquals(Kleur.WIT, spel.getDobbelstenen().get(0).kleur);
        assertEquals(Kleur.WIT, spel.getDobbelstenen().get(1).kleur);
    }

    @Test
    void constructor_BevatElkeKleurEenKeer() {

        assertEquals(Kleur.ROOD, spel.getDobbelstenen().get(2).kleur);
        assertEquals(Kleur.GEEL, spel.getDobbelstenen().get(3).kleur);
        assertEquals(Kleur.GROEN, spel.getDobbelstenen().get(4).kleur);
        assertEquals(Kleur.BLAUW, spel.getDobbelstenen().get(5).kleur);
    }

    @Test
    void rolDobbelstenen_AlleOgenTussen1En6() {
        spel.rolDobbelstenen();

        for (Dobbelsteen d : spel.getDobbelstenen()) {
            assertTrue(d.aantalOgen >= 1 && d.aantalOgen <= 6);
        }
    }

    @Test
    void geefInfoGegooideDobbelstenen_ReturntString() {
        spel.rolDobbelstenen();

       // String resultaat = spel.geefInfoGegooideDobbelstenen();

       // assertNotNull(resultaat);
       // assertFalse(resultaat.isEmpty());
    }

    @Test
    void optie1_CycletOverAlleSpelers_EnGaatDaarnaPasNaarOptie2() {
        spel = new Spel(spelerRepository, List.of(
                new Speler("speler1", 2000),
                new Speler("speler2", 2001),
                new Speler("speler3", 2002)
        ), SoortSpel.BASIS);

        spel.startRonde();

        assertEquals("speler1", spel.getHuidigeOptie1Speler().getGebruikersnaam());
        assertEquals(Fase.OPTIE1, spel.getHuidigeFase());

        spel.beeindigBeurtVanHuidigeOptie1Speler();
        assertEquals("speler2", spel.getHuidigeOptie1Speler().getGebruikersnaam());
        assertEquals(Fase.OPTIE1, spel.getHuidigeFase());

        spel.beeindigBeurtVanHuidigeOptie1Speler();
        assertEquals("speler3", spel.getHuidigeOptie1Speler().getGebruikersnaam());
        assertEquals(Fase.OPTIE1, spel.getHuidigeFase());

        spel.beeindigBeurtVanHuidigeOptie1Speler();
        assertEquals(Fase.OPTIE2, spel.getHuidigeFase());
    }

    @Test
    void naOptie2_GaatActieveSpelerDoorNaarVolgendeEnWachtSpelOpNieuweWorp() {
        spel = new Spel(spelerRepository, List.of(
                new Speler("speler1", 2000),
                new Speler("speler2", 2001)
        ), SoortSpel.BASIS);

        spel.startRonde();
        spel.beeindigBeurtVanHuidigeOptie1Speler();
        spel.beeindigBeurtVanHuidigeOptie1Speler();
        spel.beeindigOptie2FaseEnGaNaarVolgendeRonde();

        assertEquals("speler2", spel.getActieveSpeler().getGebruikersnaam());
        assertFalse(spel.isRondeGestart());
        assertEquals(Fase.OPTIE1, spel.getHuidigeFase());
    }
}
