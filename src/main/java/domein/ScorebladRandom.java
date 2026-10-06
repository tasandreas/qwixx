package domein;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ScorebladRandom extends Scoreblad {

    private static final SecureRandom RANDOM = new SecureRandom();

    /**
     * Maakt de rijen voor het randomscoreblad aan.
     * Elke rij krijgt een kleur en een willekeurige volgorde van waarden.
     */
    @Override
    protected void maakRijen() {
        getRijen().add(new Rij(Kleur.ROOD, maakWillekeurigeVolgorde()));
        getRijen().add(new Rij(Kleur.GEEL, maakWillekeurigeVolgorde()));
        getRijen().add(new Rij(Kleur.GROEN, maakWillekeurigeVolgorde()));
        getRijen().add(new Rij(Kleur.BLAUW, maakWillekeurigeVolgorde()));
    }
    /**
     * Maakt een willekeurige volgorde van de waarden 1 tot en met 12.
     *
     * @return een array met de waarden 1 tot en met 12 in willekeurige volgorde
     */
    private int[] maakWillekeurigeVolgorde() {
        List<Integer> waarden = new ArrayList<>();

        for (int i = 2; i <= 12; i++) {
            waarden.add(i);
        }

        Collections.shuffle(waarden, RANDOM);

        return waarden.stream()
                .mapToInt(Integer::intValue)
                .toArray();
    }
}
