/// klasse wordt niet gebruikt
/*package gui;

import domein.DomeinController;
import domein.Kleur;
import domein.Rij;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.List;

public class SpeelScherm extends VBox {

    private final DomeinController dc;
    private final Stage stage;

    private VBox scorebladBox;
    private Label lblSpeler;

    public SpeelScherm(DomeinController dc, Stage stage) {
        this.dc = dc;
        this.stage = stage;
        buildGui();
        updateScherm();
    }

    private void buildGui() {
        this.setPadding(new Insets(20));
        this.setSpacing(15);
        this.setAlignment(Pos.TOP_CENTER);

        Label titel = new Label("Qwixx - Spel");
        titel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        lblSpeler = new Label();
        lblSpeler.setStyle("-fx-font-size: 16px;");

        Button btnRol = new Button("Rol dobbelstenen");
        btnRol.setOnAction(e -> {
            dc.startRonde();
            GeluidManager.speelDobbelstenenRollen();
            updateScherm();
        });

        Button btnMislukt = new Button("Mislukte worp");
        btnMislukt.setOnAction(e -> {
            dc.verhoogMislukteWorpen();
          //TODO  dc.volgendeSpeler();
            updateScherm();
        });

        scorebladBox = new VBox(10);

        this.getChildren().addAll(titel, lblSpeler, btnRol, btnMislukt, scorebladBox);
    }

    private void updateScherm() {
        lblSpeler.setText("Huidige speler: " + dc.geefHuidigeSpeler().getGebruikersnaam());

        scorebladBox.getChildren().clear();

        List<Rij> rijen = dc.getRijenActieveSpeler();

        for (Rij rij : rijen) {
            HBox rijBox = new HBox(5);
            rijBox.setAlignment(Pos.CENTER_LEFT);

            Label lblKleur = new Label(rij.getKleur().toString());
            lblKleur.setMinWidth(60);

            switch (rij.getKleur()) {
                case ROOD -> lblKleur.setTextFill(Color.RED);
                case GEEL -> lblKleur.setTextFill(Color.GOLD);
                case GROEN -> lblKleur.setTextFill(Color.GREEN);
                case BLAUW -> lblKleur.setTextFill(Color.BLUE);
            }

            for (int waarde : rij.getWaarden()) {
                Button vakje = new Button(String.valueOf(waarde));

                if (rij.getAangekruist().contains(waarde)) {
                    vakje.setDisable(true);
                    vakje.setStyle("-fx-background-color: lightgray;");
                }

                vakje.setOnAction(e -> {
                    try {
                        dc.voegKruisjeToe(rij.getKleur(), waarde);
                        GeluidManager.speelDobbelsteenSelecteren();
                        updateScherm();
                    } catch (Exception ex) {
                        GeluidManager.speelOngeldigeActie();
                        System.out.println(ex.getMessage());
                    }
                });

                rijBox.getChildren().add(vakje);
            }
            scorebladBox.getChildren().addAll(lblKleur, rijBox);
        }


    }
}

 */
