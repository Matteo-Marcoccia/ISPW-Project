package com.questtable.view.javafx.controller;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

final class MessaggiGrafici {
    private static final String TITOLO_FUNZIONE_NON_DISPONIBILE = "Funzione non disponibile";
    private static final String TESTO_FUNZIONE_NON_DISPONIBILE =
            "Questa funzione non e' prevista nella versione corrente di QuestTable.";
    private static final String TITOLO_DISCONNESSIONE = "Disconnessione";
    private static final String INTESTAZIONE_DISCONNESSIONE = "Vuoi disconnetterti?";
    private static final String TESTO_DISCONNESSIONE = "Conferma per uscire dal tuo account.";
    private static final String TITOLO_NOTIFICHE = "Notifiche";
    private static final String INTESTAZIONE_NOTIFICHE = "Hai nuove notifiche";

    private MessaggiGrafici() {
    }

    static void mostraErrore(String intestazione, String contenuto) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Errore");
        alert.setHeaderText(intestazione);
        alert.setContentText(contenuto);
        alert.showAndWait();
    }

    static void mostraFunzioneNonDisponibile(String intestazione) {
        mostraInformazione(TITOLO_FUNZIONE_NON_DISPONIBILE, intestazione, TESTO_FUNZIONE_NON_DISPONIBILE);
    }

    static void mostraNotifiche(String contenuto) {
        mostraInformazione(TITOLO_NOTIFICHE, INTESTAZIONE_NOTIFICHE, contenuto);
    }

    private static void mostraInformazione(String titolo, String intestazione, String contenuto) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titolo);
        alert.setHeaderText(intestazione);
        alert.setContentText(contenuto);
        alert.showAndWait();
    }

    static boolean richiediConfermaLogout() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(TITOLO_DISCONNESSIONE);
        alert.setHeaderText(INTESTAZIONE_DISCONNESSIONE);
        alert.setContentText(TESTO_DISCONNESSIONE);

        Optional<ButtonType> scelta = alert.showAndWait();
        return scelta.isPresent() && scelta.get() == ButtonType.OK;
    }
}
