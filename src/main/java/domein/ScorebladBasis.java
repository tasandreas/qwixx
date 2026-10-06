package domein;

public class ScorebladBasis extends Scoreblad {
    /**
     * Maakt de rijen voor het basisscoreblad aan.
     * De rode en gele rij krijgen waarden van 1 tot en met 12.
     * De groene en blauwe rij krijgen waarden van 12 tot en met 1.
     */
    @Override
    protected void maakRijen() {
        getRijen().add(new Rij(Kleur.ROOD, new int[]{2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12}));
        getRijen().add(new Rij(Kleur.GEEL, new int[]{2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12}));
        getRijen().add(new Rij(Kleur.GROEN, new int[]{12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2}));
        getRijen().add(new Rij(Kleur.BLAUW, new int[]{12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2}));
    }
}
