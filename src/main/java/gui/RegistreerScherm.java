package gui;

import domein.DomeinController;
import java.time.Year;
import java.util.Locale;
import java.util.ResourceBundle;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class RegistreerScherm extends VBox {
    private static final double FORM_WIDTH = 250;

    private final DomeinController dc;
    private final SchermController sc;

    private TextField txtGebruikersnaam;
    private Spinner<Integer> spnGeboortejaar;

    private Label lblGebruikersnaamOngeldig;
    private Label lblGeboortejaarOngeldig;
    private Label lblFeedback;

    private ResourceBundle bundle;

    public RegistreerScherm(DomeinController dc, SchermController sc) {
        this.dc = dc;
        this.sc = sc;

        bundle = ResourceBundle.getBundle("messages", Locale.getDefault());

        buildGui();
    }

    private void buildGui() {
        this.setPadding(new Insets(30));
        this.setSpacing(10);
        this.setAlignment(Pos.TOP_CENTER);

        // 🔥 Taalknoppen
        Button btnNL = new Button("NL");
        Button btnEN = new Button("EN");

        btnNL.setOnAction(e -> {
            veranderTaal("nl");
            this.requestFocus();
        });

        btnEN.setOnAction(e -> {
            veranderTaal("en");
            this.requestFocus();
        });

        HBox taalBox = new HBox(10, btnNL, btnEN);
        taalBox.setAlignment(Pos.CENTER);

        Label lblTitel = new Label(bundle.getString("reg.title"));
        lblTitel.setFont(new Font("Arial", 24));

        Label lblGebruikersnaam = new Label(bundle.getString("reg.username"));

        txtGebruikersnaam = new TextField();
        txtGebruikersnaam.setPromptText(bundle.getString("reg.usernamePrompt"));
        txtGebruikersnaam.setMaxWidth(FORM_WIDTH);

        lblGebruikersnaamOngeldig = new Label();
        lblGebruikersnaamOngeldig.setTextFill(Color.RED);

        Label lblGeboortejaar = new Label(bundle.getString("reg.birthYear"));

        int huidigJaar = Year.now().getValue();
        spnGeboortejaar = new Spinner<>(huidigJaar - 99, huidigJaar - 5, huidigJaar - 18);
        spnGeboortejaar.setEditable(true);
        spnGeboortejaar.setMaxWidth(FORM_WIDTH);

        lblGeboortejaarOngeldig = new Label();
        lblGeboortejaarOngeldig.setTextFill(Color.RED);

        Button btnRegistreer = new Button(bundle.getString("reg.register"));
        btnRegistreer.setPrefWidth(FORM_WIDTH);

        Button btnTerug = new Button(bundle.getString("reg.back"));
        btnTerug.setPrefWidth(FORM_WIDTH);

        lblFeedback = new Label();

        btnRegistreer.setOnAction(e -> registreerSpeler());
        btnTerug.setOnAction(e -> gaTerugNaarStartScherm());

        this.getChildren().addAll(
                taalBox,
                lblTitel,
                lblGebruikersnaam,
                txtGebruikersnaam,
                lblGebruikersnaamOngeldig,
                lblGeboortejaar,
                spnGeboortejaar,
                lblGeboortejaarOngeldig,
                btnRegistreer,
                btnTerug,
                lblFeedback
        );
    }

    private void registreerSpeler() {
        lblGebruikersnaamOngeldig.setText("");
        lblGeboortejaarOngeldig.setText("");
        lblFeedback.setText("");

        String gebruikersnaam = txtGebruikersnaam.getText().trim();
        Integer geboortejaar = null;
        boolean geldig = true;

        if (gebruikersnaam.isEmpty()) {
            lblGebruikersnaamOngeldig.setText(bundle.getString("reg.usernameRequired"));
            geldig = false;
        }

        try {
            geboortejaar = spnGeboortejaar.getValue();
            int huidigJaar = Year.now().getValue();

            if (geboortejaar < huidigJaar - 99 || geboortejaar > huidigJaar - 5) {
                lblGeboortejaarOngeldig.setText(bundle.getString("reg.invalidYear"));
                geldig = false;
            }
        } catch (Exception ex) {
            lblGeboortejaarOngeldig.setText(bundle.getString("reg.invalidYear"));
            geldig = false;
        }

        if (!geldig) return;

        try {
            dc.registreerSpeler(gebruikersnaam, geboortejaar);

            lblFeedback.setTextFill(Color.GREEN);
            lblFeedback.setText(bundle.getString("reg.success"));

            txtGebruikersnaam.clear();
            spnGeboortejaar.getValueFactory().setValue(Year.now().getValue() - 18);

        } catch (IllegalArgumentException ex) {
            lblFeedback.setTextFill(Color.RED);
            lblFeedback.setText(ex.getMessage());
        }
    }

    private void gaTerugNaarStartScherm() {
        sc.toonStartScherm();
    }

    private void veranderTaal(String taal) {
        Locale.setDefault(Locale.forLanguageTag(taal));
        sc.toonRegistreerScherm();
    }
}
