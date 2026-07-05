package com.questtable.view.cli.controller;

import com.questtable.bean.PrenotazioneBean;

final class ComponentiPrenotazioneCli {
    private ComponentiPrenotazioneCli() {
    }

    static void stampaPrenotazione(PrenotazioneBean prenotazione, boolean mostraCliente, boolean mostraStato) {
        InterazioneConsole.stampaMessaggio("Codice: #QT"
                + prenotazione.fornisciIdentificativoPrenotazione());
        if (mostraCliente) {
            InterazioneConsole.stampaMessaggio("Cliente: " + prenotazione.fornisciUsernameCliente());
        }
        InterazioneConsole.stampaMessaggio("Gioco: " + prenotazione.fornisciTitoloGioco());
        InterazioneConsole.stampaMessaggio("Tavolo: "
                + prenotazione.fornisciGiornoAttivita().fornisciNomeVisualizzato()
                + " | " + prenotazione.fornisciFasciaOrariaAttivita());
        InterazioneConsole.stampaMessaggio("Richiesta: "
                + prenotazione.fornisciDataPrenotazione()
                + " alle " + prenotazione.fornisciOraPrenotazione());
        InterazioneConsole.stampaMessaggio("Posti: " + prenotazione.fornisciNumeroPostiPrenotati());
        InterazioneConsole.stampaMessaggio("Importo: "
                + InterazioneConsole.formattaImporto(prenotazione.fornisciImportoTotale()));
        if (mostraStato) {
            InterazioneConsole.stampaMessaggio("Stato: "
                    + prenotazione.fornisciStatoPrenotazione().fornisciNomeVisualizzato());
        }
    }
}
