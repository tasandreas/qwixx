package domein;

import exceptions.GebruikersnaamInGebruikException;
import persistentie.SpelerMapper;

import java.util.ArrayList;
import java.util.List;


public class SpelerRepository {
    private final SpelerMapper mapper;
    /**
     * Maakt een nieuwe spelerrepository aan.
     * Initialiseert de mapper waarmee spelers worden opgeslagen en opgehaald.
     */
    public SpelerRepository() {
        mapper = new SpelerMapper();
    }
    /**
     * Voegt een speler toe aan de repository.
     * Controleert eerst of de gebruikersnaam nog niet in gebruik is.
     *
     * @param speler de speler die toegevoegd wordt
     * @throws GebruikersnaamInGebruikException als er al een speler bestaat met dezelfde gebruikersnaam
     */

    public void voegToe(Speler speler) {
        if (bestaatSpeler(speler.getGebruikersnaam())) {
            throw new GebruikersnaamInGebruikException();
        }

        mapper.voegToe(speler);
    }
    /**
     * Controleert of er al een speler bestaat met de opgegeven gebruikersnaam.
     *
     * @param gebruikersnaam de gebruikersnaam die gecontroleerd wordt
     * @return true als de speler bestaat, anders false
     */
    private boolean bestaatSpeler(String gebruikersnaam) {
        return mapper.geefSpeler(gebruikersnaam) != null;
    }
    /**
     * Geeft alle gebruikersnamen van de spelers terug.
     *
     * @return een lijst met alle gebruikersnamen
     */
    public List<String> geefAlleGebruikersnamen() {
       List<String> gebruikersnamen = new ArrayList<>();
        for (Speler speler : mapper.geefSpelers()){
            gebruikersnamen.add(speler.getGebruikersnaam());
        }
        return gebruikersnamen;
    }
    /**
     * Geeft een speler terug op basis van de gebruikersnaam.
     * De vergelijking is niet hoofdlettergevoelig.
     *
     * @param gebruikersnaam de gebruikersnaam van de speler
     * @return de gevonden speler, of null als er geen speler gevonden is
     */
    public Speler geefSpelerOpGebruikersnaam(String gebruikersnaam){
        for(Speler speler : mapper.geefSpelers()){
            if (speler.getGebruikersnaam().equalsIgnoreCase(gebruikersnaam)) return speler;
        }

        return null;
    }
    /**
     * Geeft alle gebruikers terug.
     *
     * @return een lijst met alle spelers
     */
    public List<Speler> geefAlleGebruikers(){
        return mapper.geefSpelers();
    }
    /**
     * Geeft een speler terug op basis van de gebruikersnaam.
     *
     * @param gebruikersnaam de gebruikersnaam van de speler
     * @return de gevonden speler, of null als er geen speler gevonden is
     */
    public Speler geefSpeler(String gebruikersnaam) {
        return mapper.geefSpeler(gebruikersnaam);
    }
    /**
     * Verwijdert een speler op basis van de gebruikersnaam.
     *
     * @param gebruikersnaam de gebruikersnaam van de speler die verwijderd wordt
     */
    public void verwijderSpeler(String gebruikersnaam) {
        mapper.verwijder(gebruikersnaam);
    }
}
