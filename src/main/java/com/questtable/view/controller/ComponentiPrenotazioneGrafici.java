package com.questtable.view.controller;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

final class ComponentiPrenotazioneGrafici {
    private ComponentiPrenotazioneGrafici() {
    }

    static Label creaCodicePrenotazione(int idPrenotazione) {
        Label label = new Label("#QT" + idPrenotazione);
        label.setStyle("-fx-text-fill: #6200EE; -fx-font-weight: bold;");
        return label;
    }

    static Label creaTitoloGioco(String titoloGioco) {
        Label label = new Label(titoloGioco);
        label.setFont(Font.font("Segoe UI", 18));
        label.setStyle("-fx-font-weight: bold; -fx-text-fill: #1A1A1A;");
        return label;
    }

    static Label creaRigaInformazione(String etichetta, String valore) {
        Label label = new Label(etichetta + ": " + valore);
        label.setStyle("-fx-text-fill: #4A4A4A;");
        label.setWrapText(true);
        return label;
    }

    static Region creaSpaziatore() {
        Region region = new Region();
        HBox.setHgrow(region, javafx.scene.layout.Priority.ALWAYS);
        return region;
    }

    static Label creaMessaggioListaVuota(String messaggio) {
        Label label = new Label(messaggio);
        label.setStyle("-fx-text-fill: white; -fx-font-size: 15px; -fx-font-weight: bold;");
        return label;
    }

    static void applicaStileCard(VBox card) {
        card.setMaxWidth(640);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 14; "
                + "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.16), 10, 0, 0, 4);");
    }
}
