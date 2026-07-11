package com.questtable.dao.demo;

import com.questtable.model.Notifica;
import com.questtable.model.TipoNotifica;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DemoNotificaDAOTest {
    private static final String DESTINATARIO_TEST = "utente-test-notifica";
    private static final String ALTRO_DESTINATARIO_TEST = "altro-utente-test-notifica";

    @Test
    void recuperaSoloNotificheNonLetteDelDestinatario() {
        DemoNotificaDAO notificaDAO = new DemoNotificaDAO();
        notificaDAO.salvaNuovaNotifica(new Notifica(
                DESTINATARIO_TEST,
                TipoNotifica.RICHIESTA_PRENOTAZIONE,
                "Prima notifica"
        ));
        notificaDAO.salvaNuovaNotifica(new Notifica(
                ALTRO_DESTINATARIO_TEST,
                TipoNotifica.RICHIESTA_PRENOTAZIONE,
                "Notifica di un altro utente"
        ));

        List<Notifica> notifiche = notificaDAO.recuperaNotificheNonLette(DESTINATARIO_TEST);

        assertEquals(1, notifiche.size());
        assertEquals("Prima notifica", notifiche.getFirst().fornisciMessaggio());
    }

    @Test
    void segnaComeLetteLeNotificheDelDestinatario() {
        DemoNotificaDAO notificaDAO = new DemoNotificaDAO();
        notificaDAO.salvaNuovaNotifica(new Notifica(
                DESTINATARIO_TEST,
                TipoNotifica.PRENOTAZIONE_CONFERMATA,
                "Notifica da leggere"
        ));

        notificaDAO.segnaNotificheComeLette(DESTINATARIO_TEST);

        assertTrue(notificaDAO.recuperaNotificheNonLette(DESTINATARIO_TEST).isEmpty());
    }
}
