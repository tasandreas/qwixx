package gui;

import domein.DomeinController;
import domein.Speler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

    public class SpelersOverzichtScherm extends VBox {

        private final DomeinController dc;
        private final SchermController sc;
        private final ResourceBundle bundle;

        public SpelersOverzichtScherm(DomeinController dc, SchermController sc) {
            this.dc = dc;
            this.sc = sc;
            this.bundle = ResourceBundle.getBundle("messages", Locale.getDefault());
            buildGui();
        }

        private void buildGui() {
            this.setPadding(new Insets(30));
            this.setSpacing(15);
            this.setAlignment(Pos.TOP_CENTER);

            Label lblTitel = new Label(bundle.getString("overview.title"));
            lblTitel.getStyleClass().add("label-titel");

            // Koptekst rij
            HBox header = new HBox(20);
            header.setAlignment(Pos.CENTER);
            header.getStyleClass().add("table-header-line");

            Label hNaam = new Label(bundle.getString("overview.name"));
            hNaam.setMinWidth(150);
            Label hGespeeld = new Label(bundle.getString("overview.played"));
            hGespeeld.setMinWidth(100);
            Label hGewonnen = new Label(bundle.getString("overview.won"));
            hGewonnen.setMinWidth(100);
            Label hRatio = new Label(bundle.getString("overview.ratio"));
            hRatio.setMinWidth(100);

            hNaam.getStyleClass().add("table-header-label");
            hGespeeld.getStyleClass().add("table-header-label");
            hGewonnen.getStyleClass().add("table-header-label");
            hRatio.getStyleClass().add("table-header-label");

            header.getChildren().addAll(hNaam, hGespeeld, hGewonnen, hRatio);

            VBox spelersBox = new VBox(8);
            spelersBox.setAlignment(Pos.CENTER);

            List<Speler> spelers = dc.getSpelersVoorNieuwSpel();
            for (Speler speler : spelers) {
                HBox rij = new HBox(20);
                rij.setAlignment(Pos.CENTER);
                rij.getStyleClass().add("player-stats-row");

                Label naam = new Label(speler.getGebruikersnaam());
                naam.setMinWidth(150);
                naam.getStyleClass().add("player-stats-label");

                Label gespeeld = new Label(String.valueOf(speler.getAantalGespeeld()));
                gespeeld.setMinWidth(100);
                gespeeld.getStyleClass().add("player-stats-label");

                Label gewonnen = new Label(String.valueOf(speler.getAantalGewonnen()));
                gewonnen.setMinWidth(100);
                gewonnen.getStyleClass().add("player-stats-label");

                // Winratio berekenen
                String ratio;
                if (speler.getAantalGespeeld() == 0) {
                    ratio = "-";
                } else {
                    int procent = (int) ((speler.getAantalGewonnen() * 100.0) / speler.getAantalGespeeld());
                    ratio = procent + "%";
                }
                Label lblRatio = new Label(ratio);
                lblRatio.setMinWidth(100);
                lblRatio.getStyleClass().add("player-stats-label");

                rij.getChildren().addAll(naam, gespeeld, gewonnen, lblRatio);
                spelersBox.getChildren().add(rij);
            }

            Button btnDoorgaan = new Button(bundle.getString("overview.continue"));
            btnDoorgaan.getStyleClass().add("button-start");
            btnDoorgaan.setOnAction(e -> sc.toonBeginSpelScherm());

            Button btnTerug = new Button(bundle.getString("overview.back"));
            btnTerug.setOnAction(e -> sc.toonStartScherm());

            this.getChildren().addAll(lblTitel, header, spelersBox, btnDoorgaan, btnTerug);
        }
    }
