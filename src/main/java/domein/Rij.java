package domein;


import java.util.ArrayList;
import java.util.List;
import domein.*;

public class Rij {

    public Kleur kleur;
    private int[] waarden;
    private List<Integer> aangekruist;
    private boolean gesloten;

    /**
     * Maakt een nieuwe rij aan met de opgegeven kleur en waarden.
     * De rij start zonder aangekruiste waarden en is nog niet gesloten.
     *
     * @param kleur de kleur van de rij
     * @param waarden de waarden die in deze rij voorkomen
     */
    public Rij(Kleur kleur, int[] waarden) {
        this.kleur = kleur;
        this.waarden = waarden;
        this.aangekruist = new ArrayList<>();
        this.gesloten = false;
    }
    /**
     * Geeft de kleur van de rij terug.
     *
     * @return de kleur van de rij
     */
    public Kleur getKleur() {
        return kleur;
    }

    /**
     * Geeft terug of de rij gesloten is.
     *
     * @return true als de rij gesloten is, anders false
     */
    public boolean isGesloten(){
        return gesloten;
    }
    /**
     * Geeft de aangekruiste waarden van de rij terug.
     *
     * @return de lijst met aangekruiste waarden
     */
    public List<Integer> getAangekruist(){
        return aangekruist;
    }
    /**
     * Controleert of de opgegeven waarde aangekruist mag worden.
     * Een waarde mag enkel aangekruist worden als de rij niet gesloten is,
     * de waarde in de rij voorkomt en rechts van de laatst aangekruiste waarde staat.
     *
     * @param waarde de waarde die gecontroleerd wordt
     * @return true als de waarde aangekruist mag worden, anders false
     */
    public boolean kanKruisAan(int waarde) {
        if (gesloten) {
            return false;
        }

        int index = zoekIndex(waarde);
        if (index == -1) {
            return false;
        }

        int laasteIndex = laatsteAangekruisteIndex();
        if (index <= laasteIndex) {
            return false;
        }

        return true;
    }
    /**
     * Kruist de opgegeven waarde aan in de rij.
     * Als de waarde het laatste vakje van de rij is, wordt de rij gesloten.
     *
     * @param waarde de waarde die aangekruist wordt
     * @throws IllegalArgumentException als de waarde niet geldig aangekruist kan worden
     */
    public void kruisAan(int waarde){
        int index = zoekIndex(waarde);

        if(!kanKruisAan(waarde)){
            throw new IllegalArgumentException(Vertaling.tekst("exception.invalidMoveForRow"));
        }
        aangekruist.add(waarde);
        if(isLaatsteVakje(index)){
            gesloten = true;
        }
    }

    /**
     * Zoekt de index van de opgegeven waarde in de rij.
     *
     * @param waarde de waarde waarvan de index gezocht wordt
     * @return de index van de waarde, of -1 als de waarde niet voorkomt
     */
    public int zoekIndex(int waarde){
        for(int i = 0; i < waarden.length; i++){
            if(waarden[i] == waarde){
                return i;
            }
        }
        return -1;
    }
    /**
     * Geeft de index van de laatst aangekruiste waarde terug.
     *
     * @return de index van de laatst aangekruiste waarde, of -1 als er nog geen waarde is aangekruist
     */
    public int laatsteAangekruisteIndex(){
        if(aangekruist.isEmpty()){
            return -1;
        }
        int laasteWaarde = aangekruist.get(aangekruist.size() - 1);

        return zoekIndex(laasteWaarde);
    }
    /**
     * Controleert of de opgegeven index het laatste vakje van de rij is.
     *
     * @param index de index die gecontroleerd wordt
     * @return true als de index het laatste vakje is, anders false
     */
    private boolean isLaatsteVakje(int index){
        return index == waarden.length -1;
    }
    /**
     * Berekent de score van deze rij.
     * De score wordt berekend op basis van het aantal aangekruiste waarden.
     *
     * @return de score van de rij
     */
    public int berekenScore(){
        int n = aangekruist.size();
        return n * (n+1) /2;
    }
    /**
     * Geeft de waarden van deze rij terug.
     *
     * @return de waarden van de rij
     */
    public int[] getWaarden() {
        return waarden;
    }



}
