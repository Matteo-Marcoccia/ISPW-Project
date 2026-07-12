package com.questtable.view.cli.controller;

import com.questtable.bean.LoginBean;
import com.questtable.bean.ProfiloUtenteBean;
import com.questtable.controller.LoginControllerApplicativo;

import java.util.Scanner;

public class LoginCliController {
    private final LoginControllerApplicativo loginControllerApplicativo;
    private final Scanner scanner;

    public LoginCliController(LoginControllerApplicativo loginControllerApplicativo, Scanner scanner) {
        this.loginControllerApplicativo = loginControllerApplicativo;
        this.scanner = scanner;
    }

    public String effettuaLogin() {
        String username = InterazioneConsole.leggiTesto(scanner, "Username: ");
        String password = InterazioneConsole.leggiTesto(scanner, "Password: ");

        try {
            String idSessione = loginControllerApplicativo.effettuaLogin(new LoginBean(username, password));
            ProfiloUtenteBean profiloUtente = loginControllerApplicativo.fornisciProfiloUtente(idSessione);
            InterazioneConsole.stampaMessaggio(
                    "Accesso effettuato. Bentornato, " + profiloUtente.fornisciUsername() + ".");
            return idSessione;
        } catch (IllegalArgumentException | IllegalStateException exception) {
            InterazioneConsole.stampaMessaggio(exception.getMessage());
            return null;
        }
    }
}
