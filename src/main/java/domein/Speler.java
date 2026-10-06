package domein;

import java.time.Year;

public class Speler {
    private final String gebruikersnaam;
    private final int geboortejaar;
    private boolean isActief = false;
  //  private int mislukteWorpen = 0;
    private int[] rijen = new int[4];
    public Scoreblad scoreblad;
    private int score;
    private int aantalGewonnen = 0;
    private int aantalGespeeld = 0;

    /**
     * Maakt een nieuwe speler aan met de opgegeven gebruikersnaam en geboortejaar.
     * De gebruikersnaam en het geboortejaar worden eerst gecontroleerd.
     * Daarna krijgt de speler een nieuw basisscoreblad.
     *
     * @param gebruikersnaam de gebruikersnaam van de speler
     * @param geboortejaar het geboortejaar van de speler
     */
    public Speler(String gebruikersnaam, int geboortejaar) {
        //controleerMethodes worden aangeroepen
        controleerGebruikersnaam(gebruikersnaam);
        controleerGeboortejaar(geboortejaar);
        this.gebruikersnaam = gebruikersnaam;
        this.geboortejaar = geboortejaar;
        this.scoreblad = new ScorebladBasis();
    }
    /**
     * Geeft het scoreblad van de speler terug.
     *
     * @return het scoreblad van de speler
     */
    public Scoreblad getScoreblad(){
        return scoreblad;
    }
    /**
     * Stelt het scoreblad van de speler in.
     *
     * @param scoreblad het nieuwe scoreblad van de speler
     */
    public void setScoreblad(Scoreblad scoreblad) {
        this.scoreblad = scoreblad;
    }
    /**
     * Stelt de score van de speler in.
     *
     * @param score de nieuwe score van de speler
     */
    public void setScore(int score){
        this.score = score;
    }
    /**
     * Geeft de score van de speler terug.
     *
     * @return de score van de speler
     */
    public int getScore(){
        return score;
    }
    /**
     * Geeft de gebruikersnaam van de speler terug.
     *
     * @return de gebruikersnaam van de speler
     */
    public String getGebruikersnaam() {
        return gebruikersnaam;
    }
    /**
     * Geeft de gebruikersnaam van de speler terug als tekstvoorstelling.
     *
     * @return de gebruikersnaam van de speler
     */
    @Override
    public String toString() {
        return gebruikersnaam;
    }
    /**
     * Geeft het geboortejaar van de speler terug.
     *
     * @return het geboortejaar van de speler
     */
    public int getGeboortejaar() {
        return geboortejaar;
    }
    /**
     * Controleert of de opgegeven gebruikersnaam geldig is.
     * Een geldige gebruikersnaam mag niet leeg zijn en moet tussen 5 en 45 karakters lang zijn.
     *
     * @param gebruikersnaam de gebruikersnaam die gecontroleerd wordt
     * @throws IllegalArgumentException als de gebruikersnaam leeg is, korter is dan 5 karakters
     *                                  of langer is dan 45 karakters
     */
    public static void controleerGebruikersnaam(String gebruikersnaam) {
        if (gebruikersnaam.isBlank() || gebruikersnaam.length() < 5 || gebruikersnaam.length() > 45) {
            throw new IllegalArgumentException(Vertaling.tekst("exception.usernameLength"));
        }
    }
    /**
     * Controleert of het opgegeven geboortejaar geldig is.
     * Een geldig geboortejaar hoort bij een speler van 5 tot en met 99 jaar.
     *
     * @param geboortejaar het geboortejaar dat gecontroleerd wordt
     * @throws IllegalArgumentException als de speler jonger is dan 5 of ouder is dan 99
     */
    public static void controleerGeboortejaar(int geboortejaar) {
        int huidigJaar = Year.now().getValue();
        if (geboortejaar < huidigJaar - 99 || geboortejaar > huidigJaar - 5) {
            throw new IllegalArgumentException(Vertaling.tekst("exception.invalidBirthYear"));
        }
    }
    /**
     * Geeft terug of de speler actief is.
     *
     * @return true als de speler actief is, anders false
     */
    public boolean isActief() {
        return isActief;
    }
    /**
     * Stelt in of de speler actief is.
     *
     * @param actief true als de speler actief moet zijn, anders false
     */
    public void setActief(boolean actief) {
        //als spel.getActieveSpelerIndex = spelers.get(i)
        isActief = actief;
    }
    /**
     * Voegt een kruisje toe aan het scoreblad voor de opgegeven waarde.
     *
     * @param waarde de waarde waarvoor een kruisje wordt toegevoegd
     */
    public void voegKruisjeToe(int waarde){
        scoreblad.kruisAan(waarde);
    }
    /**
     * Voegt een kruisje toe aan het scoreblad voor de opgegeven kleur en waarde.
     *
     * @param kleur de kleur waarvoor een kruisje wordt toegevoegd
     * @param waarde de waarde waarvoor een kruisje wordt toegevoegd
     */
    public void voegKruisjeToe(Kleur kleur, int waarde){
        scoreblad.kruisAan(kleur, waarde);
    }
    /**
     * Geeft het aantal mislukte worpen van het scoreblad terug.
     *
     * @return het aantal mislukte worpen
     */
    public int getMislukteWorpen() {

        return getScoreblad().getMislukteWorpen();
    }
    /**
     * Verhoogt het aantal mislukte worpen op het scoreblad met één.
     */
   public void setMislukteWorpen(){
       scoreblad.verhoogMislukteWorpen();
   }
    /**
     * Geeft het aantal gesloten rijen terug.
     * Een rij wordt als gesloten beschouwd wanneer de waarde minstens 12 is.
     *
     * @return het aantal gesloten rijen
     */
    public int getAantalGeslotenRijen() {
        int aantal = 0;

        for(int rij : rijen) {
            if(rij >= 12) {
                aantal++;
            }
        }

        return aantal;
    }

    /**
     * Verhoogt het aantal gewonnen spellen met één.
     */
    public void verhoogAantalGewonnen(){
        aantalGewonnen ++;
    }
    /**
     * Verhoogt het aantal gespeelde spellen met één.
     */
    public void verhoogAantalGespeeld(){
        aantalGespeeld ++;
    }
    /**
     * Geeft het aantal gewonnen spellen terug.
     *
     * @return het aantal gewonnen spellen
     */
    public int getAantalGewonnen(){
        return aantalGewonnen;
    }
    /**
     * Geeft het aantal gespeelde spellen terug.
     *
     * @return het aantal gespeelde spellen
     */
    public int getAantalGespeeld(){
        return aantalGespeeld;
    }
}



