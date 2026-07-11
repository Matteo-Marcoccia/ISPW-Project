package com.questtable.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;

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
    void sessioneTavoloRifiutaQuantitaNullaONegativa() {
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

        assertTrue(sessioneTavolo.verificaPostiNonPrenotabili(0));
        assertTrue(sessioneTavolo.verificaPostiNonPrenotabili(-1));
        assertFalse(sessioneTavolo.prenotaPosti(0));
        assertFalse(sessioneTavolo.prenotaPosti(-1));
        assertEquals(3, sessioneTavolo.fornisciNumeroPostiDisponibili());
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

        prenotazione.confermaPrenotazione();

        assertEquals(StatoPrenotazione.CONFERMATA, prenotazione.fornisciStatoCorrente());
    }

    @Test
    void regolaPuntiFedeltaCalcolaPuntiDaImporto() {
        assertEquals(240, RegolaPuntiFedelta.calcolaPuntiPer(24.0f));
    }

    @Test
    void clienteAggiornaPuntiSenzaScendereSottoZero() {
        Cliente cliente = new Cliente("matteo", "1234", 10);

        cliente.accreditaPuntiFedelta(15);
        cliente.accreditaPuntiFedelta(-5);
        assertEquals(25, cliente.fornisciPuntiFedelta());

        cliente.aggiornaPuntiFedelta(-100);
        assertEquals(0, cliente.fornisciPuntiFedelta());
    }

    @Test
    void notificaNasceNonLettaEPuoEssereSegnataComeLetta() {
        Notifica notifica = new Notifica(
                "admin",
                TipoNotifica.RICHIESTA_PRENOTAZIONE,
                "Nuova richiesta di prenotazione."
        );

        assertEquals("admin", notifica.fornisciUsernameDestinatario());
        assertEquals(TipoNotifica.RICHIESTA_PRENOTAZIONE, notifica.fornisciTipoNotifica());
        assertEquals("Nuova richiesta di prenotazione.", notifica.fornisciMessaggio());
        assertTrue(notifica.verificaNonLetta());

        notifica.segnaComeLetta();

        assertFalse(notifica.verificaNonLetta());
    }

    @Test
    void prenotazioneRicostruitaEsponeDatiCollegati() {
        Prenotazione prenotazione = creaPrenotazioneRicostruita();

        assertEquals(9, prenotazione.fornisciIdentificativo());
        assertEquals("matteo", prenotazione.fornisciUsernameCliente());
        assertEquals(TITOLO_CATAN, prenotazione.fornisciTitoloGiocoPrenotato());
        assertEquals(4, prenotazione.fornisciIdentificativoTavoloPrenotato());
        assertEquals(GiornoSettimana.SABATO, prenotazione.fornisciGiornoAttivitaPrenotata());
        assertEquals("21:00 - 23:00", prenotazione.fornisciFasciaOrariaAttivitaPrenotata());
        assertEquals(LocalDate.of(2026, Month.JULY, 8), prenotazione.fornisciDataPrenotazione());
        assertEquals(LocalTime.of(12, 30), prenotazione.fornisciOraPrenotazione());
        assertEquals(2, prenotazione.fornisciNumeroPostiPrenotati());
        assertEquals(24.0f, prenotazione.fornisciImportoTotale());
        assertEquals(StatoPrenotazione.CONFERMATA, prenotazione.fornisciStatoCorrente());
    }

    private Prenotazione creaPrenotazioneRicostruita() {
        Cliente cliente = new Cliente("matteo", "1234", 0);
        Gioco gioco = new Gioco(TITOLO_CATAN, "/catan.png");
        SessioneTavolo sessioneTavolo = new SessioneTavolo(
                4,
                gioco,
                4,
                2,
                GiornoSettimana.SABATO,
                "21:00 - 23:00",
                12.0f
        );

        return new Prenotazione(
                9,
                cliente,
                sessioneTavolo,
                new DatiPrenotazione(
                        LocalDate.of(2026, Month.JULY, 8),
                        LocalTime.of(12, 30),
                        StatoPrenotazione.CONFERMATA
                ),
                2,
                24.0f
        );
    }
}
