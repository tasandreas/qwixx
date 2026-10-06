package domein;

import dto.DobbelsteenDTO;
import dto.KeuzeDTO;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

public class Spel {

    private final List<Dobbelsteen> dobbelstenen;
    private final List<Speler> spelers;
    private final SpelerRepository spelerRepository;

    private int actieveSpelerIndex;
    private int optie1SpelerOffset;
    private Fase huidigeFase;
    private boolean rondeGestart;
    private SoortSpel soortSpel;

    private boolean isGui = false;

    private static final SecureRandom srnd = new SecureRandom();

    /**
     * Maakt een nieuw spel aan.
     * Initialiseert de lijsten met dobbelstenen en spelers, maakt de standaard dobbelstenen aan
     * en zet de beginwaarden voor de actieve speler, fase en ronde.
     */
    public Spel() {
        this.dobbelstenen = new ArrayList<>();
        this.spelers = new ArrayList<>();
        this.spelerRepository = null;

        dobbelstenen.add(new Dobbelsteen(Kleur.WIT));
        dobbelstenen.add(new Dobbelsteen(Kleur.WIT));
        dobbelstenen.add(new Dobbelsteen(Kleur.ROOD));
        dobbelstenen.add(new Dobbelsteen(Kleur.GEEL));
        dobbelstenen.add(new Dobbelsteen(Kleur.GROEN));
        dobbelstenen.add(new Dobbelsteen(Kleur.BLAUW));

        this.actieveSpelerIndex = 0;
        this.optie1SpelerOffset = 0;
        this.huidigeFase = Fase.OPTIE1;
        this.rondeGestart = false;
    }
    /**
     * Maakt een nieuw spel aan met de opgegeven spelerrepository.
     *
     * @param spelerRepository de repository waarmee spelers beheerd worden
     */
    public Spel(SpelerRepository spelerRepository) {
        this();
    }
    /**
     * Maakt een nieuw spel aan met de opgegeven spelerrepository en spelers.
     * De spelers worden toegevoegd aan het spel. Als er minstens één speler is,
     * wordt de eerste speler actief gezet.
     *
     * @param spelerRepository de repository waarmee spelers beheerd worden
     * @param spelers de lijst met spelers die aan het spel worden toegevoegd
     */
    public Spel(SpelerRepository spelerRepository, List<Speler> spelers) {
        this();
        this.spelers.addAll(spelers);

        if (!this.spelers.isEmpty()) {
            this.spelers.get(0).setActief(true);
        }
    }
    /**
     * Maakt een nieuw spel aan met de opgegeven spelerrepository, spelers en spelsoort.
     * Afhankelijk van de spelsoort krijgt elke speler het juiste type scoreblad.
     *
     * @param spelerRepository de repository waarmee spelers beheerd worden
     * @param spelers de lijst met spelers die aan het spel worden toegevoegd
     * @param soortSpel de soort van het spel
     */
    public Spel(SpelerRepository spelerRepository, List<Speler> spelers, SoortSpel soortSpel) {
        this(spelerRepository, spelers);
        this.soortSpel = soortSpel;

        for (Speler speler : spelers) {
            if (soortSpel == SoortSpel.VARIANT) {
                speler.setScoreblad(new ScorebladVariant());
            } else if (soortSpel == SoortSpel.RANDOM) {
                speler.setScoreblad(new ScorebladRandom());
            } else {
                speler.setScoreblad(new ScorebladBasis());
            }
        }
    }
    /**
     * Geeft de soort van het spel terug.
     *
     * @return de soort van het spel
     */
    public SoortSpel getSoortSpel() {
        return soortSpel;
    }
    /**
     * Registreert een nieuwe speler met de opgegeven gebruikersnaam en het opgegeven geboortejaar.
     * De speler wordt toegevoegd aan de repository als die beschikbaar is, en daarna aan de lijst
     * met spelers in het spel. Als dit de eerste speler is, wordt deze actief gezet.
     *
     * @param gebruikersnaam de gebruikersnaam van de nieuwe speler
     * @param geboortejaar het geboortejaar van de nieuwe speler
     */
    public void registreerSpeler(String gebruikersnaam, int geboortejaar) {
        Speler nieuweSpeler = new Speler(gebruikersnaam, geboortejaar);

        if (spelerRepository != null) {
            try {
                spelerRepository.voegToe(nieuweSpeler);
            } catch (Exception e) {
                System.out.println(Vertaling.tekst("exception.databaseUnavailable"));
            }
        }

        spelers.add(nieuweSpeler);

        if (spelers.size() == 1) {
            actieveSpelerIndex = 0;
            spelers.get(0).setActief(true);
        }
    }
    /**
     * Start een nieuwe ronde.
     * Controleert eerst of er spelers geregistreerd zijn en of er nog geen ronde bezig is.
     * Daarna worden de dobbelstenen gerold en wordt de beginfase van de ronde ingesteld.
     *
     * @throws IllegalStateException als er geen spelers geregistreerd zijn
     *                               of als de huidige ronde nog niet is afgelopen
     */
    public void startRonde() {
        if (spelers.isEmpty()) {
            throw new IllegalStateException(Vertaling.tekst("exception.noPlayersRegistered"));
        }

        if (rondeGestart) {
            throw new IllegalStateException(Vertaling.tekst("exception.roundNotFinished"));
        }

        rolDobbelstenen();
        optie1SpelerOffset = 0;
        huidigeFase = Fase.OPTIE1;
        rondeGestart = true;
    }
    /**
     * Rolt alle dobbelstenen van het spel.
     */

    public void rolDobbelstenen() {
        for (Dobbelsteen d : dobbelstenen) {
            d.rol();
        }
    }
    /**
     * Geeft informatie terug over de gegooide dobbelstenen.
     * Voor elke dobbelsteen wordt een DTO aangemaakt met de kleur en het aantal ogen.
     *
     * @return een lijst met DTO's van de gegooide dobbelstenen
     */
    public List<DobbelsteenDTO> infoGegooideDobbelstenen() {
        List<DobbelsteenDTO> dtoGegooid = new ArrayList<>();

        for (Dobbelsteen dobbelsteen : dobbelstenen) {
            dtoGegooid.add(new DobbelsteenDTO(dobbelsteen.getKleur(), dobbelsteen.getAantalOgen()));
        }

        return dtoGegooid;
    }
    /**
     * Geeft de waarde voor optie 1 terug.
     * Deze waarde is de som van de twee witte dobbelstenen.
     *
     * @return de som van de witte dobbelstenen
     * @throws IllegalStateException als optie 1 momenteel niet aan de beurt is
     */
    public int geefOptie1Waarde() {
        if (!isGui && huidigeFase != Fase.OPTIE1) {
            throw new IllegalStateException(Vertaling.tekst("exception.option1NotActive"));
        }

        return berekenSomWitteDobbelstenen();
    }
    /**
     * Berekent de som van alle witte dobbelstenen.
     *
     * @return de som van de witte dobbelstenen
     */
    private int berekenSomWitteDobbelstenen() {
        int som = 0;

        for (Dobbelsteen dobbelsteen : dobbelstenen) {
            if (dobbelsteen.getKleur() == Kleur.WIT) {
                som += dobbelsteen.getAantalOgen();
            }
        }

        return som;
    }
    /**
     * Beëindigt de fase van optie 1 en gaat naar optie 2.
     *
     * @throws IllegalStateException als optie 1 momenteel niet aan de beurt is
     */
    public void beeindigOptie1Fase() {
        if (!isGui && huidigeFase != Fase.OPTIE1) {
            throw new IllegalStateException(Vertaling.tekst("exception.option1PhaseRequired"));
        }

        huidigeFase = Fase.OPTIE2;
    }
    /**
     * Beëindigt de beurt van de huidige optie 1-speler.
     * Wanneer alle spelers hun optie 1-beurt hebben gehad, wordt de optie 1-fase beëindigd.
     *
     * @throws IllegalStateException als optie 1 momenteel niet aan de beurt is
     */
    public void beeindigBeurtVanHuidigeOptie1Speler() {
        if (!isGui && huidigeFase != Fase.OPTIE1) {
            throw new IllegalStateException(Vertaling.tekst("exception.option1PhaseRequired"));
        }

        optie1SpelerOffset++;

        if (optie1SpelerOffset >= spelers.size()) {
            beeindigOptie1Fase();
        }
    }
    /**
     * Geeft alle mogelijke keuzes voor optie 2 terug.
     * Elke keuze bestaat uit de som van één witte dobbelsteen en één gekleurde dobbelsteen.
     *
     * @return een lijst met mogelijke optie 2-keuzes
     * @throws IllegalStateException als optie 2 momenteel niet aan de beurt is
     */
    public List<KeuzeDTO> geefOptie2Keuzes() {
        if (!isGui && huidigeFase != Fase.OPTIE2) {
            throw new IllegalStateException(Vertaling.tekst("exception.option2NotActive"));
        }

        List<KeuzeDTO> keuzes = new ArrayList<>();

        List<Dobbelsteen> witteDobbelstenen = new ArrayList<>();
        List<Dobbelsteen> gekleurdeDobbelstenen = new ArrayList<>();

        for (Dobbelsteen dobbelsteen : dobbelstenen) {
            if (dobbelsteen.getKleur() == Kleur.WIT) {
                witteDobbelstenen.add(dobbelsteen);
            } else {
                gekleurdeDobbelstenen.add(dobbelsteen);
            }
        }

        for (Dobbelsteen wit : witteDobbelstenen) {
            for (Dobbelsteen kleur : gekleurdeDobbelstenen) {
                keuzes.add(
                        new KeuzeDTO(
                                kleur.getKleur(),
                                wit.getAantalOgen() + kleur.getAantalOgen()
                        )
                );
            }
        }

        return keuzes;
    }
    /**
     * Beëindigt de optie 2-fase en maakt het spel klaar voor de volgende ronde.
     * De volgende speler wordt actief gezet, de optie 1-speleroffset wordt gereset
     * en de ronde wordt als niet gestart gemarkeerd.
     *
     * @throws IllegalStateException als optie 2 momenteel niet aan de beurt is
     */
    public void beeindigOptie2FaseEnGaNaarVolgendeRonde() {
        if (!isGui && huidigeFase != Fase.OPTIE2) {
            throw new IllegalStateException(Vertaling.tekst("exception.option2PhaseRequired"));
        }

        volgendeSpeler();
        optie1SpelerOffset = 0;
        huidigeFase = Fase.OPTIE1;
        rondeGestart = false;
    }
    /**
     * Zet de volgende speler als actieve speler.
     * Alle andere spelers worden op niet-actief gezet.
     */
    public void volgendeSpeler() {
        actieveSpelerIndex = (actieveSpelerIndex + 1) % spelers.size();

        for (int i = 0; i < spelers.size(); i++) {
            spelers.get(i).setActief(i == actieveSpelerIndex);
        }
    }
    /**
     * Geeft de actieve speler terug.
     *
     * @return de actieve speler
     */

    public Speler getActieveSpeler() {
        return spelers.get(actieveSpelerIndex);
    }
    /**
     * Geeft de index van de actieve speler terug.
     *
     * @return de index van de actieve speler
     */
    public int getActieveSpelerIndex() {
        return actieveSpelerIndex;
    }
    /**
     * Geeft de huidige fase van het spel terug.
     *
     * @return de huidige fase
     */
    public Fase getHuidigeFase() {
        return huidigeFase;
    }
    /**
     * Geeft terug of er momenteel een ronde gestart is.
     *
     * @return true als er een ronde gestart is, anders false
     */
    public boolean isRondeGestart() {
        return rondeGestart;
    }
    /**
     * Geeft een kopie van de lijst met dobbelstenen terug.
     *
     * @return een kopie van de dobbelstenenlijst
     */
    public List<Dobbelsteen> getDobbelstenen() {
        return new ArrayList<>(dobbelstenen);
    }
    /**
     * Geeft de index van de huidige optie 1-speler terug.
     *
     * @return de index van de huidige optie 1-speler
     * @throws IllegalStateException als er geen spelers geregistreerd zijn
     */
    public int getHuidigeOptie1SpelerIndex() {
        if (spelers.isEmpty()) {
            throw new IllegalStateException(Vertaling.tekst("exception.noPlayersRegistered"));
        }

        return (actieveSpelerIndex + optie1SpelerOffset) % spelers.size();
    }
    /**
     * Geeft de huidige optie 1-speler terug.
     *
     * @return de huidige optie 1-speler
     */
    public Speler getHuidigeOptie1Speler() {
        return spelers.get(getHuidigeOptie1SpelerIndex());
    }
    /**
     * Geeft een kopie van de lijst met spelers terug.
     *
     * @return een kopie van de spelerslijst
     */
    public List<Speler> getSpelers() {
        return new ArrayList<>(spelers);
    }
    /**
     * Controleert of het spel afgelopen is.
     * Het spel eindigt wanneer een speler minstens 4 mislukte worpen heeft
     * of wanneer minstens 2 rijen gesloten zijn.
     *
     * @return true als het spel afgelopen is, anders false
     */
    public boolean isEindeSpel() {
        for (Speler speler : spelers) {
            if (speler.getScoreblad().getMislukteWorpen() >= 4) {
                return true;
            }
        }

        int gesloten = 0;

        for (Speler speler : spelers) {
            for (Rij rij : speler.getScoreblad().getRijen()) {
                if (rij.isGesloten()) {
                    gesloten++;
                }
            }
        }

        return gesloten >= 2;
    }
    /**
     * Berekent de score van elke speler en slaat die op bij de speler.
     */
    public void berekenScores() {
        for (Speler speler : spelers) {
            speler.setScore(speler.getScoreblad().berekenScore());
        }
    }
    /**
     * Verhoogt het aantal mislukte worpen van de opgegeven speler.
     *
     * @param speler de speler waarvan het aantal mislukte worpen verhoogd wordt
     */
    public void verhoogMislukteWorpen(Speler speler) {
        speler.getScoreblad().verhoogMislukteWorpen();
    }
    /**
     * Bepaalt de winnaar of winnaars van het spel.
     * Alle spelers met de hoogste score worden toegevoegd aan de lijst met winnaars.
     *
     * @return een lijst met de winnaar of winnaars
     */
    public List<Speler> bepaalWinnaars() {
        List<Speler> winnaars = new ArrayList<>();
        int hoogste = Integer.MIN_VALUE;

        for (Speler speler : spelers) {
            if (speler.getScore() > hoogste) {
                hoogste = speler.getScore();
            }
        }

        for (Speler speler : spelers) {
            if (speler.getScore() == hoogste) {
                winnaars.add(speler);
            }
        }



        return winnaars;
    }
}
