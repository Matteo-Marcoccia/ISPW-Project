package com.questtable.view.cli.controller;

import com.questtable.bean.LoginBean;
import com.questtable.bean.ProfiloUtenteBean;
import com.questtable.controller.QuestTableController;

import java.util.Scanner;

public class LoginCliController {
    private final QuestTableController questTableController;
    private final Scanner scanner;

    public LoginCliController(QuestTableController questTableController, Scanner scanner) {
        this.questTableController = questTableController;
        this.scanner = scanner;
    }

    public String effettuaLogin() {
        String username = InterazioneConsole.leggiTesto(scanner, "Username: ");
        String password = InterazioneConsole.leggiTesto(scanner, "Password: ");

        try {
            String idSessione = questTableController.effettuaLogin(new LoginBean(username, password));
            ProfiloUtenteBean profiloUtente = questTableController.fornisciProfiloUtente(idSessione);
            InterazioneConsole.stampaMessaggio(
                    "Accesso effettuato. Bentornato, " + profiloUtente.fornisciUsername() + ".");
            return idSessione;
        } catch (IllegalArgumentException | IllegalStateException exception) {
            InterazioneConsole.stampaMessaggio(exception.getMessage());
            return null;
        }
    }
}
