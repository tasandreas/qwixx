package gui;

import domein.DomeinController;
import domein.Speler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class EindeSpelScherm extends VBox {

    private final DomeinController dc;
    private final SchermController sc;
    private final ResourceBundle bundle;

    public EindeSpelScherm(DomeinController dc, SchermController sc) {
        this.dc = dc;
        this.sc = sc;
        this.bundle = ResourceBundle.getBundle("messages", Locale.getDefault());
        buildGui();
    }

    private void buildGui() {
        setPadding(new Insets(30));
        setSpacing(18);
        setAlignment(Pos.TOP_CENTER);

        List<Speler> winnaars = dc.bepaalWinnaars();
        List<Speler> spelers = dc.getSpelers().stream()
                .sorted(Comparator.comparingInt(Speler::getScore).reversed())
                .toList();

        Label lblTitel = new Label(bundle.getString("end.title"));
        lblTitel.getStyleClass().add("label-titel");

        Label lblWinnaars = new Label(maakWinnaarsTekst(winnaars));
        lblWinnaars.getStyleClass().add("end-summary");

        HBox header = new HBox(20);
        header.setAlignment(Pos.CENTER);
        header.getStyleClass().add("table-header-line");

        Label hNaam = new Label(bundle.getString("end.name"));
        hNaam.setMinWidth(180);
        Label hPunten = new Label(bundle.getString("end.points"));
        hPunten.setMinWidth(100);
        Label hStatus = new Label(bundle.getString("end.status"));
        hStatus.setMinWidth(140);

        hNaam.getStyleClass().add("table-header-label");
        hPunten.getStyleClass().add("table-header-label");
        hStatus.getStyleClass().add("table-header-label");

        header.getChildren().addAll(hNaam, hPunten, hStatus);

        VBox spelersBox = new VBox(10);
        spelersBox.setAlignment(Pos.CENTER);

        for (Speler speler : spelers) {
            HBox rij = new HBox(20);
            rij.setAlignment(Pos.CENTER);
            rij.getStyleClass().add("player-stats-row");

            Label naam = new Label(speler.getGebruikersnaam());
            naam.setMinWidth(180);
            naam.getStyleClass().add("player-stats-label");

            Label punten = new Label(String.valueOf(speler.getScore()));
            punten.setMinWidth(100);
            punten.getStyleClass().add("player-stats-label");

            String statusTekst = winnaars.contains(speler)
                    ? bundle.getString("end.winnerLabel")
                    : bundle.getString("end.playerLabel");
            Label status = new Label(statusTekst);
            status.setMinWidth(140);
            status.getStyleClass().add("player-stats-label");

            if (winnaars.contains(speler)) {
                status.getStyleClass().add("winner-status");
            }

            rij.getChildren().addAll(naam, punten, status);
            spelersBox.getChildren().add(rij);
        }

        Button btnNieuwSpel = new Button(bundle.getString("end.samePlayers"));
        btnNieuwSpel.getStyleClass().add("button-start");
        btnNieuwSpel.setOnAction(e -> startNieuwSpelMetDezelfdeSpelers());

        Button btnTerug = new Button(bundle.getString("end.backToStart"));
        btnTerug.setOnAction(e -> sc.toonStartScherm());

        getChildren().addAll(
                lblTitel,
                lblWinnaars,
                header,
                spelersBox,
                btnNieuwSpel,
                btnTerug
        );
    }

    private String maakWinnaarsTekst(List<Speler> winnaars) {
        if (winnaars.isEmpty()) {
            return bundle.getString("game.noWinnersLabel");
        }

        String label = winnaars.size() == 1
                ? bundle.getString("game.winnerLabel")
                : bundle.getString("game.winnersLabel");
        String namen = winnaars.stream()
                .map(Speler::getGebruikersnaam)
                .collect(Collectors.joining(", "));

        return label + namen;
    }

    private void startNieuwSpelMetDezelfdeSpelers() {
        dc.startSpel(dc.getSoortSpel());
        sc.toonBeginSpelScherm();
    }
}
