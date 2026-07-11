package com.questtable.bean;

import com.questtable.model.GiornoSettimana;
import com.questtable.model.MetodoPagamento;
import com.questtable.model.RuoloUtente;
import com.questtable.model.StatoPrenotazione;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BeanValidationTest {
    private static final String USERNAME_CLIENTE = "matteo";
    private static final String CREDENZIALE_CLIENTE = "1234";
    private static final String TITOLO_CATAN = "Catan";

    @Test
    void loginBeanRiconosceCampiCompilati() {
        LoginBean loginBean = new LoginBean(USERNAME_CLIENTE, CREDENZIALE_CLIENTE);

        assertEquals(USERNAME_CLIENTE, loginBean.fornisciUsername());
        assertEquals(CREDENZIALE_CLIENTE, loginBean.fornisciPassword());
        assertTrue(loginBean.verificaCampiCompilati());
    }

    @Test
    void loginBeanRifiutaCampiVuoti() {
        LoginBean loginBean = new LoginBean(" ", "1234");

        assertFalse(loginBean.verificaCampiCompilati());
    }

    @Test
    void loginBeanRifiutaPasswordVuota() {
        LoginBean loginBean = new LoginBean(USERNAME_CLIENTE, " ");

        assertFalse(loginBean.verificaCampiCompilati());
    }

    @Test
    void pagamentoBeanValidaDatiMinimi() {
        PagamentoBean pagamentoBean = new PagamentoBean(1, 2, 24.0f, MetodoPagamento.CARTA_CREDITO, true);

        assertEquals(1, pagamentoBean.fornisciIdentificativoTavolo());
        assertEquals(2, pagamentoBean.fornisciNumeroPostiRichiesti());
        assertEquals(24.0f, pagamentoBean.fornisciImporto());
        assertTrue(pagamentoBean.verificaPagamentoEffettuato());
        assertTrue(pagamentoBean.verificaDatiPagamentoValidi());
    }

    @Test
    void pagamentoBeanRifiutaMetodoAssente() {
        PagamentoBean pagamentoBean = new PagamentoBean(1, 2, 24.0f, null, true);

        assertFalse(pagamentoBean.verificaDatiPagamentoValidi());
    }

    @Test
    void pagamentoBeanRifiutaDatiNumericiNonValidi() {
        PagamentoBean tavoloNonValido = new PagamentoBean(0, 2, 24.0f, MetodoPagamento.PAYPAL, true);
        PagamentoBean postiNonValidi = new PagamentoBean(1, 0, 24.0f, MetodoPagamento.PAYPAL, true);
        PagamentoBean importoNonValido = new PagamentoBean(1, 2, 0.0f, MetodoPagamento.PAYPAL, true);
        PagamentoBean pagamentoNonEffettuato = new PagamentoBean(1, 2, 24.0f, MetodoPagamento.PAYPAL, false);

        assertFalse(tavoloNonValido.verificaDatiPagamentoValidi());
        assertFalse(postiNonValidi.verificaDatiPagamentoValidi());
        assertFalse(importoNonValido.verificaDatiPagamentoValidi());
        assertFalse(pagamentoNonEffettuato.verificaPagamentoEffettuato());
    }

    @Test
    void listaTavoliEsponeCopiaNonModificabile() {
        InfoTavoloBean tavolo = new InfoTavoloBean(
                1,
                TITOLO_CATAN,
                "/catan.png",
                new PostiTavoloBean(4, 2),
                GiornoSettimana.GIOVEDI,
                "18:00 - 20:00",
                12.0f
        );
        ListaTavoliBean listaTavoliBean = new ListaTavoliBean(List.of(tavolo));
        List<InfoTavoloBean> tavoliNonModificabili = listaTavoliBean.fornisciTavoli();

        assertTrue(listaTavoliBean.verificaPresenzaTavoli());
        assertThrows(UnsupportedOperationException.class, tavoliNonModificabili::clear);
    }

    @Test
    void ricercaTavoliBeanRiconosceFiltriPresenti() {
        RicercaTavoliBean ricercaTavoliBean = new RicercaTavoliBean("Catan", GiornoSettimana.SABATO);

        assertTrue(ricercaTavoliBean.verificaFiltroGiocoPresente());
        assertTrue(ricercaTavoliBean.verificaFiltroGiornoPresente());
    }

    @Test
    void ricercaTavoliBeanRiconosceFiltriAssenti() {
        RicercaTavoliBean ricercaTavoliBean = new RicercaTavoliBean(" ", null);

        assertFalse(ricercaTavoliBean.verificaFiltroGiocoPresente());
        assertFalse(ricercaTavoliBean.verificaFiltroGiornoPresente());
    }

    @Test
    void infoTavoloBeanEsponeDatiDelTavolo() {
        InfoTavoloBean tavolo = new InfoTavoloBean(
                7,
                TITOLO_CATAN,
                "/catan.png",
                new PostiTavoloBean(4, 3),
                GiornoSettimana.GIOVEDI,
                "18:00 - 20:00",
                12.0f
        );

        assertEquals(7, tavolo.fornisciIdentificativoTavolo());
        assertEquals(TITOLO_CATAN, tavolo.fornisciTitoloGioco());
        assertEquals("/catan.png", tavolo.fornisciPercorsoImmagine());
        assertEquals(4, tavolo.fornisciNumeroPostiTotali());
        assertEquals(3, tavolo.fornisciNumeroPostiDisponibili());
        assertEquals(GiornoSettimana.GIOVEDI, tavolo.fornisciGiornoSettimana());
        assertEquals("18:00 - 20:00", tavolo.fornisciFasciaOraria());
        assertEquals(12.0f, tavolo.fornisciQuotaPartecipazione());
    }

    @Test
    void prenotazioneBeanEsponeDatiDellaPrenotazione() {
        TavoloPrenotatoBean tavoloPrenotato = new TavoloPrenotatoBean(
                TITOLO_CATAN,
                GiornoSettimana.GIOVEDI,
                "18:00 - 20:00",
                2,
                24.0f
        );
        PrenotazioneBean prenotazioneBean = new PrenotazioneBean(
                3,
                USERNAME_CLIENTE,
                tavoloPrenotato,
                "08/07/2026",
                "12:30",
                StatoPrenotazione.IN_ATTESA
        );

        assertEquals(3, prenotazioneBean.fornisciIdentificativoPrenotazione());
        assertEquals(USERNAME_CLIENTE, prenotazioneBean.fornisciUsernameCliente());
        assertEquals(TITOLO_CATAN, prenotazioneBean.fornisciTitoloGioco());
        assertEquals(GiornoSettimana.GIOVEDI, prenotazioneBean.fornisciGiornoAttivita());
        assertEquals("18:00 - 20:00", prenotazioneBean.fornisciFasciaOrariaAttivita());
        assertEquals("08/07/2026", prenotazioneBean.fornisciDataPrenotazione());
        assertEquals("12:30", prenotazioneBean.fornisciOraPrenotazione());
        assertEquals(2, prenotazioneBean.fornisciNumeroPostiPrenotati());
        assertEquals(24.0f, prenotazioneBean.fornisciImportoTotale());
        assertEquals(StatoPrenotazione.IN_ATTESA, prenotazioneBean.fornisciStatoPrenotazione());
    }

    @Test
    void listaPrenotazioniEsponeCopiaNonModificabile() {
        PrenotazioneBean prenotazioneBean = new PrenotazioneBean(
                1,
                USERNAME_CLIENTE,
                new TavoloPrenotatoBean(TITOLO_CATAN, GiornoSettimana.GIOVEDI, "18:00 - 20:00", 1, 12.0f),
                "08/07/2026",
                "12:30",
                StatoPrenotazione.CONFERMATA
        );
        ListaPrenotazioniBean listaPrenotazioniBean = new ListaPrenotazioniBean(List.of(prenotazioneBean));
        List<PrenotazioneBean> prenotazioniNonModificabili = listaPrenotazioniBean.fornisciPrenotazioni();

        assertFalse(listaPrenotazioniBean.verificaAssenzaPrenotazioni());
        assertThrows(UnsupportedOperationException.class, prenotazioniNonModificabili::clear);
    }

    @Test
    void listaNotificheEsponeCopiaNonModificabile() {
        ListaNotificheBean listaNotificheBean = new ListaNotificheBean(List.of("Prima notifica", "Seconda notifica"));
        List<String> notificheNonModificabili = listaNotificheBean.fornisciMessaggi();

        assertFalse(listaNotificheBean.verificaAssenzaNotifiche());
        assertEquals(2, notificheNonModificabili.size());
        assertThrows(UnsupportedOperationException.class, notificheNonModificabili::clear);
    }

    @Test
    void listaNotificheRiconosceAssenzaMessaggi() {
        ListaNotificheBean listaNotificheBean = new ListaNotificheBean(List.of());

        assertTrue(listaNotificheBean.verificaAssenzaNotifiche());
    }

    @Test
    void profiloUtenteBeanRiconosceRuoloEPunti() {
        ProfiloUtenteBean profiloUtenteBean = new ProfiloUtenteBean(USERNAME_CLIENTE, RuoloUtente.CLIENTE, 120);

        assertEquals(USERNAME_CLIENTE, profiloUtenteBean.fornisciUsername());
        assertEquals(120, profiloUtenteBean.fornisciPuntiFedelta());
        assertTrue(profiloUtenteBean.verificaRuolo(RuoloUtente.CLIENTE));
        assertFalse(profiloUtenteBean.verificaRuolo(RuoloUtente.GESTORE));
    }

    @Test
    void preventivoBeanEsponeDettagliDiPagamento() {
        PreventivoBean preventivoBean = new PreventivoBean(
                1,
                TITOLO_CATAN,
                GiornoSettimana.GIOVEDI,
                "18:00 - 20:00",
                2,
                24.0f,
                240
        );

        assertEquals(1, preventivoBean.fornisciIdentificativoTavolo());
        assertEquals(TITOLO_CATAN, preventivoBean.fornisciTitoloGioco());
        assertEquals(GiornoSettimana.GIOVEDI, preventivoBean.fornisciGiornoSettimana());
        assertEquals("18:00 - 20:00", preventivoBean.fornisciFasciaOraria());
        assertEquals(2, preventivoBean.fornisciNumeroPostiRichiesti());
        assertEquals(24.0f, preventivoBean.fornisciImportoTotale());
        assertEquals(240, preventivoBean.fornisciPuntiFedeltaPrevisti());
    }

    @Test
    void enumEspongonoNomeVisualizzato() {
        assertEquals("Carta di credito", MetodoPagamento.CARTA_CREDITO.fornisciNomeVisualizzato());
        assertEquals("In attesa", StatoPrenotazione.IN_ATTESA.fornisciNomeVisualizzato());
    }
}
