package com.questtable.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ModelBehaviorTest {
    private static final String TITOLO_CATAN = "Catan";

    @Test
    void sessioneTavoloPrenotaPostiDisponibili() {
        Gioco gioco = new Gioco(TITOLO_CATAN, "/catan.png");
        SessioneTavolo sessioneTavolo = new SessioneTavolo(
                1,
                gioco,
                4,
                3,
                GiornoSettimana.GIOVEDI,
                "18:00 - 20:00",
                12.0f
        );

        assertFalse(sessioneTavolo.verificaPostiNonPrenotabili(2));
        assertTrue(sessioneTavolo.prenotaPosti(2));
        assertEquals(1, sessioneTavolo.fornisciNumeroPostiDisponibili());
    }

    @Test
    void sessioneTavoloRifiutaPostiNonPrenotabili() {
        Gioco gioco = new Gioco(TITOLO_CATAN, "/catan.png");
        SessioneTavolo sessioneTavolo = new SessioneTavolo(
                1,
                gioco,
                4,
                1,
                GiornoSettimana.GIOVEDI,
                "18:00 - 20:00",
                12.0f
        );

        assertTrue(sessioneTavolo.verificaPostiNonPrenotabili(2));
        assertFalse(sessioneTavolo.prenotaPosti(2));
        assertEquals(1, sessioneTavolo.fornisciNumeroPostiDisponibili());
    }

    @Test
    void prenotazioneNasceInAttesaEPoiVieneConfermata() {
        Cliente cliente = new Cliente("matteo", "1234", 0);
        Gioco gioco = new Gioco(TITOLO_CATAN, "/catan.png");
        SessioneTavolo sessioneTavolo = new SessioneTavolo(
                1,
                gioco,
                4,
                2,
                GiornoSettimana.GIOVEDI,
                "18:00 - 20:00",
                12.0f
        );
        Prenotazione prenotazione = new Prenotazione(1, cliente, sessioneTavolo, 2, 24.0f);

        assertEquals(StatoPrenotazione.IN_ATTESA, prenotazione.fornisciStatoCorrente());
        assertEquals(240, prenotazione.calcolaPuntiFedeltaDaAccreditare());

        prenotazione.confermaPrenotazione();

        assertEquals(StatoPrenotazione.CONFERMATA, prenotazione.fornisciStatoCorrente());
    }
}
