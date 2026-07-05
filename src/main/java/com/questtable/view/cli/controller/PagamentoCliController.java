package com.questtable.view.cli.controller;

import com.questtable.bean.PagamentoBean;
import com.questtable.bean.PrenotazioneBean;
import com.questtable.bean.PreventivoBean;
import com.questtable.controller.QuestTableController;
import com.questtable.model.MetodoPagamento;

import java.util.Scanner;

public class PagamentoCliController {
    private final QuestTableController questTableController;
    private final Scanner scanner;
    private final String idSessione;
    private final PreventivoBean preventivoBean;

    public PagamentoCliController(QuestTableController questTableController, Scanner scanner,
                                  String idSessione, PreventivoBean preventivoBean) {
        this.questTableController = questTableController;
        this.scanner = scanner;
        this.idSessione = idSessione;
        this.preventivoBean = preventivoBean;
    }

    public boolean apri() {
        if (preventivoBean == null) {
            return false;
        }

        mostraPreventivo();
        MetodoPagamento metodoPagamento = selezionaMetodoPagamento();
        if (metodoPagamento == null) {
            return false;
        }

        boolean pagamentoConfermato = verificaConfermaPagamento();
        if (!pagamentoConfermato) {
            return false;
        }

        try {
            PrenotazioneBean prenotazioneBean = questTableController.registraPrenotazione(
                    idSessione,
                    creaPagamentoBean(metodoPagamento)
            );
            RichiestaPrenotazioneOkCliController richiestaPrenotazioneOkCliController =
                    new RichiestaPrenotazioneOkCliController(prenotazioneBean, scanner);
            richiestaPrenotazioneOkCliController.apri();
            return true;
        } catch (IllegalArgumentException | IllegalStateException exception) {
            InterazioneConsole.stampaMessaggio(exception.getMessage());
            return false;
        }
    }

    private void mostraPreventivo() {
        InterazioneConsole.stampaSeparatore();
        InterazioneConsole.stampaMessaggio("Preventivo");
        mostraRiepilogoPagamento();
        InterazioneConsole.stampaMessaggio("Punti fedelta previsti: "
                + preventivoBean.fornisciPuntiFedeltaPrevisti());
    }

    private void mostraRiepilogoPagamento() {
        InterazioneConsole.stampaMessaggio("Gioco: " + preventivoBean.fornisciTitoloGioco());
        InterazioneConsole.stampaMessaggio("Giorno: "
                + preventivoBean.fornisciGiornoSettimana().fornisciNomeVisualizzato());
        InterazioneConsole.stampaMessaggio("Orario: " + preventivoBean.fornisciFasciaOraria());
        InterazioneConsole.stampaMessaggio("Posti richiesti: "
                + preventivoBean.fornisciNumeroPostiRichiesti());
        InterazioneConsole.stampaMessaggio("Totale: "
                + InterazioneConsole.formattaImporto(preventivoBean.fornisciImportoTotale()));
    }

    private MetodoPagamento selezionaMetodoPagamento() {
        MetodoPagamento[] metodiPagamento = MetodoPagamento.values();

        while (true) {
            InterazioneConsole.stampaSeparatore();
            InterazioneConsole.stampaMessaggio("Pagamento");
            mostraRiepilogoPagamento();
            InterazioneConsole.stampaMessaggio("");
            InterazioneConsole.stampaMessaggio("Metodo di pagamento");

            for (int indice = 0; indice < metodiPagamento.length; indice++) {
                InterazioneConsole.stampaMessaggio((indice + 1) + ". "
                        + metodiPagamento[indice].fornisciNomeVisualizzato());
            }
            InterazioneConsole.stampaMessaggio("0. Torna ai dettagli");

            int scelta = InterazioneConsole.leggiIntero(scanner, "Seleziona metodo: ");
            if (scelta == 0) {
                return null;
            }
            if (scelta >= 1 && scelta <= metodiPagamento.length) {
                return metodiPagamento[scelta - 1];
            }

            InterazioneConsole.stampaSceltaNonValida();
        }
    }

    private boolean verificaConfermaPagamento() {
        while (true) {
            String conferma = InterazioneConsole.leggiTesto(
                    scanner,
                    "Scrivi paga per confermare oppure 0 per tornare ai dettagli: "
            );
            String confermaNormalizzata = conferma.trim();

            if ("0".equals(confermaNormalizzata)) {
                return false;
            }
            if ("paga".equalsIgnoreCase(confermaNormalizzata)) {
                return true;
            }

            InterazioneConsole.stampaSceltaNonValida();
        }
    }

    private PagamentoBean creaPagamentoBean(MetodoPagamento metodoPagamento) {
        return new PagamentoBean(
                preventivoBean.fornisciIdentificativoTavolo(),
                preventivoBean.fornisciNumeroPostiRichiesti(),
                preventivoBean.fornisciImportoTotale(),
                metodoPagamento,
                true
        );
    }
}
