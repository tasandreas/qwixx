package gui;

import domein.DomeinController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.util.Locale;
import java.util.ResourceBundle;

public class BeginSpelScherm extends VBox {

    private final DomeinController dc;
    public final SchermController sc;
    private final ResourceBundle bundle;

    public BeginSpelScherm(DomeinController dc, SchermController sc) {
        this.dc = dc;
        this.sc = sc;
        this.bundle = ResourceBundle.getBundle("messages", Locale.getDefault());
        buildGui();
    }

    private void buildGui() {
        this.setPadding(new Insets(20));
        this.setSpacing(20);
        this.setAlignment(Pos.CENTER);

        Label lblTitel = new Label(bundle.getString("begin.title"));
        lblTitel.setFont(new Font("Arial", 26));

        Label lblInfo = new Label(bundle.getString("begin.info"));
        lblInfo.setFont(new Font("Arial", 14));

        Button btnStart = new Button(bundle.getString("begin.start"));
        btnStart.setPrefWidth(200);

        Button btnTerug = new Button(bundle.getString("begin.back"));
        btnTerug.setPrefWidth(200);

        // Acties
        btnStart.setOnAction(e -> gaNaarSpel());
        btnTerug.setOnAction(e -> gaTerug());

        this.getChildren().addAll(lblTitel, lblInfo, btnStart, btnTerug);
    }

    private void gaNaarSpel() {
        // spel is al gestart in StartScherm, dus gewoon naar spel scherm
       // SpelScherm spelScherm = new SpelScherm(dc, sc);
        sc.toonSpelScherm();
    }

    private void gaTerug() {
        //StartScherm startScherm = new StartScherm(dc, sc);
        sc.toonStartScherm();
    }
}
