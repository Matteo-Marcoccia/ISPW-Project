package com.questtable.view.cli.controller;

import com.questtable.bean.PrenotazioneBean;

import java.util.Scanner;

public class RichiestaPrenotazioneOkCliController {
    private final PrenotazioneBean prenotazioneBean;
    private final Scanner scanner;

    public RichiestaPrenotazioneOkCliController(PrenotazioneBean prenotazioneBean, Scanner scanner) {
        this.prenotazioneBean = prenotazioneBean;
        this.scanner = scanner;
    }

    public void apri() {
        if (prenotazioneBean == null) {
            return;
        }

        InterazioneConsole.stampaSeparatore();
        InterazioneConsole.stampaMessaggio("Richiesta di prenotazione inviata.");
        ComponentiPrenotazioneCli.stampaPrenotazione(prenotazioneBean, false, true);
        InterazioneConsole.leggiTesto(scanner, "Premi invio per tornare alla home.");
    }
}
