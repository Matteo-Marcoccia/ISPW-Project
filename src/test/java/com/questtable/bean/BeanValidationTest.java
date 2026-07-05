package com.questtable.bean;

import com.questtable.model.GiornoSettimana;
import com.questtable.model.MetodoPagamento;
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

        assertTrue(listaTavoliBean.verificaPresenzaTavoli());
        assertThrows(UnsupportedOperationException.class, () -> listaTavoliBean.fornisciTavoli().clear());
    }

    @Test
    void ricercaTavoliBeanRiconosceFiltriPresenti() {
        RicercaTavoliBean ricercaTavoliBean = new RicercaTavoliBean("Catan", GiornoSettimana.SABATO);

        assertTrue(ricercaTavoliBean.verificaFiltroGiocoPresente());
        assertTrue(ricercaTavoliBean.verificaFiltroGiornoPresente());
    }
}
