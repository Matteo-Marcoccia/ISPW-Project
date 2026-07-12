package com.questtable.view.cli.controller;

import com.questtable.bean.ListaPrenotazioniBean;
import com.questtable.bean.PrenotazioneBean;
import com.questtable.controller.PrenotaPostoControllerApplicativo;

import java.util.List;
import java.util.Scanner;

public class ConfermaPrenotazioniCliController {
    private final PrenotaPostoControllerApplicativo prenotaPostoControllerApplicativo;
    private final Scanner scanner;
    private final String idSessione;

    public ConfermaPrenotazioniCliController(PrenotaPostoControllerApplicativo prenotaPostoControllerApplicativo, Scanner scanner,
                                             String idSessione) {
        this.prenotaPostoControllerApplicativo = prenotaPostoControllerApplicativo;
        this.scanner = scanner;
        this.idSessione = idSessione;
    }

    public void apri() {
        boolean continua = true;

        while (continua) {
            continua = mostraPrenotazioniEConferma();
        }
    }

    private boolean mostraPrenotazioniEConferma() {
        try {
            ListaPrenotazioniBean listaPrenotazioniBean =
                    prenotaPostoControllerApplicativo.fornisciPrenotazioniInAttesa(idSessione);

            InterazioneConsole.stampaSeparatore();
            InterazioneConsole.stampaMessaggio("Prenotazioni in attesa");

            if (listaPrenotazioniBean.verificaAssenzaPrenotazioni()) {
                InterazioneConsole.stampaMessaggio("Non ci sono prenotazioni in attesa.");
                return false;
            }

            List<PrenotazioneBean> prenotazioni = listaPrenotazioniBean.fornisciPrenotazioni();
            mostraPrenotazioni(prenotazioni);

            int scelta = InterazioneConsole.leggiIntero(scanner, "Seleziona prenotazione da confermare: ");
            if (scelta == 0) {
                return false;
            }
            if (scelta < 1 || scelta > prenotazioni.size()) {
                InterazioneConsole.stampaSceltaNonValida();
                return true;
            }

            confermaPrenotazione(prenotazioni.get(scelta - 1));
            return true;
        } catch (IllegalArgumentException | IllegalStateException exception) {
            InterazioneConsole.stampaMessaggio(exception.getMessage());
            return false;
        }
    }

    private void mostraPrenotazioni(List<PrenotazioneBean> prenotazioni) {
        for (int indice = 0; indice < prenotazioni.size(); indice++) {
            InterazioneConsole.stampaSeparatore();
            InterazioneConsole.stampaMessaggio((indice + 1) + ". Prenotazione");
            ComponentiPrenotazioneCli.stampaPrenotazione(prenotazioni.get(indice), true, false);
        }
        InterazioneConsole.stampaMessaggio("0. Torna alla home");
    }

    private void confermaPrenotazione(PrenotazioneBean prenotazione) {
        prenotaPostoControllerApplicativo.confermaPrenotazione(
                idSessione,
                prenotazione.fornisciIdentificativoPrenotazione()
        );
        InterazioneConsole.stampaMessaggio("Prenotazione #QT"
                + prenotazione.fornisciIdentificativoPrenotazione()
                + " confermata con successo.");
    }
}

