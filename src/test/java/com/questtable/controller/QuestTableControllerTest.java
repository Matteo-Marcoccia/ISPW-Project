package com.questtable.controller;

import com.questtable.bean.ListaPrenotazioniBean;
import com.questtable.bean.ListaTavoliBean;
import com.questtable.bean.LoginBean;
import com.questtable.bean.PagamentoBean;
import com.questtable.bean.PrenotazioneBean;
import com.questtable.bean.PreventivoBean;
import com.questtable.bean.ProfiloUtenteBean;
import com.questtable.bean.RicercaTavoliBean;
import com.questtable.bean.RichiestaPreventivoBean;
import com.questtable.model.GiornoSettimana;
import com.questtable.model.MetodoPagamento;
import com.questtable.model.RuoloUtente;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class QuestTableControllerTest {
    private static final String USERNAME_CLIENTE = "matteo";
    private static final String CREDENZIALE_CLIENTE = "1234";
    private static final String USERNAME_GESTORE = "admin";
    private static final String CREDENZIALE_GESTORE = "admin";
    private static final String TITOLO_CATAN = "Catan";

    @Test
    void loginClienteRestituisceProfilo() {
        QuestTableController controller = new QuestTableController();

        String idSessione = controller.effettuaLogin(new LoginBean(USERNAME_CLIENTE, CREDENZIALE_CLIENTE));
        ProfiloUtenteBean profiloUtente = controller.fornisciProfiloUtente(idSessione);

        assertEquals(USERNAME_CLIENTE, profiloUtente.fornisciUsername());
        assertTrue(profiloUtente.verificaRuolo(RuoloUtente.CLIENTE));
    }

    @Test
    void loginRifiutaPasswordErrata() {
        QuestTableController controller = new QuestTableController();
        LoginBean loginBean = new LoginBean(USERNAME_CLIENTE, "sbagliata");

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> controller.effettuaLogin(loginBean)
        );

        assertEquals("Password non valida.", exception.getMessage());
    }

    @Test
    void ricercaTavoliFiltraPerGiocoEGiorno() {
        QuestTableController controller = new QuestTableController();
        String idSessione = controller.effettuaLogin(new LoginBean(USERNAME_CLIENTE, CREDENZIALE_CLIENTE));

        ListaTavoliBean listaTavoliBean = controller.fornisciTavoliDisponibili(
                idSessione,
                new RicercaTavoliBean(TITOLO_CATAN, GiornoSettimana.GIOVEDI)
        );

        assertTrue(listaTavoliBean.verificaPresenzaTavoli());
        assertEquals(TITOLO_CATAN, listaTavoliBean.fornisciTavoli().get(0).fornisciTitoloGioco());
    }

    @Test
    void calcolaPreventivoPerTavoloDisponibile() {
        QuestTableController controller = new QuestTableController();
        RichiestaPreventivoBean richiestaPreventivoBean = new RichiestaPreventivoBean(1, 2);

        PreventivoBean preventivoBean = controller.calcolaPreventivo(richiestaPreventivoBean);

        assertEquals(1, preventivoBean.fornisciIdentificativoTavolo());
        assertEquals(2, preventivoBean.fornisciNumeroPostiRichiesti());
        assertEquals(24.0f, preventivoBean.fornisciImportoTotale());
        assertEquals(240, preventivoBean.fornisciPuntiFedeltaPrevisti());
    }

    @Test
    void registraPrenotazioneDopoPagamentoValido() {
        QuestTableController controller = new QuestTableController();
        String idSessione = controller.effettuaLogin(new LoginBean(USERNAME_CLIENTE, CREDENZIALE_CLIENTE));
        PagamentoBean pagamentoBean = new PagamentoBean(2, 1, 12.0f, MetodoPagamento.CARTA_CREDITO, true);

        PrenotazioneBean prenotazioneBean = controller.registraPrenotazione(idSessione, pagamentoBean);

        assertNotNull(prenotazioneBean);
        assertEquals(USERNAME_CLIENTE, prenotazioneBean.fornisciUsernameCliente());
        assertEquals(TITOLO_CATAN, prenotazioneBean.fornisciTitoloGioco());
    }

    @Test
    void gestoreRecuperaPrenotazioniInAttesa() {
        QuestTableController controller = new QuestTableController();
        String idSessioneGestore = controller.effettuaLogin(new LoginBean(USERNAME_GESTORE, CREDENZIALE_GESTORE));

        ListaPrenotazioniBean listaPrenotazioniBean = controller.fornisciPrenotazioniInAttesa(idSessioneGestore);

        assertFalse(listaPrenotazioniBean.verificaAssenzaPrenotazioni());
    }
}
