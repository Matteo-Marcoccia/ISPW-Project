package com.questtable.controller;

import com.questtable.bean.ListaPrenotazioniBean;
import com.questtable.bean.ListaTavoliBean;
import com.questtable.bean.LoginBean;
import com.questtable.bean.PagamentoBean;
import com.questtable.bean.PrenotazioneBean;
import com.questtable.bean.PreventivoBean;
import com.questtable.bean.RicercaTavoliBean;
import com.questtable.bean.RichiestaPreventivoBean;
import com.questtable.exception.PaymentException;
import com.questtable.exception.PostiNonDisponibiliException;
import com.questtable.model.GiornoSettimana;
import com.questtable.model.MetodoPagamento;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PrenotaPostoControllerApplicativoTest {
    private static final String USERNAME_CLIENTE = "matteo";
    private static final String CREDENZIALE_CLIENTE = "1234";
    private static final String USERNAME_GESTORE = "admin";
    private static final String CREDENZIALE_GESTORE = "admin";
    private static final String TITOLO_CATAN = "Catan";

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
    void ricercaTavoliFiltraPerGiocoEGiorno() {
        PrenotaPostoControllerApplicativo controller = new PrenotaPostoControllerApplicativo();
        String idSessione = apriSessioneCliente();

        ListaTavoliBean listaTavoliBean = controller.fornisciTavoliDisponibili(
                idSessione,
                new RicercaTavoliBean(TITOLO_CATAN, GiornoSettimana.GIOVEDI)
        );

        assertTrue(listaTavoliBean.verificaPresenzaTavoli());
        assertEquals(TITOLO_CATAN, listaTavoliBean.fornisciTavoli().getFirst().fornisciTitoloGioco());
    }

    @Test
    void calcolaPreventivoPerTavoloDisponibile() {
        PrenotaPostoControllerApplicativo controller = new PrenotaPostoControllerApplicativo();
        RichiestaPreventivoBean richiestaPreventivoBean = new RichiestaPreventivoBean(1, 2);

        PreventivoBean preventivoBean = controller.calcolaPreventivo(richiestaPreventivoBean);

        assertEquals(1, preventivoBean.fornisciIdentificativoTavolo());
        assertEquals(2, preventivoBean.fornisciNumeroPostiRichiesti());
        assertEquals(24.0f, preventivoBean.fornisciImportoTotale());
        assertEquals(240, preventivoBean.fornisciPuntiFedeltaPrevisti());
    }

    @Test
    void calcolaPreventivoRifiutaRichiestaNonValida() {
        PrenotaPostoControllerApplicativo controller = new PrenotaPostoControllerApplicativo();
        RichiestaPreventivoBean richiestaPreventivoBean = new RichiestaPreventivoBean(1, 0);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> controller.calcolaPreventivo(richiestaPreventivoBean)
        );

        assertEquals("Richiesta preventivo non valida.", exception.getMessage());
    }

    @Test
    void calcolaPreventivoRifiutaTavoloAssente() {
        PrenotaPostoControllerApplicativo controller = new PrenotaPostoControllerApplicativo();
        RichiestaPreventivoBean richiestaPreventivoBean = new RichiestaPreventivoBean(99, 1);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> controller.calcolaPreventivo(richiestaPreventivoBean)
        );

        assertEquals("Tavolo non trovato.", exception.getMessage());
    }

    @Test
    void calcolaPreventivoRifiutaPostiNonDisponibili() {
        PrenotaPostoControllerApplicativo controller = new PrenotaPostoControllerApplicativo();
        RichiestaPreventivoBean richiestaPreventivoBean = new RichiestaPreventivoBean(1, 99);

        PostiNonDisponibiliException exception = assertThrows(
                PostiNonDisponibiliException.class,
                () -> controller.calcolaPreventivo(richiestaPreventivoBean)
        );

        assertEquals("Posti non disponibili per il tavolo selezionato.", exception.getMessage());
    }

    @Test
    void registraPrenotazioneDopoPagamentoValido() {
        PrenotaPostoControllerApplicativo controller = new PrenotaPostoControllerApplicativo();
        String idSessione = apriSessioneCliente();
        PagamentoBean pagamentoBean = new PagamentoBean(2, 1, 12.0f, MetodoPagamento.CARTA_CREDITO, true);

        PrenotazioneBean prenotazioneBean = controller.registraPrenotazione(idSessione, pagamentoBean);

        assertNotNull(prenotazioneBean);
        assertEquals(USERNAME_CLIENTE, prenotazioneBean.fornisciUsernameCliente());
        assertEquals(TITOLO_CATAN, prenotazioneBean.fornisciTitoloGioco());
    }

    @Test
    void registraPrenotazioneRifiutaPagamentoNonValido() {
        PrenotaPostoControllerApplicativo controller = new PrenotaPostoControllerApplicativo();
        String idSessione = apriSessioneCliente();
        PagamentoBean pagamentoBean = new PagamentoBean(2, 1, 12.0f, MetodoPagamento.CARTA_CREDITO, false);

        PaymentException exception = assertThrows(
                PaymentException.class,
                () -> controller.registraPrenotazione(idSessione, pagamentoBean)
        );

        assertEquals("Pagamento non valido.", exception.getMessage());
    }


    @Test
    void gestoreRecuperaPrenotazioniInAttesa() {
        PrenotaPostoControllerApplicativo controller = new PrenotaPostoControllerApplicativo();
        String idSessioneGestore = apriSessioneGestore();

        ListaPrenotazioniBean listaPrenotazioniBean = controller.fornisciPrenotazioniInAttesa(idSessioneGestore);

        assertFalse(listaPrenotazioniBean.verificaAssenzaPrenotazioni());
    }

    @Test
    void clienteRecuperaStoricoPrenotazioni() {
        PrenotaPostoControllerApplicativo controller = new PrenotaPostoControllerApplicativo();
        String idSessione = apriSessioneCliente();

        ListaPrenotazioniBean listaPrenotazioniBean = controller.fornisciPrenotazioniCliente(idSessione);

        assertFalse(listaPrenotazioniBean.verificaAssenzaPrenotazioni());
    }

    @Test
    void gestoreConfermaPrenotazione() {
        PrenotaPostoControllerApplicativo controller = new PrenotaPostoControllerApplicativo();
        String idSessioneGestore = apriSessioneGestore();

        controller.confermaPrenotazione(idSessioneGestore, 1);

        ListaPrenotazioniBean listaPrenotazioniBean = controller.fornisciPrenotazioniInAttesa(idSessioneGestore);
        assertTrue(listaPrenotazioniBean.fornisciPrenotazioni()
                .stream()
                .noneMatch(prenotazione -> prenotazione.fornisciIdentificativoPrenotazione() == 1));
    }

    private String apriSessioneCliente() {
        String idSessione = new LoginControllerApplicativo().effettuaLogin(
                new LoginBean(USERNAME_CLIENTE, CREDENZIALE_CLIENTE)
        );
        sessioniAperte.add(idSessione);
        return idSessione;
    }

    private String apriSessioneGestore() {
        String idSessione = new LoginControllerApplicativo().effettuaLogin(
                new LoginBean(USERNAME_GESTORE, CREDENZIALE_GESTORE)
        );
        sessioniAperte.add(idSessione);
        return idSessione;
    }
}

