package com.questtable.controller;

import com.questtable.bean.LoginBean;
import com.questtable.bean.ProfiloUtenteBean;
import com.questtable.model.RuoloUtente;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LoginControllerApplicativoTest {
    private static final String USERNAME_CLIENTE = "matteo";
    private static final String CREDENZIALE_CLIENTE = "1234";

    @Test
    void loginClienteRestituisceProfilo() {
        LoginControllerApplicativo controller = new LoginControllerApplicativo();

        String idSessione = controller.effettuaLogin(new LoginBean(USERNAME_CLIENTE, CREDENZIALE_CLIENTE));
        ProfiloUtenteBean profiloUtente = controller.fornisciProfiloUtente(idSessione);

        assertEquals(USERNAME_CLIENTE, profiloUtente.fornisciUsername());
        assertTrue(profiloUtente.verificaRuolo(RuoloUtente.CLIENTE));
    }

    @Test
    void loginRifiutaPasswordErrata() {
        LoginControllerApplicativo controller = new LoginControllerApplicativo();
        LoginBean loginBean = new LoginBean(USERNAME_CLIENTE, "sbagliata");

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> controller.effettuaLogin(loginBean)
        );

        assertEquals("Password non valida.", exception.getMessage());
    }

    @Test
    void loginRifiutaUtenteInesistente() {
        LoginControllerApplicativo controller = new LoginControllerApplicativo();
        LoginBean loginBean = new LoginBean("nessuno", CREDENZIALE_CLIENTE);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> controller.effettuaLogin(loginBean)
        );

        assertEquals("Utente non registrato.", exception.getMessage());
    }

    @Test
    void loginRifiutaCredenzialiNonCompilate() {
        LoginControllerApplicativo controller = new LoginControllerApplicativo();

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
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
