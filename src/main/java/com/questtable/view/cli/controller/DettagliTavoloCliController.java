package com.questtable.view.cli.controller;

import com.questtable.bean.InfoTavoloBean;
import com.questtable.bean.PreventivoBean;
import com.questtable.bean.RichiestaPreventivoBean;
import com.questtable.controller.QuestTableController;
import com.questtable.view.FormattatoreImporti;

import java.util.Scanner;

public class DettagliTavoloCliController {
    private final QuestTableController questTableController;
    private final Scanner scanner;
    private final String idSessione;

    public DettagliTavoloCliController(QuestTableController questTableController, Scanner scanner,
                                       String idSessione) {
        this.questTableController = questTableController;
        this.scanner = scanner;
        this.idSessione = idSessione;
    }

    public boolean apri(InfoTavoloBean tavoloSelezionato) {
        if (tavoloSelezionato == null) {
            return false;
        }

        try {
            boolean dettagliAttivi = true;
            while (dettagliAttivi) {
                mostraDettagliTavolo(tavoloSelezionato);
                int postiRichiesti = leggiPostiRichiesti(tavoloSelezionato);
                if (postiRichiesti == 0) {
                    return false;
                }

                PreventivoBean preventivoBean = questTableController.calcolaPreventivo(
                        new RichiestaPreventivoBean(
                                tavoloSelezionato.fornisciIdentificativoTavolo(),
                                postiRichiesti
                        )
                );
                PagamentoCliController pagamentoCliController =
                        new PagamentoCliController(questTableController, scanner, idSessione, preventivoBean);
                dettagliAttivi = !pagamentoCliController.apri();
            }
            return true;
        } catch (IllegalArgumentException | IllegalStateException exception) {
            InterazioneConsole.stampaMessaggio(exception.getMessage());
            return false;
        }
    }

    private void mostraDettagliTavolo(InfoTavoloBean tavoloSelezionato) {
        InterazioneConsole.stampaSeparatore();
        InterazioneConsole.stampaMessaggio("Dettagli tavolo");
        InterazioneConsole.stampaMessaggio("Gioco: " + tavoloSelezionato.fornisciTitoloGioco());
        InterazioneConsole.stampaMessaggio("Giorno: "
                + tavoloSelezionato.fornisciGiornoSettimana().fornisciNomeVisualizzato());
        InterazioneConsole.stampaMessaggio("Orario: " + tavoloSelezionato.fornisciFasciaOraria());
        InterazioneConsole.stampaMessaggio("Posti disponibili: "
                + tavoloSelezionato.fornisciNumeroPostiDisponibili());
        InterazioneConsole.stampaMessaggio("Quota: "
                + FormattatoreImporti.formattaImporto(tavoloSelezionato.fornisciQuotaPartecipazione()));
        InterazioneConsole.stampaMessaggio("0. Torna ai tavoli");
    }

    private int leggiPostiRichiesti(InfoTavoloBean tavoloSelezionato) {
        int postiRichiesti = InterazioneConsole.leggiIntero(scanner, "Posti da prenotare: ");
        while (postiRichiesti != 0
                && (postiRichiesti < 1 || postiRichiesti > tavoloSelezionato.fornisciNumeroPostiDisponibili())) {
            InterazioneConsole.stampaMessaggio("Inserisci un numero di posti valido.");
            postiRichiesti = InterazioneConsole.leggiIntero(scanner, "Posti da prenotare: ");
        }
        return postiRichiesti;
    }

}
