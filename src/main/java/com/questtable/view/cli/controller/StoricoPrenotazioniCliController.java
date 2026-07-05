package com.questtable.view.cli.controller;

import com.questtable.bean.ListaPrenotazioniBean;
import com.questtable.bean.PrenotazioneBean;
import com.questtable.controller.QuestTableController;

import java.util.Scanner;

public class StoricoPrenotazioniCliController {
    private final QuestTableController questTableController;
    private final Scanner scanner;
    private final String idSessione;

    public StoricoPrenotazioniCliController(QuestTableController questTableController, Scanner scanner,
                                            String idSessione) {
        this.questTableController = questTableController;
        this.scanner = scanner;
        this.idSessione = idSessione;
    }

    public void apri() {
        try {
            ListaPrenotazioniBean listaPrenotazioniBean =
                    questTableController.fornisciPrenotazioniCliente(idSessione);

            InterazioneConsole.stampaSeparatore();
            InterazioneConsole.stampaMessaggio("Storico prenotazioni");

            if (listaPrenotazioniBean.verificaAssenzaPrenotazioni()) {
                InterazioneConsole.stampaMessaggio("Non hai ancora prenotazioni.");
                attendiInvio();
                return;
            }

            for (PrenotazioneBean prenotazione : listaPrenotazioniBean.fornisciPrenotazioni()) {
                InterazioneConsole.stampaSeparatore();
                ComponentiPrenotazioneCli.stampaPrenotazione(prenotazione, false, true);
            }
            attendiInvio();
        } catch (IllegalArgumentException | IllegalStateException exception) {
            InterazioneConsole.stampaMessaggio(exception.getMessage());
        }
    }

    private void attendiInvio() {
        InterazioneConsole.leggiTesto(scanner, "Premi invio per tornare alla home.");
    }
}
