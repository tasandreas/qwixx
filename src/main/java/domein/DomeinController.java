    package domein;

    import dto.DobbelsteenDTO;
    import dto.KeuzeDTO;

    import java.util.ArrayList;
    import java.util.List;

    public class DomeinController {
        private final SpelerRepository spelerRepository;
        private Spel spel;

        List<Speler> geselecteerdeSpelers;
        /**
         * Maakt een nieuwe domeincontroller aan.
         * Initialiseert de spelerrepository, het spel en de lijst met geselecteerde spelers.
         */
        public DomeinController() {
            spelerRepository = new SpelerRepository();
            spel = new Spel(spelerRepository);
            geselecteerdeSpelers = new ArrayList<>();
        }
        /**
         * Registreert een nieuwe speler met de opgegeven gebruikersnaam en geboortejaar.
         *
         * @param gebruikersnaam de gebruikersnaam van de nieuwe speler
         * @param geboortejaar het geboortejaar van de nieuwe speler
         */
        public void registreerSpeler(String gebruikersnaam, int geboortejaar) {
            Speler nieuweSpeler = new Speler(gebruikersnaam, geboortejaar);
            spelerRepository.voegToe(nieuweSpeler);
        }

        /**
         * Geeft alle geregistreerde spelers terug.
         *
         * @return een lijst met geregistreerde spelers
         */
        public List<Speler> geefGeregistreerdeSpelers() {
            return spelerRepository.geefAlleGebruikers();
        }
        /**
         * Voegt een bestaande speler toe aan het spel op basis van de gebruikersnaam.
         *
         * @param gebruikersnaam de gebruikersnaam van de speler
         * @throws IllegalArgumentException als de speler niet gevonden wordt
         */
        public void voegBestaandeSpelerToe(String gebruikersnaam) {
            Speler speler = spelerRepository.geefSpelerOpGebruikersnaam(gebruikersnaam);

            if (speler == null) {
                throw new IllegalArgumentException(Vertaling.tekst("exception.playerNotFound"));
            }

            registreerBestaandeSpeler(speler);
        }
        /**
         * Registreert een bestaande speler in het huidige spel.
         *
         * @param speler de speler die geregistreerd wordt
         */
        public void registreerBestaandeSpeler(Speler speler) {
            spel.registreerSpeler(speler.getGebruikersnaam(), speler.getGeboortejaar());
        }
        /**
         * Selecteert spelers voor een nieuw spel.
         * Er moeten minstens 2 en maximum 5 spelers geselecteerd worden.
         *
         * @param geselecteerdeSpelersVoorNieuwSpel de spelers die aan het nieuwe spel deelnemen
         * @throws IllegalArgumentException als het aantal spelers ongeldig is of als een speler null is
         */
        public void selecteerSpelersVoorNieuwSpel(List<Speler> geselecteerdeSpelersVoorNieuwSpel) {
            if (geselecteerdeSpelersVoorNieuwSpel == null || geselecteerdeSpelersVoorNieuwSpel.size() < 2 ||
                    geselecteerdeSpelersVoorNieuwSpel.size() > 5) {
                throw new IllegalArgumentException(Vertaling.tekst("exception.selectPlayersBetween"));
            }

            geselecteerdeSpelers = new ArrayList<>(geselecteerdeSpelersVoorNieuwSpel);
            for (Speler speler : geselecteerdeSpelers) {
                if (speler == null) {
                    throw new IllegalArgumentException(Vertaling.tekst("exception.playerNotFound"));
                }
            }
        }
        /**
         * Start een nieuw spel met de opgegeven spelsoort.
         * Als er spelers geselecteerd zijn, worden die gebruikt.
         * Anders worden de spelers uit het huidige spel gebruikt.
         *
         * @param soortSpel de soort van het spel
         */
            public void startSpel(SoortSpel soortSpel){
                List<Speler> spelersVoorNieuwSpel = new ArrayList<>();

                if (geselecteerdeSpelers != null && !geselecteerdeSpelers.isEmpty()) {
                    spelersVoorNieuwSpel = geselecteerdeSpelers;
                } else {
                    spelersVoorNieuwSpel = spel.getSpelers();
                }


                spel = new Spel(spelerRepository, spelersVoorNieuwSpel, soortSpel);
            }
        /**
         * Start een nieuwe ronde.
         */
            public void startRonde () {
                spel.startRonde();
            }
        /**
         * Geeft informatie over de gegooide dobbelstenen terug.
         *
         * @return een lijst met dobbelsteen-DTO's
         */
            public List<DobbelsteenDTO> infoGegooideDobbelstenen () {
                return spel.infoGegooideDobbelstenen();
            }
        /**
         * Geeft de waarde van optie 1 terug.
         *
         * @return de waarde van optie 1
         */

            public int geefOptie1Waarde () {
                return spel.geefOptie1Waarde();
            }
        /**
         * Beëindigt de optie 1-fase.
         */
            public void beeindigOptie1Fase () {
                spel.beeindigOptie1Fase();
            }
        /**
         * Beëindigt de beurt van de huidige optie 1-speler.
         */
            public void beeindigBeurtVanHuidigeOptie1Speler() {
                spel.beeindigBeurtVanHuidigeOptie1Speler();
            }
        /**
         * Geeft alle mogelijke keuzes voor optie 2 terug.
         *
         * @return een lijst met optie 2-keuzes
         */
            public List<KeuzeDTO> geefOptie2Keuzes () {
                return spel.geefOptie2Keuzes();
            }
        /**
         * Beëindigt de optie 2-fase en maakt het spel klaar voor de volgende ronde.
         */
            public void beeindigOptie2FaseEnGaNaarVolgendeRonde () {
                spel.beeindigOptie2FaseEnGaNaarVolgendeRonde();
            }
        /**
         * Geeft de actieve speler terug.
         *
         * @return de actieve speler
         */
            public Speler getActieveSpeler () {
                return spel.getActieveSpeler();
            }
        /**
         * Geeft de huidige speler terug.
         *
         * @return de huidige speler
         */
            public Speler geefHuidigeSpeler () {
                return spel.getActieveSpeler();
            }
        /**
         * Geeft de index van de actieve speler terug.
         *
         * @return de index van de actieve speler
         */
            public int getActieveSpelerIndex () {
                return spel.getActieveSpelerIndex();
            }
        /**
         * Geeft de huidige fase van het spel terug.
         *
         * @return de huidige fase
         */

            public Fase getHuidigeFase () {
                return spel.getHuidigeFase();
            }
        /**
         * Geeft de spelers van het huidige spel terug.
         *
         * @return een lijst met spelers
         */
            public List<Speler> getSpelers () {
                return spel.getSpelers();
            }
        /**
         * Geeft de spelers van het huidige spel terug.
         *
         * @return een lijst met spelers
         */
            public List<Speler> getAantalSpelers () {
                return spel.getSpelers();
            }

        /**
         * Controleert of het spel afgelopen is.
         *
         * @return true als het spel afgelopen is, anders false
         */
            public boolean isEindeSpel () {
                return spel.isEindeSpel();
            }
        /**
         * Bepaalt de winnaar of winnaars van het spel.
         *
         * @return een lijst met winnaars
         */
            public List<Speler> bepaalWinnaars () {
                return spel.bepaalWinnaars();
            }
        /**
         * Berekent de scores van alle spelers.
         */
            public void berekenScores () {
                spel.berekenScores();
            }
        /**
         * Voegt een kruisje toe voor de actieve speler op basis van een waarde.
         *
         * @param waarde de waarde die aangekruist wordt
         */
            // optie 1: actieve speler kruisje laten zetten
            public void voegKruisjeToe ( int waarde){
                spel.getActieveSpeler().voegKruisjeToe(waarde);
            }
        /**
         * Voegt een kruisje toe voor de actieve speler op basis van kleur en waarde.
         *
         * @param kleur de kleur van de rij
         * @param waarde de waarde die aangekruist wordt
         */
            // optie 2: actieve speler kruisje laten zetten met kleur + waarde
            public void voegKruisjeToe (Kleur kleur,int waarde){
                spel.getActieveSpeler().voegKruisjeToe(kleur, waarde);
            }
        /**
         * Voegt een kruisje toe voor een specifieke speler.
         *
         * @param spelerIndex de index van de speler
         * @param kleur de kleur van de rij
         * @param waarde de waarde die aangekruist wordt
         */
            public void voegKruisjeToeVoorSpeler ( int spelerIndex, Kleur kleur,int waarde){
                spel.getSpelers().get(spelerIndex).voegKruisjeToe(kleur, waarde);
            }
        /**
         * Verhoogt het aantal mislukte worpen van de actieve speler.
         */
            public void verhoogMislukteWorpen () {
                spel.verhoogMislukteWorpen(spel.getActieveSpeler());
            }
        /**
         * Verhoogt het aantal mislukte worpen van een specifieke speler.
         *
         * @param spelerIndex de index van de speler
         */
            public void verhoogMislukteWorpenVoorSpeler (int spelerIndex) {
                spel.verhoogMislukteWorpen(spel.getSpelers().get(spelerIndex));
            }
        /**
         * Geeft het aantal mislukte worpen van een speler terug.
         *
         * @param speler de speler waarvan het aantal mislukte worpen wordt opgevraagd
         * @return het aantal mislukte worpen
         */
            public int getMislukteWorpen (Speler speler){
                return speler.getMislukteWorpen();
            }
        /**
         * Verhoogt het aantal mislukte worpen van een speler.
         *
         * @param speler de speler waarvan het aantal mislukte worpen verhoogd wordt
         */
            public void setMislukteWorpen (Speler speler){
                speler.setMislukteWorpen();
            }
        /**
         * Geeft het aantal gesloten rijen van de actieve speler terug.
         *
         * @return het aantal gesloten rijen
         */
            public int getAantalGeslotenRijen () {
                return spel.getActieveSpeler().getAantalGeslotenRijen();
            }
        /**
         * Geeft de rijen van de actieve speler terug.
         *
         * @return een lijst met rijen van de actieve speler
         */
            public List<Rij> getRijenActieveSpeler () {
                return spel.getActieveSpeler().getScoreblad().getRijen();
            }
        /**
         * Geeft de rijen van een specifieke speler terug.
         *
         * @param spelerIndex de index van de speler
         * @return een lijst met rijen van de speler
         */
            public List<Rij> getRijenVoorSpeler(int spelerIndex) {
                return spel.getSpelers().get(spelerIndex).getScoreblad().getRijen();
            }
        /**
         * Voegt een speler toe aan de spelerrepository.
         *
         * @param speler de speler die toegevoegd wordt
         */
            public void voegToe (Speler speler){
                spelerRepository.voegToe(speler);
            }
        /**
         * Verwijdert een speler uit de spelerrepository.
         *
         * @param gebruikersnaam de gebruikersnaam van de speler die verwijderd wordt
         */
        public void verwijderSpeler(String gebruikersnaam) {
            spelerRepository.verwijderSpeler(gebruikersnaam);
        }
        /**
         * Verhoogt het aantal gespeelde spellen voor alle spelers in het spel.
         */
        public void verhoogAantalGespeeldVoorAlleSpelers() {
            for (Speler speler : spel.getSpelers()) {
                speler.verhoogAantalGespeeld();
            }
        }
        /**
         * Verhoogt het aantal gewonnen spellen voor alle winnaars.
         *
         * @param winnaars de lijst met winnaars
         */
        public void verhoogAantalGewonnenVoorWinnaars(List<Speler> winnaars) {
            for (Speler winnaar : winnaars) {
                winnaar.verhoogAantalGewonnen();
            }
        }
        /**
         * Geeft de geselecteerde spelers voor een nieuw spel terug.
         *
         * @return een lijst met geselecteerde spelers
         */
        public List<Speler> getSpelersVoorNieuwSpel() {
            return geselecteerdeSpelers;
        }
        /**
         * Geeft de soort van het huidige spel terug.
         *
         * @return de soort van het spel
         */
        public SoortSpel getSoortSpel() { return spel.getSoortSpel(); }
        /**
         * Geeft de huidige optie 1-speler terug.
         *
         * @return de huidige optie 1-speler
         */
        public Speler getHuidigeOptie1Speler() {
            return spel.getHuidigeOptie1Speler();
        }
        /**
         * Geeft de index van de huidige optie 1-speler terug.
         *
         * @return de index van de huidige optie 1-speler
         */
        public int getHuidigeOptie1SpelerIndex() {
            return spel.getHuidigeOptie1SpelerIndex();
        }
    }
