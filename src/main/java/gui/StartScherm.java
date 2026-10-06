package gui;

import domein.DomeinController;
import domein.SoortSpel;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Locale;

import domein.Speler;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.application.Platform;
import javafx.scene.control.ButtonBar;

public class StartScherm extends VBox {
    private final DomeinController dc;
    private final SchermController sc;

    private ListView<Speler> lvSpelers;
    private Label lblFeedback;
    private Button btnStartSpel;
    private ResourceBundle bundle;
    private Button btnNL;
    private Button btnEN;
    private Button btnDonker;
    private Button btnLicht;
    private TextField txfZoekSpeler;
    private FilteredList<Speler> gefilterdeSpelers;

    private static final int MIN_SPELERS = 2;
    private static final int MAX_SPELERS = 5;

    public StartScherm(DomeinController dc, SchermController sc) {
        this.dc = dc;
        this.sc = sc;

        bundle = ResourceBundle.getBundle("messages", Locale.getDefault());

        buildGui();
        laadSpelers();
    }

    private void buildGui() {
        this.setPadding(new Insets(20));
        this.setSpacing(12);
        this.setAlignment(Pos.TOP_CENTER);

        Button btnVerlaat = new Button(bundle.getString("gui.quit"));
        btnVerlaat.setPrefWidth(180);
        btnVerlaat.setOnAction(e -> bevestigVerlaten());


        btnNL = new Button("NL");
        btnEN = new Button("EN");

        btnNL.setOnAction(e -> veranderTaal("nl"));
        btnEN.setOnAction(e -> veranderTaal("en"));

        btnDonker = new Button(bundle.getString("gui.theme.dark"));
        btnLicht = new Button(bundle.getString("gui.theme.light"));

        btnDonker.setOnAction(e -> veranderThema(false));
        btnLicht.setOnAction(e -> veranderThema(true));

        HBox taalBox = new HBox(10, btnNL, btnEN);
        taalBox.setAlignment(Pos.CENTER);

        HBox themaBox = new HBox(10, btnDonker, btnLicht);
        themaBox.setAlignment(Pos.CENTER);

        javafx.scene.image.ImageView logo = new javafx.scene.image.ImageView(
                new javafx.scene.image.Image(getClass().getResourceAsStream("/logo.png"))
        );

        logo.setFitWidth(300);
        logo.setFitHeight(150);
        logo.setPreserveRatio(true);

        Label lblTitel = new Label(bundle.getString("gui.title"));
        lblTitel.setFont(new Font("Arial", 28));

        Label lblUitleg = new Label(bundle.getString("gui.selectPlayers"));
        txfZoekSpeler = new TextField();
        txfZoekSpeler.setPromptText(bundle.getString("gui.searchPlayer"));
        txfZoekSpeler.setMaxWidth(360);

        lvSpelers = new ListView<>();
        lvSpelers.setPrefHeight(280);
        lvSpelers.setPrefWidth(360);
        lvSpelers.setMaxWidth(360);
        lvSpelers.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        //door dit kunnen we meerdere spelers kiezen zonder de ctrl toets
        lvSpelers.setCellFactory(lv -> {
            javafx.scene.control.ListCell<Speler> cel = new javafx.scene.control.ListCell<>() {
                @Override
                protected void updateItem(Speler speler, boolean leeg) {
                    super.updateItem(speler, leeg);
                    setText(leeg || speler == null ? null : speler.toString());
                }
            };
            cel.addEventFilter(javafx.scene.input.MouseEvent.MOUSE_PRESSED, event -> {
                lv.requestFocus();
                if (!cel.isEmpty()) {
                    int index = cel.getIndex();
                    if (lv.getSelectionModel().isSelected(index)) {
                        lv.getSelectionModel().clearSelection(index);
                    } else {
                        lv.getSelectionModel().select(index);
                    }
                    event.consume();
                }
            });

            return cel;
        });

        lblFeedback = new Label();
        lblFeedback.setTextFill(Color.BLACK);

        Button btnRegistreerSpeler = new Button(bundle.getString("gui.register"));
        btnRegistreerSpeler.setPrefWidth(180);

        btnStartSpel = new Button(bundle.getString("gui.start"));
        btnStartSpel.setPrefWidth(180);
        btnStartSpel.setDisable(true);

        btnRegistreerSpeler.setOnAction(e -> gaNaarRegistreerScherm());
        btnStartSpel.setOnAction(e -> startSpel());

        lvSpelers.getSelectionModel().getSelectedItems().addListener(
                (ListChangeListener<Speler>) change -> controleerSelectie()
        );

        Button btnVerwijderSpeler = new Button(bundle.getString("gui.delete"));
        btnVerwijderSpeler.setPrefWidth(180);
        btnVerwijderSpeler.setOnAction(e -> toonVerwijderScherm());


        lblTitel.getStyleClass().add("label-titel");
        btnNL.getStyleClass().add("button-taal");
        btnEN.getStyleClass().add("button-taal");
        btnDonker.getStyleClass().add("button-taal");
        btnLicht.getStyleClass().add("button-taal");
        btnStartSpel.getStyleClass().add("button-start");
        lblFeedback.getStyleClass().add("label-feedback");
        updateTaalHighlight();
        updateThemaHighlight();

        this.getChildren().addAll(
                taalBox,
                themaBox,
                logo,
                lblTitel,
                lblUitleg,
                txfZoekSpeler,
                lvSpelers,
                lblFeedback,
                btnRegistreerSpeler,
                btnStartSpel,
                btnVerlaat,
                btnVerwijderSpeler
        );
    }

    private void laadSpelers() {
        ObservableList<Speler> alleSpelers = FXCollections.observableArrayList(dc.geefGeregistreerdeSpelers());
        gefilterdeSpelers = new FilteredList<>(alleSpelers, speler -> true);

        txfZoekSpeler.textProperty().addListener((observable, oudeWaarde, nieuweWaarde) -> {
            gefilterdeSpelers.setPredicate(speler -> {
                if (nieuweWaarde == null || nieuweWaarde.isBlank()) {
                    return true;
                }

                String zoektekst = nieuweWaarde.toLowerCase();
                return speler.getGebruikersnaam().toLowerCase().contains(zoektekst);
            });
        });

        lvSpelers.setItems(gefilterdeSpelers);

        List<Speler> geregistreerdeSpelers = dc.geefGeregistreerdeSpelers();

        if (geregistreerdeSpelers.isEmpty()) {
            lblFeedback.setTextFill(Color.RED);
            lblFeedback.setText(bundle.getString("gui.noPlayers"));
            btnStartSpel.setDisable(true);
        } else if (geregistreerdeSpelers.size() < MIN_SPELERS) {
            lblFeedback.setTextFill(Color.RED);
            lblFeedback.setText(bundle.getString("gui.minPlayers"));
            btnStartSpel.setDisable(true);
        } else {
            lblFeedback.setTextFill(Color.BLACK);
            lblFeedback.setText(bundle.getString("gui.selected") + " 0/" + MAX_SPELERS);
        }
    }

    private void controleerSelectie() {
        int aantal = lvSpelers.getSelectionModel().getSelectedItems().size();

        if (aantal > MAX_SPELERS) {
            lblFeedback.setTextFill(Color.RED);
            lblFeedback.setText(bundle.getString("gui.maxPlayers"));
            btnStartSpel.setDisable(true);
            return;
        }

        if (aantal >= MIN_SPELERS) {
            lblFeedback.setTextFill(Color.GREEN);
            lblFeedback.setText(aantal + " " + bundle.getString("gui.selectedReady"));
            btnStartSpel.setDisable(false);
        } else {
            lblFeedback.setTextFill(Color.BLACK);
            lblFeedback.setText(bundle.getString("gui.selected") + " " + aantal + "/" + MAX_SPELERS);
            btnStartSpel.setDisable(true);
        }
    }

    private void startSpel() {
        List<Speler> geselecteerdeSpelers = new ArrayList<>(lvSpelers.getSelectionModel().getSelectedItems());

        if (geselecteerdeSpelers.size() < MIN_SPELERS || geselecteerdeSpelers.size() > MAX_SPELERS) {
            lblFeedback.setTextFill(Color.RED);
            lblFeedback.setText(bundle.getString("gui.selectBetweenError"));
            return;
        }

        SoortSpel soortSpel = kiesSoortSpel();
        if (soortSpel == null) {
            return;
        }

        dc.selecteerSpelersVoorNieuwSpel(geselecteerdeSpelers);
        dc.startSpel(soortSpel);
        sc.toonSpelersOverzichtScherm();

    }

    private void gaNaarRegistreerScherm() {
        sc.toonRegistreerScherm();
    }

    private void veranderTaal(String taal) {
        Locale.setDefault(new Locale(taal));
        ResourceBundle.clearCache();
        sc.toonStartScherm();
    }

    private void veranderThema(boolean lichtThema) {
        if (lichtThema) {
            sc.gebruikLichtThema();
        } else {
            sc.gebruikDonkerThema();
        }
        sc.toonStartScherm();
    }

    private void bevestigVerlaten() {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
                javafx.scene.control.Alert.AlertType.CONFIRMATION
        );
        alert.setTitle(bundle.getString("gui.quit"));
        alert.setHeaderText(null);
        alert.setContentText(bundle.getString("gui.quit.confirm"));

        javafx.stage.Stage alertStage = (javafx.stage.Stage) alert.getDialogPane().getScene().getWindow();
        alertStage.getIcons().add(new javafx.scene.image.Image(getClass().getResourceAsStream("/kruisje.png")));

        javafx.scene.control.ButtonType btnJa = new javafx.scene.control.ButtonType(
                bundle.getString("gui.quit.yes")
        );
        javafx.scene.control.ButtonType btnNee = new javafx.scene.control.ButtonType(
                bundle.getString("gui.quit.no"),
                javafx.scene.control.ButtonBar.ButtonData.CANCEL_CLOSE
        );

        alert.getButtonTypes().setAll(btnJa, btnNee);

        alert.showAndWait().ifPresent(response -> {
            if (response == btnJa) {
                javafx.application.Platform.exit();
            }
        });
    }

    private void toonVerwijderScherm() {
        List<Speler> geselecteerdeSpelers = new ArrayList<>(lvSpelers.getSelectionModel().getSelectedItems());

        if (geselecteerdeSpelers.isEmpty()) {
            lblFeedback.setTextFill(Color.RED);
            lblFeedback.setText(bundle.getString("gui.delete.selectFirst"));
            return;
        }

        try {
            for (Speler speler : geselecteerdeSpelers) {
                dc.verwijderSpeler(speler.getGebruikersnaam());
            }

            laadSpelers();

            lblFeedback.setTextFill(Color.GREEN);

            if (geselecteerdeSpelers.size() == 1) {
                lblFeedback.setText(bundle.getString("gui.delete.deletedOne"));
            } else {
                lblFeedback.setText(String.format(
                        bundle.getString("gui.delete.deletedMany"),
                        geselecteerdeSpelers.size()
                ));
            }

        } catch (Exception e) {
            lblFeedback.setTextFill(Color.RED);
            lblFeedback.setText(e.getMessage());
        }
    }

    private void updateTaalHighlight() {
        btnNL.getStyleClass().remove("button-taal-actief");
        btnEN.getStyleClass().remove("button-taal-actief");

        String taal = Locale.getDefault().getLanguage();

        if (taal.equals("nl")) {
            btnNL.getStyleClass().add("button-taal-actief");
        } else if (taal.equals("en")) {
            btnEN.getStyleClass().add("button-taal-actief");
        }
    }

    private void updateThemaHighlight() {
        btnDonker.getStyleClass().remove("button-taal-actief");
        btnLicht.getStyleClass().remove("button-taal-actief");

        if (sc.gebruiktLichtThema()) {
            btnLicht.getStyleClass().add("button-taal-actief");
        } else {
            btnDonker.getStyleClass().add("button-taal-actief");
        }
    }

    private SoortSpel kiesSoortSpel() {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
                javafx.scene.control.Alert.AlertType.CONFIRMATION
        );

        alert.setTitle(bundle.getString("gui.gameType.title"));
        alert.setHeaderText(null);
        alert.setContentText(bundle.getString("gui.gameType.info"));
        alert.setGraphic(null);

        alert.getDialogPane().getStylesheets().add(
                getClass().getResource(sc.getStylesheetPath()).toExternalForm()
        );
        alert.getDialogPane().getStyleClass().add("compact-dialog");

        javafx.scene.control.ButtonType btnBasis = new javafx.scene.control.ButtonType(
                bundle.getString("gui.gameType.base")
        );
        javafx.scene.control.ButtonType btnVariant = new javafx.scene.control.ButtonType(
                bundle.getString("gui.gameType.variant")
        );
        javafx.scene.control.ButtonType btnRandom = new javafx.scene.control.ButtonType(
                bundle.getString("gui.gameType.random")
        );
        javafx.scene.control.ButtonType btnAnnuleer = new javafx.scene.control.ButtonType(
                bundle.getString("gui.cancel"),
                javafx.scene.control.ButtonBar.ButtonData.CANCEL_CLOSE
        );

        alert.getButtonTypes().setAll(btnBasis, btnVariant, btnRandom, btnAnnuleer);

        java.util.Optional<javafx.scene.control.ButtonType> resultaat = alert.showAndWait();

        if (resultaat.isEmpty() || resultaat.get() == btnAnnuleer) {
            return null;
        }

        if (resultaat.get() == btnVariant) {
            return SoortSpel.VARIANT;
        }

        if (resultaat.get() == btnRandom) {
            return SoortSpel.RANDOM;
        }

        return SoortSpel.BASIS;
    }
}
