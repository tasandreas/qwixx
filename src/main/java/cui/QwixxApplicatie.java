package cui;

import domein.*;
import dto.DobbelsteenDTO;
import dto.KeuzeDTO;

import java.time.Year;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Scanner;

public class QwixxApplicatie {
    private final Scanner scanner = new Scanner(System.in);
    private final DomeinController dc;
    private final ResourceBundle bundle;

    public QwixxApplicatie(DomeinController dc) {
        this.dc = dc;
        this.bundle = ResourceBundle.getBundle("messages");
    }

    public void run() {
        System.out.println(bundle.getString("app.welcome"));

        while (true) {
            System.out.print(bundle.getString("app.playQuestion"));
            String antwoord = scanner.nextLine();

            if (isJa(antwoord)) {
                startSpel();
                break;
            } else if (isNee(antwoord)) {
                System.out.println(bundle.getString("app.closing"));
                return;
            } else {
                System.out.println(bundle.getString("app.invalidInput"));
            }
        }
    }

    private void startSpel() {
        System.out.println(bundle.getString("game.starting"));
        spelersRegistreren();

        System.out.println(bundle.getString("game.chooseType"));
        System.out.println(bundle.getString("game.type.base"));
        System.out.println(bundle.getString("game.type.variant"));
        System.out.println(bundle.getString("game.type.random"));

        int gekozenVariant;
        try {
            gekozenVariant = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException exception) {
            System.out.println(bundle.getString("players.invalidNumber"));
            return;
        }

        switch (gekozenVariant) {
            case 1:
                dc.startSpel(SoortSpel.BASIS);
                break;
            case 2:
                dc.startSpel(SoortSpel.VARIANT);
                break;
            case 3:
                dc.startSpel(SoortSpel.RANDOM);
                break;
            default:
                System.out.println(bundle.getString("game.invalidType"));
                return;
        }

        System.out.println(bundle.getString("game.begins"));
        toonScorebladenVanAlleSpelers();

        while (!dc.isEindeSpel()) {
            speelRonde();
        }

        System.out.println(bundle.getString("game.ended"));
        System.out.println(bundle.getString("game.winners"));

        List<Speler> winnaars = dc.bepaalWinnaars();
        for (Speler speler : winnaars) {
            System.out.println(speler.getGebruikersnaam());
        }

        System.out.println(bundle.getString("scoreboard.finalScores"));
        for (Speler speler : dc.getSpelers()) {
            int score = speler.getScoreblad().berekenScore();
            System.out.println(speler.getGebruikersnaam() + ": " + score);
        }
    }

    private void speelRonde() {
        if (dc.isEindeSpel()) {
            return;
        }

        dc.startRonde();

        Speler actieveSpeler = dc.getActieveSpeler();

        System.out.println(bundle.getString("game.roundStarted"));
        System.out.println(tekst("console.turn", actieveSpeler.getGebruikersnaam()));

        System.out.println(bundle.getString("game.diceThrown"));
        List<DobbelsteenDTO> dobbelstenen = dc.infoGegooideDobbelstenen();
        for (DobbelsteenDTO dobbelsteen : dobbelstenen) {
            System.out.println(tekst("console.die", kleurNaam(dobbelsteen.kleur()), dobbelsteen.aantalOgen()));
        }

        boolean actieveSpelerHeeftOptie1Overgeslagen = speelOptie1VoorAlleSpelers();

        if (dc.isEindeSpel()) {
            return;
        }

        dc.beeindigOptie1Fase();

        speelOptie2VoorActieveSpeler(actieveSpelerHeeftOptie1Overgeslagen);

        if (dc.isEindeSpel()) {
            return;
        }

        toonScorebladenVanAlleSpelers();

        System.out.println(bundle.getString("game.nextRound"));
        scanner.nextLine();

        dc.beeindigOptie2FaseEnGaNaarVolgendeRonde();
    }

    private boolean speelOptie1VoorAlleSpelers() {
        int somWit = dc.geefOptie1Waarde();
        int actieveSpelerIndex = dc.getActieveSpelerIndex();
        boolean actieveSpelerHeeftOptie1Overgeslagen = false;

        System.out.println(bundle.getString("console.option1.title"));
        System.out.println(tekst("console.option1.whiteSum", somWit));

        List<Speler> spelers = dc.getSpelers();

        for (int i = 0; i < spelers.size(); i++) {
            Speler speler = spelers.get(i);
            boolean beurtAfgerond = false;

            while (!beurtAfgerond) {
                System.out.println(tekst("console.option1.playerTurn", speler.getGebruikersnaam()));
                System.out.println(bundle.getString("console.option1.mark"));
                System.out.println(bundle.getString("console.option0"));

                int keuze;
                try {
                    keuze = Integer.parseInt(scanner.nextLine());
                } catch (NumberFormatException exception) {
                    System.out.println(bundle.getString("players.invalidNumber"));
                    continue;
                }

                if (keuze == 0) {
                    if (i == actieveSpelerIndex) {
                        actieveSpelerHeeftOptie1Overgeslagen = true;
                    }
                    System.out.println(bundle.getString("game.doNothing"));
                    beurtAfgerond = true;
                } else if (keuze == 1) {
                    Kleur gekozenKleur = vraagKleur();

                    if (gekozenKleur == null) {
                        System.out.println(bundle.getString("game.invalidChoice"));
                        continue;
                    }

                    try {
                        dc.voegKruisjeToeVoorSpeler(i, gekozenKleur, somWit);
                        System.out.println(bundle.getString("game.boxChecked"));
                        beurtAfgerond = true;
                    } catch (Exception exception) {
                        System.out.println(exception.getMessage());
                    }
                } else {
                    System.out.println(bundle.getString("game.invalidChoice"));
                }
            }
        }

        return actieveSpelerHeeftOptie1Overgeslagen;
    }

    private void speelOptie2VoorActieveSpeler(boolean actieveSpelerHeeftOptie1Overgeslagen) {
        Speler actieveSpeler = dc.getActieveSpeler();
        List<KeuzeDTO> keuzes = dc.geefOptie2Keuzes();

        System.out.println(bundle.getString("console.option2.title"));
        System.out.println(tekst("console.option2.activeOnly", actieveSpeler.getGebruikersnaam()));
        System.out.println(bundle.getString("console.option2.chooseCombination"));
        System.out.println(bundle.getString("console.option0"));

        int keuze;
        try {
            keuze = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException exception) {
            System.out.println(bundle.getString("players.invalidNumber"));
            return;
        }

        if (keuze == 0) {
            if (actieveSpelerHeeftOptie1Overgeslagen) {
                dc.verhoogMislukteWorpen();
            }
            System.out.println(bundle.getString("game.doNothing"));

            if (dc.isEindeSpel()) {
                System.out.println(bundle.getString("game.finishedByMisses"));
            }
            return;
        }

        if (keuze == 2) {
            System.out.println(bundle.getString("combination.choose"));

            for (int i = 0; i < keuzes.size(); i++) {
                KeuzeDTO k = keuzes.get(i);
                System.out.println((i + 1) + ". " + kleurNaam(k.kleur()) + " " + k.waarde());
            }

            int index;
            try {
                index = Integer.parseInt(scanner.nextLine()) - 1;
            } catch (NumberFormatException exception) {
                System.out.println(bundle.getString("players.invalidNumber"));
                return;
            }

            if (index >= 0 && index < keuzes.size()) {
                KeuzeDTO gekozen = keuzes.get(index);

                try {
                    dc.voegKruisjeToe(gekozen.kleur(), gekozen.waarde());
                    System.out.println(bundle.getString("game.boxChecked"));
                } catch (Exception exception) {
                    System.out.println(exception.getMessage());
                }
            } else {
                System.out.println(bundle.getString("game.invalidChoice"));
            }
        } else {
            System.out.println(bundle.getString("game.invalidChoice"));
        }
    }

    private Kleur vraagKleur() {
        System.out.println(bundle.getString("game.chooseRow"));
        System.out.println(bundle.getString("row.red"));
        System.out.println(bundle.getString("row.yellow"));
        System.out.println(bundle.getString("row.green"));
        System.out.println(bundle.getString("row.blue"));

        int rijKeuze;
        try {
            rijKeuze = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException exception) {
            return null;
        }

        return switch (rijKeuze) {
            case 1 -> Kleur.ROOD;
            case 2 -> Kleur.GEEL;
            case 3 -> Kleur.GROEN;
            case 4 -> Kleur.BLAUW;
            default -> null;
        };
    }

    private void spelersRegistreren() {
        System.out.println(bundle.getString("players.registerInfo"));
        int aantalSpelers = 0;

        while (aantalSpelers < 5) {
            boolean geldigeSpeler = false;

            while (!geldigeSpeler) {
                System.out.println(bundle.getString("console.players.new"));
                System.out.println(bundle.getString("console.players.existing"));

                int keuze;
                try {
                    keuze = Integer.parseInt(scanner.nextLine());
                } catch (NumberFormatException exception) {
                    System.out.println(bundle.getString("players.invalidNumber"));
                    continue;
                }

                switch (keuze) {
                    case 1:
                        geldigeSpeler = registreerNieuweSpeler();
                        break;
                    case 2:
                        geldigeSpeler = kiesBestaandeSpeler();
                        break;
                    default:
                        System.out.println(bundle.getString("game.invalidChoice"));
                }
            }

            aantalSpelers++;

            if (aantalSpelers >= 2 && aantalSpelers < 5) {
                System.out.println(bundle.getString("players.registerMore"));
                String antwoord = scanner.nextLine();

                if (isNee(antwoord)) {
                    break;
                }
            }
        }

        if (aantalSpelers == 5) {
            System.out.println(bundle.getString("players.maximum"));
        }
    }

    private boolean registreerNieuweSpeler() {
        System.out.println(bundle.getString("players.enterUsername"));
        String gebruikersnaam = scanner.nextLine();
        int geboortejaar;

        while (true) {
            System.out.println(bundle.getString("players.enterBirthYear"));

            try {
                geboortejaar = Integer.parseInt(scanner.nextLine());

                if (geboortejaar < Year.now().getValue() - 99 || geboortejaar > Year.now().getValue() - 5) {
                    throw new IllegalArgumentException(bundle.getString("players.invalidAge"));
                }

                break;
            } catch (NumberFormatException exception) {
                System.out.println(bundle.getString("players.invalidNumber"));
            } catch (IllegalArgumentException exception) {
                System.out.println(exception.getMessage());
            }
        }

        try {
            dc.registreerSpeler(gebruikersnaam, geboortejaar);
            System.out.println(bundle.getString("players.added"));
            return true;
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            return false;
        }
    }

    private boolean kiesBestaandeSpeler() {
        List<Speler> geregistreerdeSpelers = dc.geefGeregistreerdeSpelers();

        if (geregistreerdeSpelers.isEmpty()) {
            System.out.println(bundle.getString("console.players.none"));
            return false;
        }

        System.out.println(bundle.getString("console.players.list"));
        for (int i = 0; i < geregistreerdeSpelers.size(); i++) {
            System.out.println((i + 1) + ". " + geregistreerdeSpelers.get(i).getGebruikersnaam());

        }

        System.out.println(bundle.getString("console.players.chooseNumber"));
        int keuze;

        try {
            keuze = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException exception) {
            System.out.println(bundle.getString("players.invalidNumber"));
            return false;
        }

        if (keuze < 1 || keuze > geregistreerdeSpelers.size()) {
            System.out.println(bundle.getString("game.invalidChoice"));
            return false;
        }

        String gebruikersnaam = geregistreerdeSpelers.get(keuze - 1).getGebruikersnaam();

        try {
            dc.voegBestaandeSpelerToe(gebruikersnaam);
            System.out.println(bundle.getString("players.added"));
            return true;
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            return false;
        }
    }

    private void toonScorebladenVanAlleSpelers() {
        System.out.println(bundle.getString("scoreboard.title"));
        System.out.println(tekst("scoreboard.gameType", soortSpelNaam()));

        for (Speler speler : dc.getSpelers()) {
            System.out.println(tekst("console.scoreboard.player", speler.getGebruikersnaam()));

            List<Rij> rijen = speler.getScoreblad().getRijen();
            for (Rij rij : rijen) {
                System.out.println(formatteerScoreRij(rij));
            }

            System.out.printf(bundle.getString("scoreboard.missedThrows"),
                    dc.getMislukteWorpen(speler));
            System.out.println(bundle.getString("scoreboard.end"));

            int score = speler.getScoreblad().berekenScore();
            System.out.println(tekst("console.scoreboard.score", score));
        }
    }

    private String formatteerScoreRij(Rij rij) {
        StringBuilder waarden = new StringBuilder();
        StringBuilder kruisen = new StringBuilder();

        for (int waarde : rij.getWaarden()) {
            waarden.append(String.format("%3d", waarde));
            kruisen.append(String.format("%3s", rij.getAangekruist().contains(waarde) ? "X" : "-"));
        }

        return String.format("%-6s | %s%n       | %s | %s: %s",
                kleurNaam(rij.getKleur()),
                waarden,
                kruisen,
                bundle.getString("scoreboard.closed"),
                rij.isGesloten() ? bundle.getString("scoreboard.yes") : bundle.getString("scoreboard.no"));
    }

    private String soortSpelNaam() {
        return switch (dc.getSoortSpel()) {
            case BASIS -> bundle.getString("gui.gameType.base");
            case VARIANT -> bundle.getString("gui.gameType.variant");
            case RANDOM -> bundle.getString("gui.gameType.random");
        };
    }

    private boolean isJa(String antwoord) {
        return antwoord.equalsIgnoreCase("y") || antwoord.equalsIgnoreCase("j");
    }

    private boolean isNee(String antwoord) {
        return antwoord.equalsIgnoreCase("n");
    }

    private String kleurNaam(Kleur kleur) {
        return switch (kleur) {
            case ROOD -> bundle.getString("color.red");
            case GEEL -> bundle.getString("color.yellow");
            case GROEN -> bundle.getString("color.green");
            case BLAUW -> bundle.getString("color.blue");
            case WIT -> bundle.getString("color.white");
        };
    }

    private String tekst(String key, Object... args) {
        return String.format(bundle.getString(key), args);
    }
}
