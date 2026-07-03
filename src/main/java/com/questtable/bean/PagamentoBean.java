package com.questtable.bean;

import com.questtable.model.MetodoPagamento;

public class PagamentoBean {
    private final int idTavolo;
    private final int postiRichiesti;
    private final float importo;
    private final MetodoPagamento metodoPagamento;
    private final boolean pagamentoEffettuato;

    public PagamentoBean(int idTavolo, int postiRichiesti, float importo,
                         MetodoPagamento metodoPagamento, boolean pagamentoEffettuato) {
        this.idTavolo = idTavolo;
        this.postiRichiesti = postiRichiesti;
        this.importo = importo;
        this.metodoPagamento = metodoPagamento;
        this.pagamentoEffettuato = pagamentoEffettuato;
    }

    public int fornisciIdentificativoTavolo() {
        return idTavolo;
    }

    public int fornisciNumeroPostiRichiesti() {
        return postiRichiesti;
    }

    public float fornisciImporto() {
        return importo;
    }

    public boolean verificaPagamentoEffettuato() {
        return pagamentoEffettuato;
    }

    public boolean verificaDatiPagamentoValidi() {
        return idTavolo > 0
                && postiRichiesti > 0
                && importo > 0
                && metodoPagamento != null;
    }
}
