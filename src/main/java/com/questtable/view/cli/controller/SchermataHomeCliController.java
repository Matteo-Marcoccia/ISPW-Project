package com.questtable.view.cli.controller;

import com.questtable.bean.ProfiloUtenteBean;
import com.questtable.controller.LoginControllerApplicativo;
import com.questtable.controller.QuestTableController;
import com.questtable.model.RuoloUtente;

import java.util.Scanner;

public class SchermataHomeCliController {
    private static final String TITOLO_APPLICAZIONE = "QuestTable";
    private static final String VOCE_ESCI = "0. Esci";

    private final QuestTableController questTableController = new QuestTableController();
    private final LoginControllerApplicativo loginControllerApplicativo = new LoginControllerApplicativo();
    private final Scanner scanner;

    private String idSessione;

    public SchermataHomeCliController(Scanner scanner) {
        this.scanner = scanner;
    }

    public void avvia() {
        boolean applicazioneAttiva = true;

        while (applicazioneAttiva) {
            mostraHome();
            int scelta = leggiScelta();
            applicazioneAttiva = gestisciScelta(scelta);
        }

        InterazioneConsole.stampaMessaggio("Chiusura di " + TITOLO_APPLICAZIONE + ".");
    }

    private void mostraHome() {
        InterazioneConsole.stampaSeparatore();
        InterazioneConsole.stampaMessaggio(TITOLO_APPLICAZIONE);
        InterazioneConsole.stampaMessaggio("Prenota un posto ai tavoli da gioco disponibili.");
        mostraProfiloCorrente();
        InterazioneConsole.stampaSeparatore();

        if (idSessione == null) {
            InterazioneConsole.stampaMessaggio("1. Accedi");
            InterazioneConsole.stampaMessaggio("2. Prenota posto al tavolo");
            InterazioneConsole.stampaMessaggio(VOCE_ESCI);
            return;
        }

        ProfiloUtenteBean profiloUtente = loginControllerApplicativo.fornisciProfiloUtente(idSessione);
        if (profiloUtente.verificaRuolo(RuoloUtente.CLIENTE)) {
            InterazioneConsole.stampaMessaggio("1. Prenota posto al tavolo");
            InterazioneConsole.stampaMessaggio("2. Storico prenotazioni");
            InterazioneConsole.stampaMessaggio("3. Logout");
            InterazioneConsole.stampaMessaggio(VOCE_ESCI);
            return;
        }

        InterazioneConsole.stampaMessaggio("1. Conferma prenotazioni in attesa");
        InterazioneConsole.stampaMessaggio("2. Logout");
        InterazioneConsole.stampaMessaggio(VOCE_ESCI);
    }

    private void mostraProfiloCorrente() {
        if (idSessione == null) {
            InterazioneConsole.stampaMessaggio("Profilo: ospite");
            return;
        }

        ProfiloUtenteBean profiloUtente = loginControllerApplicativo.fornisciProfiloUtente(idSessione);
        if (profiloUtente.verificaRuolo(RuoloUtente.CLIENTE)) {
            InterazioneConsole.stampaMessaggio("Profilo: " + profiloUtente.fornisciUsername()
                    + " | " + profiloUtente.fornisciPuntiFedelta() + " punti fedelta");
            return;
        }

        InterazioneConsole.stampaMessaggio("Profilo: " + profiloUtente.fornisciUsername() + " | Gestore");
    }

    private boolean gestisciScelta(int scelta) {
        if (idSessione == null) {
            return gestisciSceltaOspite(scelta);
        }

        ProfiloUtenteBean profiloUtente = loginControllerApplicativo.fornisciProfiloUtente(idSessione);
        if (profiloUtente.verificaRuolo(RuoloUtente.CLIENTE)) {
            return gestisciSceltaCliente(scelta);
        }

        return gestisciSceltaGestore(scelta);
    }

    private boolean gestisciSceltaOspite(int scelta) {
        return switch (scelta) {
            case 1 -> {
                apriLogin();
                yield true;
            }
            case 2 -> {
                InterazioneConsole.stampaMessaggio("Per prenotare devi prima accedere.");
                apriLogin();
                if (verificaClienteCollegato()) {
                    apriListaTavoli();
                }
                yield true;
            }
            case 0 -> false;
            default -> {
                mostraSceltaNonValida();
                yield true;
            }
        };
    }

    private boolean gestisciSceltaCliente(int scelta) {
        return switch (scelta) {
            case 1 -> {
                apriListaTavoli();
                yield true;
            }
            case 2 -> {
                apriStoricoPrenotazioni();
                yield true;
            }
            case 3 -> {
                effettuaLogout();
                yield true;
            }
            case 0 -> false;
            default -> {
                mostraSceltaNonValida();
                yield true;
            }
        };
    }

    private boolean gestisciSceltaGestore(int scelta) {
        return switch (scelta) {
            case 1 -> {
                apriConfermaPrenotazioni();
                yield true;
            }
            case 2 -> {
                effettuaLogout();
                yield true;
            }
            case 0 -> false;
            default -> {
                mostraSceltaNonValida();
                yield true;
            }
        };
    }

    private void apriLogin() {
        LoginCliController loginCliController = new LoginCliController(loginControllerApplicativo, scanner);
        String nuovaSessione = loginCliController.effettuaLogin();
        if (nuovaSessione != null) {
            idSessione = nuovaSessione;
            mostraAvvisoPrenotazioniInAttesa();
        }
    }

    private void apriListaTavoli() {
        ListaTavoliCliController listaTavoliCliController =
                new ListaTavoliCliController(questTableController, scanner, idSessione);
        listaTavoliCliController.apri();
    }

    private void apriStoricoPrenotazioni() {
        StoricoPrenotazioniCliController storicoPrenotazioniCliController =
                new StoricoPrenotazioniCliController(questTableController, scanner, idSessione);
        storicoPrenotazioniCliController.apri();
    }

    private void apriConfermaPrenotazioni() {
        ConfermaPrenotazioniCliController confermaPrenotazioniCliController =
                new ConfermaPrenotazioniCliController(questTableController, scanner, idSessione);
        confermaPrenotazioniCliController.apri();
    }

    private void effettuaLogout() {
        loginControllerApplicativo.effettuaLogout(idSessione);
        idSessione = null;
        InterazioneConsole.stampaMessaggio("Logout effettuato.");
    }

    private int leggiScelta() {
        return InterazioneConsole.leggiIntero(scanner, "Scelta: ");
    }

    private boolean verificaClienteCollegato() {
        if (idSessione == null) {
            return false;
        }

        ProfiloUtenteBean profiloUtente = loginControllerApplicativo.fornisciProfiloUtente(idSessione);
        return profiloUtente.verificaRuolo(RuoloUtente.CLIENTE);
    }

    private void mostraSceltaNonValida() {
        InterazioneConsole.stampaSceltaNonValida();
    }

    private void mostraAvvisoPrenotazioniInAttesa() {
        ProfiloUtenteBean profiloUtente = loginControllerApplicativo.fornisciProfiloUtente(idSessione);
        if (!profiloUtente.verificaRuolo(RuoloUtente.GESTORE)
                || !questTableController.verificaPrenotazioniInAttesa(idSessione)) {
            return;
        }

        InterazioneConsole.stampaSeparatore();
        InterazioneConsole.stampaMessaggio("Sono presenti prenotazioni in attesa di conferma.");
    }
}
