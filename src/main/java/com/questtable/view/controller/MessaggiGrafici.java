package com.questtable.view.controller;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

final class MessaggiGrafici {
    private MessaggiGrafici() {
    }

    static void mostraErrore(String intestazione, String contenuto) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Errore");
        alert.setHeaderText(intestazione);
        alert.setContentText(contenuto);
        alert.showAndWait();
    }

    static void mostraInformazione(String titolo, String intestazione, String contenuto) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titolo);
        alert.setHeaderText(intestazione);
        alert.setContentText(contenuto);
        alert.showAndWait();
    }

    static boolean richiediConferma(String titolo, String intestazione, String contenuto) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(titolo);
        alert.setHeaderText(intestazione);
        alert.setContentText(contenuto);

        Optional<ButtonType> scelta = alert.showAndWait();
        return scelta.isPresent() && scelta.get() == ButtonType.OK;
    }
}
