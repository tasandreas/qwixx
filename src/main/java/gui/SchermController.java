package gui;

import domein.DomeinController;
import javafx.stage.Stage;

public class SchermController {

    private final Stage stage;
    private final DomeinController dc;
    private String stylesheetPath = "/css/style.css";

    public SchermController(Stage stage, DomeinController dc) {
        this.stage = stage;
        this.dc = dc;
    }

    public void toonStartScherm() {
        StartScherm scherm = new StartScherm(dc, this);
        wisselScherm(scherm);
    }

    public void toonRegistreerScherm() {
        RegistreerScherm scherm = new RegistreerScherm(dc, this);
        wisselScherm(scherm);
    }

    public void toonSpelScherm() {
        SpelScherm scherm = new SpelScherm(dc, this);
        wisselScherm(scherm);
    }

    public void toonBeginSpelScherm() {
        BeginSpelScherm scherm = new BeginSpelScherm(dc, this);
        wisselScherm(scherm);
    }

    public void toonEindeSpelScherm() {
        EindeSpelScherm scherm = new EindeSpelScherm(dc, this);
        wisselScherm(scherm);
    }

    private void wisselScherm(javafx.scene.layout.Pane scherm) {
        if (stage.getScene() == null) {
            javafx.scene.Scene scene = new javafx.scene.Scene(scherm, 500, 650);
            scene.getStylesheets().add(getClass().getResource(stylesheetPath).toExternalForm());
            stage.setScene(scene);
        } else {
            stage.getScene().setRoot(scherm);
            pasStylesheetToe();
        }
        stage.show();
    }

    public void gebruikDonkerThema() {
        stylesheetPath = "/css/style.css";
        pasStylesheetToe();
    }

    public void gebruikLichtThema() {
        stylesheetPath = "/css/licht.css";
        pasStylesheetToe();
    }

    public boolean gebruiktLichtThema() {
        return "/css/licht.css".equals(stylesheetPath);
    }

    public String getStylesheetPath() {
        return stylesheetPath;
    }

    private void pasStylesheetToe() {
        if (stage.getScene() == null) {
            return;
        }

        stage.getScene().getStylesheets().clear();
        stage.getScene().getStylesheets().add(getClass().getResource(stylesheetPath).toExternalForm());
    }

    public void toonSpelersOverzichtScherm() {
        SpelersOverzichtScherm scherm = new SpelersOverzichtScherm(dc, this);
        wisselScherm(scherm);
    }
}
