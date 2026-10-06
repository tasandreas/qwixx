package domein;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

public class Dobbelsteen {

    public Kleur kleur;
    public int aantalOgen;

    /**
     * Maakt een nieuwe dobbelsteen aan met de opgegeven kleur.
     *
     * @param kleur de kleur van de dobbelsteen
     */
    public Dobbelsteen(Kleur kleur){
        this.kleur = kleur;
    }
    /**
     * Rolt de dobbelsteen en geeft deze een willekeurige waarde van 1 tot en met 6.
     */
    public void rol(){
        SecureRandom srnd = new SecureRandom();
        this.aantalOgen = srnd.nextInt(1, 7);
    }
    /**
     * Geeft het aantal ogen van de dobbelsteen terug.
     *
     * @return het aantal ogen van de dobbelsteen
     */
    public int getAantalOgen() {
        return aantalOgen;
    }
    /**
     * Geeft de kleur van de dobbelsteen terug.
     *
     * @return de kleur van de dobbelsteen
     */
    public Kleur getKleur() {
        return kleur;
    }
    /**
     * Geeft een tekstvoorstelling van de dobbelsteen terug.
     *
     * @return de kleur en het aantal ogen van de dobbelsteen als tekst
     */
    @Override
    public String toString(){
        return String.format("%s : %d" ,this.kleur, this.aantalOgen);
    }
}
