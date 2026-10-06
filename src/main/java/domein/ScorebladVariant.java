package domein;

public class ScorebladVariant extends Scoreblad{
    /**
     * Maakt de rijen voor het variantscoreblad aan.
     * Elke rij krijgt een kleur en een specifieke volgorde van waarden.
     */
    @Override
    protected void maakRijen() {
        getRijen().add(new Rij(Kleur.ROOD, new int[]{2, 6, 3, 7, 4, 8, 5, 9, 10, 11, 1, 12}));
        getRijen().add(new Rij(Kleur.GEEL, new int[]{3, 7, 2, 6, 5, 9, 4, 8, 10, 11, 1, 12}));
        getRijen().add(new Rij(Kleur.GROEN, new int[]{12, 1, 11, 10, 8, 4, 9, 5, 7, 3, 6, 2}));
        getRijen().add(new Rij(Kleur.BLAUW, new int[]{12, 1, 11, 10, 9, 5, 8, 4, 7, 3, 6, 2}));
    }
}
