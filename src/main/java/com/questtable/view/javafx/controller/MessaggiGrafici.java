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
    private static final String TITOLO_USCITA = "Uscita";
    private static final String INTESTAZIONE_USCITA = "Vuoi chiudere QuestTable?";
    private static final String TESTO_USCITA = "Conferma per terminare l'applicazione.";
    private static final String TITOLO_PRENOTAZIONI_IN_ATTESA = "Prenotazioni in attesa";
    private static final String INTESTAZIONE_PRENOTAZIONI_IN_ATTESA = "Sono presenti richieste da confermare";
    private static final String TESTO_PRENOTAZIONI_IN_ATTESA =
            "Apri la sezione Conferma prenotazioni in attesa per gestirle.";

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

    static void mostraAvvisoPrenotazioniInAttesa() {
        mostraInformazione(
                TITOLO_PRENOTAZIONI_IN_ATTESA,
                INTESTAZIONE_PRENOTAZIONI_IN_ATTESA,
                TESTO_PRENOTAZIONI_IN_ATTESA
        );
    }

    private static void mostraInformazione(String titolo, String intestazione, String contenuto) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titolo);
        alert.setHeaderText(intestazione);
        alert.setContentText(contenuto);
        alert.showAndWait();
    }

    static boolean richiediConfermaLogout() {
        return richiediConferma(TITOLO_DISCONNESSIONE, INTESTAZIONE_DISCONNESSIONE, TESTO_DISCONNESSIONE);
    }

    static boolean richiediConfermaUscita() {
        return richiediConferma(TITOLO_USCITA, INTESTAZIONE_USCITA, TESTO_USCITA);
    }

    private static boolean richiediConferma(String titolo, String intestazione, String contenuto) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(titolo);
        alert.setHeaderText(intestazione);
        alert.setContentText(contenuto);

        Optional<ButtonType> scelta = alert.showAndWait();
        return scelta.isPresent() && scelta.get() == ButtonType.OK;
    }
}
