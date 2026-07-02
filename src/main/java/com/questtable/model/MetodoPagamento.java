package com.questtable.model;

public enum MetodoPagamento {
    PAYPAL("PayPal"),
    CARTA_CREDITO("Carta di credito"),
    GOOGLE_PAY("Google Pay");

    private final String nomeVisualizzato;

    MetodoPagamento(String nomeVisualizzato) {
        this.nomeVisualizzato = nomeVisualizzato;
    }

    public String fornisciNomeVisualizzato() {
        return nomeVisualizzato;
    }
}
