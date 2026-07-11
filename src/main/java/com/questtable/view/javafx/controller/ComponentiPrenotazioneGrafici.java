package com.questtable.view.javafx.controller;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

final class ComponentiPrenotazioneGrafici {
    private static final String FONT_PRINCIPALE = "Segoe UI";

    private ComponentiPrenotazioneGrafici() {
    }

    static Label creaCodicePrenotazione(int idPrenotazione) {
        Label label = new Label("#QT" + idPrenotazione);
        label.setFont(Font.font(FONT_PRINCIPALE, FontWeight.BOLD, 14));
        label.setStyle("-fx-text-fill: #6200EE;");
        return label;
    }

    static Label creaTitoloGioco(String titoloGioco) {
        Label label = new Label(titoloGioco);
        label.setFont(Font.font(FONT_PRINCIPALE, FontWeight.BOLD, 18));
        label.setStyle("-fx-text-fill: #1A1A1A;");
        return label;
    }

    static Label creaRigaInformazione(String etichetta, String valore) {
        Label label = new Label(etichetta + ": " + valore);
        label.setFont(Font.font(FONT_PRINCIPALE, 14));
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
        label.setFont(Font.font(FONT_PRINCIPALE, FontWeight.BOLD, 15));
        label.setStyle("-fx-text-fill: white;");
        return label;
    }

    static void applicaStileCard(VBox card) {
        card.setMaxWidth(640);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 14; "
                + "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.16), 10, 0, 0, 4);");
    }
}
