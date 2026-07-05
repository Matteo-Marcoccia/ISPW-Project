package com.questtable.view.cli.controller;

import com.questtable.bean.InfoTavoloBean;
import com.questtable.bean.ListaTavoliBean;
import com.questtable.controller.QuestTableController;

import java.util.List;
import java.util.Scanner;

public class ListaTavoliCliController {
    private final QuestTableController questTableController;
    private final Scanner scanner;
    private final String idSessione;

    public ListaTavoliCliController(QuestTableController questTableController, Scanner scanner, String idSessione) {
        this.questTableController = questTableController;
        this.scanner = scanner;
        this.idSessione = idSessione;
    }

    public void apri() {
        try {
            ListaTavoliBean listaTavoliBean = questTableController.fornisciTavoliDisponibili(idSessione, null);
            if (!listaTavoliBean.verificaPresenzaTavoli()) {
                InterazioneConsole.stampaMessaggio("Non ci sono tavoli disponibili.");
                return;
            }

            boolean listaAttiva = true;
            while (listaAttiva) {
                InfoTavoloBean tavoloSelezionato = selezionaTavoloDallaLista(listaTavoliBean.fornisciTavoli());
                if (tavoloSelezionato == null) {
                    return;
                }

                DettagliTavoloCliController dettagliTavoloCliController =
                        new DettagliTavoloCliController(questTableController, scanner, idSessione);
                listaAttiva = !dettagliTavoloCliController.apri(tavoloSelezionato);
            }
        } catch (IllegalArgumentException | IllegalStateException exception) {
            InterazioneConsole.stampaMessaggio(exception.getMessage());
        }
    }

    private InfoTavoloBean selezionaTavoloDallaLista(List<InfoTavoloBean> tavoli) {
        mostraTavoliDisponibili(tavoli);

        int scelta = InterazioneConsole.leggiIntero(scanner, "Seleziona tavolo: ");
        if (scelta == 0) {
            return null;
        }
        if (scelta < 1 || scelta > tavoli.size()) {
            InterazioneConsole.stampaSceltaNonValida();
            return null;
        }

        return tavoli.get(scelta - 1);
    }

    private void mostraTavoliDisponibili(List<InfoTavoloBean> tavoli) {
        InterazioneConsole.stampaSeparatore();
        InterazioneConsole.stampaMessaggio("Tavoli disponibili");

        for (int indice = 0; indice < tavoli.size(); indice++) {
            InfoTavoloBean tavolo = tavoli.get(indice);
            InterazioneConsole.stampaMessaggio((indice + 1) + ". "
                    + tavolo.fornisciTitoloGioco()
                    + " | " + tavolo.fornisciGiornoSettimana().fornisciNomeVisualizzato()
                    + " | " + tavolo.fornisciFasciaOraria()
                    + " | posti disponibili: " + tavolo.fornisciNumeroPostiDisponibili()
                    + " | quota: " + InterazioneConsole.formattaImporto(tavolo.fornisciQuotaPartecipazione()));
        }

        InterazioneConsole.stampaMessaggio("0. Torna alla home");
    }

}
