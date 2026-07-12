package com.questtable.controller;

import com.questtable.bean.LoginBean;
import com.questtable.bean.ProfiloUtenteBean;
import com.questtable.exception.InvalidCredentialsException;
import com.questtable.model.RuoloUtente;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LoginControllerApplicativoTest {
    private static final String USERNAME_CLIENTE = "matteo";
    private static final String CREDENZIALE_CLIENTE = "1234";

    private final List<String> sessioniAperte = new ArrayList<>();

    @AfterEach
    void chiudiSessioniAperte() {
        LoginControllerApplicativo loginControllerApplicativo = new LoginControllerApplicativo();
        for (String idSessione : sessioniAperte) {
            loginControllerApplicativo.effettuaLogout(idSessione);
        }
        sessioniAperte.clear();
    }

    @Test
    void loginClienteRestituisceProfilo() {
        LoginControllerApplicativo controller = new LoginControllerApplicativo();

        String idSessione = controller.effettuaLogin(new LoginBean(USERNAME_CLIENTE, CREDENZIALE_CLIENTE));
        sessioniAperte.add(idSessione);
        ProfiloUtenteBean profiloUtente = controller.fornisciProfiloUtente(idSessione);

        assertEquals(USERNAME_CLIENTE, profiloUtente.fornisciUsername());
        assertTrue(profiloUtente.verificaRuolo(RuoloUtente.CLIENTE));
    }

    @Test
    void loginRifiutaPasswordErrata() {
        LoginControllerApplicativo controller = new LoginControllerApplicativo();
        LoginBean loginBean = new LoginBean(USERNAME_CLIENTE, "sbagliata");

        InvalidCredentialsException exception = assertThrows(
                InvalidCredentialsException.class,
                () -> controller.effettuaLogin(loginBean)
        );

        assertEquals("Password non valida.", exception.getMessage());
    }

    @Test
    void loginRifiutaUtenteInesistente() {
        LoginControllerApplicativo controller = new LoginControllerApplicativo();
        LoginBean loginBean = new LoginBean("nessuno", CREDENZIALE_CLIENTE);

        InvalidCredentialsException exception = assertThrows(
                InvalidCredentialsException.class,
                () -> controller.effettuaLogin(loginBean)
        );

        assertEquals("Utente non registrato.", exception.getMessage());
    }

    @Test
    void loginRifiutaCredenzialiNonCompilate() {
        LoginControllerApplicativo controller = new LoginControllerApplicativo();

        InvalidCredentialsException exception = assertThrows(
                InvalidCredentialsException.class,
                () -> controller.effettuaLogin(null)
        );

        assertEquals("Credenziali non compilate correttamente.", exception.getMessage());
    }

    @Test
    void profiloRifiutaSessioneNonValida() {
        LoginControllerApplicativo controller = new LoginControllerApplicativo();

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> controller.fornisciProfiloUtente("sessione-assente")
        );

        assertEquals("Sessione non valida.", exception.getMessage());
    }
}
