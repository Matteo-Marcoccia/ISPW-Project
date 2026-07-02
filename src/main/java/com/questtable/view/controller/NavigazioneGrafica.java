package com.questtable.view.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.util.Objects;

final class NavigazioneGrafica {
    private NavigazioneGrafica() {
    }

    static FXMLLoader creaLoader(Class<?> classeRiferimento, String percorsoFXML) {
        return new FXMLLoader(Objects.requireNonNull(classeRiferimento.getResource(percorsoFXML)));
    }

    static void aggiornaScena(ActionEvent event, Parent root) {
        aggiornaScena((Button) event.getSource(), root);
    }

    static void aggiornaScena(Button bottoneSorgente, Parent root) {
        Stage stage = (Stage) bottoneSorgente.getScene().getWindow();
        stage.setScene(new Scene(root, stage.getScene().getWidth(), stage.getScene().getHeight()));
    }
}
