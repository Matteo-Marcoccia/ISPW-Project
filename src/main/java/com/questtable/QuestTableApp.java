package com.questtable;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class QuestTableApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load the FXML file for the main view
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/questtable/view/SchermataHomeView.fxml")));

        // Set up the primary stage
        primaryStage.setTitle("QuestTable Application");
        primaryStage.setScene(new Scene(root, 1050, 720));
        primaryStage.show();
    }

    public static void main(String[] args) {
        System.setProperty("prism.lcdtext", "false");
        System.setProperty("prism.text", "t2k");
        launch(args);
    }
}
