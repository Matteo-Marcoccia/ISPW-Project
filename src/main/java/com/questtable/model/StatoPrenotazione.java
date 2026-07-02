package com.questtable.model;

public enum StatoPrenotazione {
    IN_ATTESA("In attesa"),
    CONFERMATA("Confermata");

    private final String nomeVisualizzato;

    StatoPrenotazione(String nomeVisualizzato) {
        this.nomeVisualizzato = nomeVisualizzato;
    }

    public String fornisciNomeVisualizzato() {
        return nomeVisualizzato;
    }
}
