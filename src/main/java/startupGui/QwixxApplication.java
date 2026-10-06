package startupGui;

import domein.DomeinController;
import gui.SchermController;
import gui.StartScherm;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.image.Image;

import javax.swing.*;

public class QwixxApplication extends Application {

    @Override
    public void start(Stage stage) {
        DomeinController dc = new DomeinController();
        SchermController sc = new SchermController(stage, dc);
        stage.setTitle("Qwixx!");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/icon.png")));
        sc.toonStartScherm();
        stage.setMaximized(true);
    }
}

