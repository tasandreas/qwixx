package gui;

import domein.DomeinController;
import domein.Fase;
import domein.Kleur;
import domein.Rij;
import domein.Speler;
import dto.DobbelsteenDTO;
import dto.KeuzeDTO;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class SpelScherm extends VBox {

    private final DomeinController dc;
    private final SchermController sc;
    private final ResourceBundle bundle;

    private Label lblTitel;
    private Label lblActieveSpeler;
    private Label lblFase;
    private Label lblSoortSpel;
    private HBox dobbelstenenBox;
    private VBox actieBox;
    private VBox scorebladBox;
    private VBox spelersScoreBox;
    private Button btnRolDobbelstenen;
    private Button btnNiets;
    private Button btnTerugNaarMenu;

    private boolean rondeGestart;
    private boolean actieveSpelerHeeftOptie1Overgeslagen;

    public SpelScherm(DomeinController dc, SchermController sc) {
        this.dc = dc;
        this.sc = sc;
        this.bundle = ResourceBundle.getBundle("messages", Locale.getDefault());
        buildGui();
        updateScherm();
    }

    private void buildGui() {
        setSpacing(15);
        setPadding(new Insets(20));
        setAlignment(Pos.TOP_LEFT);
        setFillWidth(true);


        lblTitel = new Label(bundle.getString("gui.title"));
        lblTitel.setStyle("-fx-font-size: 26px; -fx-font-weight: bold;");

        lblActieveSpeler = new Label();
        lblFase = new Label();
        lblSoortSpel = new Label();

        HBox header = new HBox(20, lblActieveSpeler, lblFase, lblSoortSpel);
        header.setAlignment(Pos.CENTER);

        lblSoortSpel.getStyleClass().add("label-speltype");

        btnRolDobbelstenen = new Button(bundle.getString("game.rollDice"));
        btnRolDobbelstenen.setOnAction(e -> rolDobbelstenen());

        btnNiets = new Button(bundle.getString("game.option0"));
        btnNiets.setOnAction(e -> slaOptieOver());

        dobbelstenenBox = new HBox(10);
        dobbelstenenBox.setAlignment(Pos.CENTER);

        actieBox = new VBox(10);
        actieBox.setAlignment(Pos.CENTER);

        scorebladBox = new VBox(12);
        scorebladBox.setAlignment(Pos.CENTER);

        ScrollPane scorebladScroll = new ScrollPane(scorebladBox);
        scorebladScroll.setFitToWidth(true);
        scorebladScroll.setPrefViewportHeight(300);
        scorebladScroll.setMaxWidth(900);
        scorebladScroll.getStyleClass().add("scoreblad-scroll");

        spelersScoreBox = new VBox(10);
        spelersScoreBox.setPadding(new Insets(15));
        spelersScoreBox.getStyleClass().add("side-panel-card");
        spelersScoreBox.setMinWidth(310);
        spelersScoreBox.setMaxWidth(310);
        spelersScoreBox.setAlignment(Pos.CENTER_LEFT);

        btnTerugNaarMenu = new Button(bundle.getString("game.backToMenu"));
        btnTerugNaarMenu.setOnAction(e -> sc.toonStartScherm());

        VBox middenKolom = new VBox(15,
                lblTitel,
                header,
                btnRolDobbelstenen,
                dobbelstenenBox,
                actieBox,
                btnNiets,
                scorebladScroll,
                btnTerugNaarMenu
        );
        middenKolom.setAlignment(Pos.TOP_CENTER);
        middenKolom.setMaxWidth(820);

        VBox linkerKolom = new VBox(spelersScoreBox);
        linkerKolom.setMinWidth(340);
        linkerKolom.setPrefWidth(340);
        linkerKolom.setMaxWidth(340);
        linkerKolom.setAlignment(Pos.CENTER_LEFT);

        StackPane gecentreerdeMiddenKolom = new StackPane(middenKolom);
        gecentreerdeMiddenKolom.setAlignment(Pos.TOP_CENTER);
        gecentreerdeMiddenKolom.setPrefWidth(Double.MAX_VALUE);
        gecentreerdeMiddenKolom.setMaxWidth(Double.MAX_VALUE);
        gecentreerdeMiddenKolom.setPrefHeight(620);

        AnchorPane linkerLaag = new AnchorPane(linkerKolom);
        linkerLaag.setPickOnBounds(false);
        linkerLaag.setPrefWidth(Double.MAX_VALUE);
        linkerLaag.setMaxWidth(Double.MAX_VALUE);
        linkerLaag.setPrefHeight(620);
        AnchorPane.setLeftAnchor(linkerKolom, 0.0);
        AnchorPane.setTopAnchor(linkerKolom, 0.0);
        AnchorPane.setBottomAnchor(linkerKolom, 0.0);

        StackPane hoofdLayout = new StackPane(gecentreerdeMiddenKolom, linkerLaag);
        hoofdLayout.setAlignment(Pos.TOP_CENTER);
        hoofdLayout.setPrefWidth(Double.MAX_VALUE);
        hoofdLayout.setMaxWidth(Double.MAX_VALUE);
        hoofdLayout.setPrefHeight(620);

        getChildren().add(hoofdLayout);
    }

    private void updateSpelersScorebord() {
        spelersScoreBox.getChildren().clear();

        Label lblOverzicht = new Label(bundle.getString("end.points"));
        lblOverzicht.getStyleClass().add("panel-title");

        HBox header = new HBox(12);
        header.setAlignment(Pos.CENTER_LEFT);
        header.getStyleClass().add("table-header-line");

        Label lblNaamHeader = new Label(bundle.getString("end.name"));
        lblNaamHeader.setMinWidth(120);
        lblNaamHeader.getStyleClass().add("table-header-label");

        Label lblPuntenHeader = new Label(bundle.getString("end.points"));
        lblPuntenHeader.getStyleClass().add("table-header-label");

        header.getChildren().addAll(lblNaamHeader, lblPuntenHeader);
        spelersScoreBox.getChildren().addAll(lblOverzicht, header);

        Speler gemarkeerdeSpeler = dc.getHuidigeFase() == Fase.OPTIE1 && rondeGestart
                ? dc.getHuidigeOptie1Speler()
                : dc.getActieveSpeler();

        List<Speler> spelers = dc.getSpelers();
        for (int i = 0; i < spelers.size(); i++) {
            Speler speler = spelers.get(i);
            HBox rij = new HBox(12);
            rij.setAlignment(Pos.CENTER_LEFT);
            rij.setPadding(new Insets(8));

            boolean isActieveSpeler = speler.equals(gemarkeerdeSpeler);
            if (isActieveSpeler) {
                rij.getStyleClass().add("player-stats-row-active");
            } else {
                rij.getStyleClass().add("player-stats-row");
            }

            Label lblNaam = new Label(speler.getGebruikersnaam());
            lblNaam.setMinWidth(120);
            lblNaam.getStyleClass().add(isActieveSpeler ? "player-stats-label-active" : "player-stats-label");

            Label lblPunten = new Label(String.valueOf(speler.getScoreblad().berekenScore()));
            lblPunten.getStyleClass().add(isActieveSpeler ? "player-stats-label-active" : "player-stats-label");

            rij.getChildren().addAll(lblNaam, lblPunten);
            spelersScoreBox.getChildren().add(rij);
            if (!isActieveSpeler) {
                VBox miniScoreblad = new VBox(4);
                miniScoreblad.setPadding(new Insets(8));
                miniScoreblad.setMinWidth(270);
                miniScoreblad.setMaxWidth(270);
                miniScoreblad.getStyleClass().add("mini-scoreblad");

                Label lblNaamMini = new Label(speler.getGebruikersnaam());
                lblNaamMini.setStyle("-fx-font-size: 11px; -fx-font-weight: bold; -fx-text-fill: white;");
                miniScoreblad.getChildren().add(lblNaamMini);

                for (Rij scoreRij : dc.getRijenVoorSpeler(i)) {
                    HBox miniRijBox = new HBox(2);
                    miniRijBox.setAlignment(Pos.CENTER_LEFT);

                    Label lblKleur = new Label(kleurNaam(scoreRij.getKleur()));
                    lblKleur.setMinWidth(45);
                    lblKleur.setStyle("-fx-font-size: 9px; -fx-font-weight: bold;");
                    switch (scoreRij.getKleur()) {
                        case ROOD -> lblKleur.setTextFill(Color.RED);
                        case GEEL -> lblKleur.setTextFill(Color.GOLDENROD);
                        case GROEN -> lblKleur.setTextFill(Color.GREEN);
                        case BLAUW -> lblKleur.setTextFill(Color.CORNFLOWERBLUE);
                        default -> {}
                    }
                    miniRijBox.getChildren().add(lblKleur);

                    for (int waarde : scoreRij.getWaarden()) {
                        boolean aangekruist = scoreRij.getAangekruist().contains(waarde);
                        Label vakje = new Label(aangekruist ? "X" : String.valueOf(waarde));
                        vakje.setMinWidth(14);
                        vakje.setMinHeight(14);
                        vakje.setAlignment(Pos.CENTER);

                        String bgKleur = switch (scoreRij.getKleur()) {
                            case ROOD -> aangekruist ? "#7b1a1a" : "#e74c3c";
                            case GEEL -> aangekruist ? "#7a6a00" : "#f4d03f";
                            case GROEN -> aangekruist ? "#1a5c1a" : "#2ecc71";
                            case BLAUW -> aangekruist ? "#1a3a7a" : "#3498db";
                            default -> "#555";
                        };
                        String tekstKleur = (scoreRij.getKleur() == Kleur.GEEL && !aangekruist) ? "black" : "white";

                        vakje.setStyle(
                                "-fx-font-size: 8px; -fx-font-weight: bold;" +
                                        "-fx-background-color: " + bgKleur + ";" +
                                        "-fx-text-fill: " + tekstKleur + ";" +
                                        "-fx-background-radius: 3;"
                        );
                        miniRijBox.getChildren().add(vakje);
                    }

                    miniScoreblad.getChildren().add(miniRijBox);
                }

                Label lblMislukt = new Label(bundle.getString("game.failedThrows") + " " + speler.getMislukteWorpen());
                lblMislukt.setStyle("-fx-font-size: 10px; -fx-text-fill: #ff6b6b;");
                miniScoreblad.getChildren().add(lblMislukt);

                spelersScoreBox.getChildren().add(miniScoreblad);
            }
        }
    }

    private void updateScherm() {
        Speler actieveSpeler = dc.getActieveSpeler();
        lblActieveSpeler.setText(bundle.getString("game.turn") + " " + actieveSpeler.getGebruikersnaam());

        lblSoortSpel.setText(getSoortSpelTekst());

        if (!rondeGestart) {
            lblFase.setText(bundle.getString("game.waitForRoll"));
        } else if (dc.getHuidigeFase() == Fase.OPTIE1) {
            lblFase.setText(bundle.getString("game.phase") + " " + faseNaam(Fase.OPTIE1)
                    + " - " + dc.getHuidigeOptie1Speler().getGebruikersnaam());
        } else {
            lblFase.setText(bundle.getString("game.phase") + " " + faseNaam(Fase.OPTIE2)
                    + " - " + actieveSpeler.getGebruikersnaam());
        }

        btnRolDobbelstenen.setDisable(rondeGestart);
        btnNiets.setDisable(!rondeGestart);

        updateSpelersScorebord();
        updateDobbelstenen();
        updateActies();
        updateScoreblad();
    }

    private void updateDobbelstenen() {
        dobbelstenenBox.getChildren().clear();

        if (!rondeGestart) {
            dobbelstenenBox.getChildren().add(new Label(bundle.getString("game.rollToStart")));
            return;
        }

        for (DobbelsteenDTO dobbelsteen : dc.infoGegooideDobbelstenen()) {
            Label dobbelLabel = new Label(String.valueOf(dobbelsteen.aantalOgen()));
            dobbelLabel.setMinSize(50, 50);
            dobbelLabel.setAlignment(Pos.CENTER);
            Color tekstKleur = tekstKleur(dobbelsteen.kleur());
            String tekstKleurCss = String.format("#%02x%02x%02x",
                    (int)(tekstKleur.getRed() * 255),
                    (int)(tekstKleur.getGreen() * 255),
                    (int)(tekstKleur.getBlue() * 255));

            dobbelLabel.setStyle(
                    "-fx-border-color: black;" +
                            "-fx-border-width: 2;" +
                            "-fx-font-size: 20px;" +
                            "-fx-font-weight: bold;" +
                            "-fx-background-color: " + cssKleur(dobbelsteen.kleur()) + ";" +
                            "-fx-text-fill: " + tekstKleurCss + ";"
            );
            dobbelLabel.setTextFill(tekstKleur(dobbelsteen.kleur()));
            dobbelstenenBox.getChildren().add(dobbelLabel);
        }
    }

    private void updateActies() {
        actieBox.getChildren().clear();

        if (!rondeGestart) {
            return;
        }

        if (dc.getHuidigeFase() == Fase.OPTIE1) {
            toonOptie1Acties();
        } else {
            toonOptie2Acties();
        }
    }

    private void toonOptie1Acties() {
        Speler speler = dc.getHuidigeOptie1Speler();
        int spelerIndex = dc.getHuidigeOptie1SpelerIndex();
        int waarde = dc.geefOptie1Waarde();
        Label uitleg = new Label(bundle.getString("game.option1.current") + " " + waarde + " - " + speler.getGebruikersnaam());
        HBox knoppen = new HBox(8);
        knoppen.setAlignment(Pos.CENTER);

        for (Rij rij : dc.getRijenVoorSpeler(spelerIndex)) {
            if (bevatWaarde(rij, waarde) && rij.kanKruisAan(waarde)) {
                Button knop = new Button(rij.getKleur() + " " + waarde);
                knop.setOnAction(e -> voerOptie1Uit(spelerIndex, rij.getKleur(), waarde));
                knoppen.getChildren().add(knop);
            }
        }

        actieBox.getChildren().addAll(uitleg, knoppen);
    }

    private void toonOptie2Acties() {
        Label uitleg = new Label(bundle.getString("game.option2.current"));
        HBox knoppen = new HBox(8);
        knoppen.setAlignment(Pos.CENTER);

        for (KeuzeDTO keuze : dc.geefOptie2Keuzes()) {
            Rij rij = dc.getRijenActieveSpeler().stream()
                    .filter(r -> r.getKleur() == keuze.kleur())
                    .findFirst()
                    .orElse(null);
            if (rij != null && rij.kanKruisAan(keuze.waarde())) {
                Button knop = new Button(keuze.kleur() + " " + keuze.waarde());
                knop.setOnAction(e -> voerOptie2Uit(keuze.kleur(), keuze.waarde()));
                knoppen.getChildren().add(knop);
            }
        }

        actieBox.getChildren().addAll(uitleg, knoppen);
    }

    private void updateScoreblad() {
        scorebladBox.getChildren().clear();

        Speler speler = dc.getHuidigeFase() == Fase.OPTIE1 && rondeGestart
                ? dc.getHuidigeOptie1Speler()
                : dc.getActieveSpeler();
        int spelerIndex = dc.getHuidigeFase() == Fase.OPTIE1 && rondeGestart
                ? dc.getHuidigeOptie1SpelerIndex()
                : dc.getActieveSpelerIndex();
        String soortSpelTekst = getSoortSpelTekst();
        if (dc.getSoortSpel() == domein.SoortSpel.VARIANT) {
            scorebladBox.getStyleClass().remove("scoreblad-basis");
            if (!scorebladBox.getStyleClass().contains("scoreblad-variant")) {
                scorebladBox.getStyleClass().add("scoreblad-variant");
            }
        } else {
            scorebladBox.getStyleClass().remove("scoreblad-variant");
            if (!scorebladBox.getStyleClass().contains("scoreblad-basis")) {
                scorebladBox.getStyleClass().add("scoreblad-basis");
            }
        }

        Label info = new Label(
                bundle.getString("game.scoreboardOf") + " " + speler.getGebruikersnaam()
                        + " | " + bundle.getString("game.failedThrows") + " " + speler.getMislukteWorpen()
                        + " | " + soortSpelTekst
        );
        info.getStyleClass().add("scoreblad-info");
        scorebladBox.getChildren().add(info);

        for (Rij rij : dc.getRijenVoorSpeler(spelerIndex)) {
            HBox rijBox = new HBox(6);
            rijBox.setAlignment(Pos.CENTER);
            rijBox.getStyleClass().add("scoreblad-rij");

            Label kleurLabel = new Label(kleurNaam(rij.getKleur()));
            kleurLabel.setMinWidth(70);
            kleurLabel.setTextFill(tekstKleurVoorRij(rij.getKleur()));
            kleurLabel.getStyleClass().add("kleur-label");

            rijBox.getChildren().add(kleurLabel);

            for (int waarde : rij.getWaarden()) {
                Label vakje = new Label(rij.getAangekruist().contains(waarde) ? "X" : String.valueOf(waarde));
                vakje.setMinSize(34, 28);
                vakje.setAlignment(Pos.CENTER);
                if (rij.getAangekruist().contains(waarde)) {
                    vakje.getStyleClass().add("scoreblad-vakje-aangekruist");
                } else {
                    vakje.getStyleClass().add("scoreblad-vakje");
                }

                switch (rij.getKleur()) {
                    case ROOD -> vakje.getStyleClass().add("vakje-rood");
                    case GEEL -> vakje.getStyleClass().add("vakje-geel");
                    case GROEN -> vakje.getStyleClass().add("vakje-groen");
                    case BLAUW -> vakje.getStyleClass().add("vakje-blauw");
                }

                rijBox.getChildren().add(vakje);
            }

            Label slotLabel = new Label(rij.isGesloten() ? "🔒" : "🔓");
            slotLabel.setMinWidth(40);
            slotLabel.setAlignment(Pos.CENTER);
            slotLabel.getStyleClass().add("slot-label");

            rijBox.getChildren().add(slotLabel);

            scorebladBox.getChildren().add(rijBox);
        }
    }

    private void rolDobbelstenen() {
        try {
            dc.startRonde();
            GeluidManager.speelDobbelstenenRollen();
            rondeGestart = true;
            actieveSpelerHeeftOptie1Overgeslagen = false;
            updateScherm();
        } catch (RuntimeException ex) {
            toonFout(ex.getMessage());
        }
    }

    private void voerOptie1Uit(int spelerIndex, Kleur kleur, int waarde) {
        try {
            dc.voegKruisjeToeVoorSpeler(spelerIndex, kleur, waarde);
            GeluidManager.speelDobbelsteenSelecteren();
            dc.beeindigBeurtVanHuidigeOptie1Speler();
            updateScherm();
        } catch (RuntimeException ex) {
            toonFout(ex.getMessage());
        }
    }

    private void voerOptie2Uit(Kleur kleur, int waarde) {
        try {
            dc.voegKruisjeToe(kleur, waarde);
            GeluidManager.speelDobbelsteenSelecteren();
            beeindigRonde();
        } catch (RuntimeException ex) {
            toonFout(ex.getMessage());
        }
    }

    private void slaOptieOver() {
        try {
            if (dc.getHuidigeFase() == Fase.OPTIE1) {
                if (dc.getHuidigeOptie1SpelerIndex() == dc.getActieveSpelerIndex()) {
                    actieveSpelerHeeftOptie1Overgeslagen = true;
                }
                dc.beeindigBeurtVanHuidigeOptie1Speler();
                updateScherm();
            } else {
                if (actieveSpelerHeeftOptie1Overgeslagen) {
                    dc.verhoogMislukteWorpen();
                }
                beeindigRonde();
            }
        } catch (RuntimeException ex) {
            toonFout(ex.getMessage());
        }
    }

    private void beeindigRonde() {
        dc.beeindigOptie2FaseEnGaNaarVolgendeRonde();
        rondeGestart = false;
        actieveSpelerHeeftOptie1Overgeslagen = false;

        if (dc.isEindeSpel()) {
            dc.berekenScores();
            dc.verhoogAantalGespeeldVoorAlleSpelers();
            List<Speler> winnaars = dc.bepaalWinnaars();
            dc.verhoogAantalGewonnenVoorWinnaars(winnaars);
            GeluidManager.speelSpelGewonnen();
            sc.toonEindeSpelScherm();
            return;
        }

        updateScherm();
    }

    private boolean bevatWaarde(Rij rij, int waarde) {
        for (int rijWaarde : rij.getWaarden()) {
            if (rijWaarde == waarde) {
                return true;
            }
        }
        return false;
    }

    private void toonFout(String boodschap) {
        GeluidManager.speelOngeldigeActie();
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(bundle.getString("game.invalidMove"));
        alert.setHeaderText(null);
        alert.setContentText(boodschap);
        alert.showAndWait();
    }

    private void toonInfo(String titel, String boodschap) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titel);
        alert.setHeaderText(null);
        alert.setContentText(boodschap);
        alert.showAndWait();
    }

    private String cssKleur(Kleur kleur) {
        return switch (kleur) {
            case ROOD -> "#e74c3c";
            case GEEL -> "#f4d03f";
            case GROEN -> "#2ecc71";
            case BLAUW -> "#3498db";
            case WIT -> "white";
        };
    }

    private Color tekstKleur(Kleur kleur) {
        return kleur == Kleur.WIT || kleur == Kleur.GEEL ? Color.BLACK : Color.WHITE;
    }

    private Color tekstKleurVoorRij(Kleur kleur) {
        return switch (kleur) {
            case ROOD -> Color.RED;
            case GEEL -> Color.GOLDENROD;
            case GROEN -> Color.GREEN;
            case BLAUW -> Color.BLUE;
            case WIT -> Color.BLACK;
        };
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

    private String faseNaam(Fase fase) {
        return switch (fase) {
            case OPTIE1 -> bundle.getString("game.phase.option1");
            case OPTIE2 -> bundle.getString("game.phase.option2");
        };
    }

    private String getSoortSpelTekst() {
        return switch (dc.getSoortSpel()) {
            case BASIS -> bundle.getString("game.type.base");
            case VARIANT -> bundle.getString("game.type.variant");
            case RANDOM -> bundle.getString("game.type.random");
        };
    }
}
