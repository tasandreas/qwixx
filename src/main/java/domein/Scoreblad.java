package domein;

import java.util.ArrayList;
import java.util.List;


public abstract class Scoreblad {

    private List<Rij> rijen;
    private int mislukteWorpen;
    /**
     * Maakt een nieuw scoreblad aan.
     * Initialiseert de rijen en het aantal mislukte worpen.
     * Daarna worden de rijen van het scoreblad aangemaakt.
     */
    public Scoreblad() {
        rijen = new ArrayList<>();
        mislukteWorpen = 0;
        maakRijen();
    }
    /**
     * Geeft het aantal mislukte worpen terug.
     *
     * @return het aantal mislukte worpen
     */
    public int getMislukteWorpen() {
        return mislukteWorpen;
    }
    /**
     * Verhoogt het aantal mislukte worpen met één.
     */
    public void verhoogMislukteWorpen() {
        mislukteWorpen++;
    }
    /**
     * Geeft de rijen van het scoreblad terug.
     *
     * @return de lijst met rijen
     */
    public List<Rij> getRijen() {
        return rijen;
    }
    /**
     * Geeft de rij met de opgegeven kleur terug.
     *
     * @param kleur de kleur van de rij
     * @return de rij met de opgegeven kleur, of null als die rij niet bestaat
     */
    public Rij getRij(Kleur kleur) {
        for (Rij r : rijen) {
            if (r.getKleur() == kleur) {

                return r;
            }
        }
        return null;
    }

    /**
     * Maakt de rijen van het scoreblad aan.
     * De concrete scorebladklasse bepaalt zelf welke rijen worden toegevoegd.
     */
    protected abstract void maakRijen();
    /**
     * Kruist een waarde aan in de eerste rij waarin deze zet geldig is.
     *
     * @param waarde de waarde die aangekruist wordt
     * @throws IllegalArgumentException als de waarde in geen enkele rij geldig aangekruist kan worden
     */
    public void kruisAan(int waarde){
        for(Rij rij : rijen){
            try {
                rij.kruisAan(waarde);
                return;
            } catch (Exception e){
            }
        }

        throw new IllegalArgumentException(Vertaling.tekst("exception.noValidMove"));
    }

    /**
     * Kruist een waarde aan in de rij met de opgegeven kleur.
     *
     * @param kleur de kleur van de rij waarin aangekruist wordt
     * @param waarde de waarde die aangekruist wordt
     * @throws IllegalArgumentException als er geen rij bestaat met de opgegeven kleur
     */
    public void kruisAan(Kleur kleur, int waarde){
        for(Rij rij : rijen){
            if(rij.getKleur() == kleur){
                rij.kruisAan(waarde);
                return;
            }
        }

        throw new IllegalArgumentException(Vertaling.tekst("exception.noValidRow"));
    }
    /**
     * Berekent de totale score van het scoreblad.
     * De score bestaat uit de som van de scores van alle rijen,
     * verminderd met 5 punten per mislukte worp.
     *
     * @return de totale score van het scoreblad
     */
    public int berekenScore() {
        int totaal = 0;
        for (Rij rij : rijen) {
            totaal += rij.berekenScore();
        }
        totaal -= mislukteWorpen * 5;
        return totaal;
    }






}



