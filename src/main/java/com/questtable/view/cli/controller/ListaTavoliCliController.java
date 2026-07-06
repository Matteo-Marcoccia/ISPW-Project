package com.questtable.view.cli.controller;

import com.questtable.bean.InfoTavoloBean;
import com.questtable.bean.ListaTavoliBean;
import com.questtable.bean.RicercaTavoliBean;
import com.questtable.controller.QuestTableController;
import com.questtable.model.GiornoSettimana;

import java.util.List;
import java.util.Scanner;

public class ListaTavoliCliController {
    private static final int SCELTA_TORNA_HOME = 0;
    private static final int SCELTA_FILTRA_TAVOLI = 8;
    private static final int SCELTA_PULISCI_FILTRI = 9;

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
            RicercaTavoliBean ricercaTavoliBean = null;
            boolean listaAttiva = true;
            while (listaAttiva) {
                ListaTavoliBean listaTavoliBean =
                        questTableController.fornisciTavoliDisponibili(idSessione, ricercaTavoliBean);

                mostraTavoliDisponibili(listaTavoliBean.fornisciTavoli(), ricercaTavoliBean);
                int scelta = InterazioneConsole.leggiIntero(scanner, "Seleziona tavolo o comando: ");

                if (scelta == SCELTA_TORNA_HOME) {
                    return;
                }
                if (scelta == SCELTA_FILTRA_TAVOLI) {
                    ricercaTavoliBean = creaRicercaTavoliBean();
                    continue;
                }
                if (scelta == SCELTA_PULISCI_FILTRI) {
                    ricercaTavoliBean = null;
                    continue;
                }

                InfoTavoloBean tavoloSelezionato = selezionaTavoloDallaLista(
                        listaTavoliBean.fornisciTavoli(),
                        scelta
                );
                if (tavoloSelezionato == null) {
                    InterazioneConsole.stampaSceltaNonValida();
                    continue;
                }

                DettagliTavoloCliController dettagliTavoloCliController =
                        new DettagliTavoloCliController(questTableController, scanner, idSessione);
                listaAttiva = !dettagliTavoloCliController.apri(tavoloSelezionato);
            }
        } catch (IllegalArgumentException | IllegalStateException exception) {
            InterazioneConsole.stampaMessaggio(exception.getMessage());
        }
    }

    private InfoTavoloBean selezionaTavoloDallaLista(List<InfoTavoloBean> tavoli, int scelta) {
        if (scelta < 1 || scelta > tavoli.size()) {
            return null;
        }

        return tavoli.get(scelta - 1);
    }

    private void mostraTavoliDisponibili(List<InfoTavoloBean> tavoli, RicercaTavoliBean ricercaTavoliBean) {
        InterazioneConsole.stampaSeparatore();
        InterazioneConsole.stampaMessaggio("Tavoli disponibili");
        mostraFiltriApplicati(ricercaTavoliBean);

        if (tavoli.isEmpty()) {
            InterazioneConsole.stampaMessaggio("Non ci sono tavoli disponibili con i filtri selezionati.");
        }

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
        InterazioneConsole.stampaMessaggio("8. Filtra tavoli");
        InterazioneConsole.stampaMessaggio("9. Pulisci filtri");
    }

    private void mostraFiltriApplicati(RicercaTavoliBean ricercaTavoliBean) {
        if (ricercaTavoliBean == null
                || (!ricercaTavoliBean.verificaFiltroGiocoPresente()
                && !ricercaTavoliBean.verificaFiltroGiornoPresente())) {
            InterazioneConsole.stampaMessaggio("Filtri: nessuno");
            return;
        }

        String filtroGioco = ricercaTavoliBean.verificaFiltroGiocoPresente()
                ? ricercaTavoliBean.fornisciTitoloGioco().trim()
                : "qualsiasi gioco";
        String filtroGiorno = ricercaTavoliBean.verificaFiltroGiornoPresente()
                ? ricercaTavoliBean.fornisciGiornoSettimana().fornisciNomeVisualizzato()
                : "qualsiasi giorno";

        InterazioneConsole.stampaMessaggio("Filtri: " + filtroGioco + " | " + filtroGiorno);
    }

    private RicercaTavoliBean creaRicercaTavoliBean() {
        InterazioneConsole.stampaSeparatore();
        InterazioneConsole.stampaMessaggio("Filtra tavoli");
        String titoloGioco = InterazioneConsole.leggiTesto(
                scanner,
                "Titolo gioco (invio per qualsiasi gioco): "
        );
        GiornoSettimana giornoSettimana = selezionaGiornoSettimana();

        if (titoloGioco.trim().isEmpty() && giornoSettimana == null) {
            return null;
        }

        return new RicercaTavoliBean(titoloGioco, giornoSettimana);
    }

    private GiornoSettimana selezionaGiornoSettimana() {
        GiornoSettimana[] giorni = GiornoSettimana.values();

        while (true) {
            InterazioneConsole.stampaMessaggio("Giorno:");
            for (int indice = 0; indice < giorni.length; indice++) {
                InterazioneConsole.stampaMessaggio((indice + 1) + ". " + giorni[indice].fornisciNomeVisualizzato());
            }
            InterazioneConsole.stampaMessaggio("0. Qualsiasi giorno");

            int scelta = InterazioneConsole.leggiIntero(scanner, "Seleziona giorno: ");
            if (scelta == 0) {
                return null;
            }
            if (scelta >= 1 && scelta <= giorni.length) {
                return giorni[scelta - 1];
            }

            InterazioneConsole.stampaSceltaNonValida();
        }
    }

}
