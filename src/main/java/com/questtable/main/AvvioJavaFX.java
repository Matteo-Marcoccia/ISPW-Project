package com.questtable.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class AvvioJavaFX extends Application {
    private static final String PERCORSO_HOME = "/com/questtable/view/SchermataHomeView.fxml";
    private static final String TITOLO_APPLICAZIONE = "QuestTable";
    private static final int LARGHEZZA_FINESTRA = 1050;
    private static final int ALTEZZA_FINESTRA = 720;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(PERCORSO_HOME)));

        primaryStage.setTitle(TITOLO_APPLICAZIONE);
        primaryStage.setScene(new Scene(root, LARGHEZZA_FINESTRA, ALTEZZA_FINESTRA));
        primaryStage.show();
    }

    static void avvia() {
        System.setProperty("prism.lcdtext", "false");
        System.setProperty("prism.text", "t2k");
        launch();
    }
}
