package domein;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

class DobbelsteenTest {

    @ParameterizedTest
    @EnumSource(Kleur.class)
    void maakDobbelsteen_Kleur_MaaktDobbelsteen(Kleur kleur) {
        Dobbelsteen dobbelsteen = new Dobbelsteen(kleur);
        assertEquals(kleur, dobbelsteen.getKleur());
    }
}