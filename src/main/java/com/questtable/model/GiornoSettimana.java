package com.questtable.model;

public enum GiornoSettimana {
    LUNEDI("Lunedi"),
    MARTEDI("Martedi"),
    MERCOLEDI("Mercoledi"),
    GIOVEDI("Giovedi"),
    VENERDI("Venerdi"),
    SABATO("Sabato"),
    DOMENICA("Domenica");

    private final String nomeVisualizzato;

    GiornoSettimana(String nomeVisualizzato) {
        this.nomeVisualizzato = nomeVisualizzato;
    }

    public String fornisciNomeVisualizzato() {
        return nomeVisualizzato;
    }
}
