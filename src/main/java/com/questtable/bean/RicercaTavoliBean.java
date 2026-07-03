package com.questtable.bean;

import com.questtable.model.GiornoSettimana;

public class RicercaTavoliBean {
    private final String titoloGioco;
    private final GiornoSettimana giornoSettimana;

    public RicercaTavoliBean(String titoloGioco, GiornoSettimana giornoSettimana) {
        this.titoloGioco = titoloGioco;
        this.giornoSettimana = giornoSettimana;
    }

    public String fornisciTitoloGioco() {
        return titoloGioco;
    }

    public GiornoSettimana fornisciGiornoSettimana() {
        return giornoSettimana;
    }

    public boolean verificaFiltroGiocoPresente() {
        return titoloGioco != null && !titoloGioco.trim().isEmpty();
    }

    public boolean verificaFiltroGiornoPresente() {
        return giornoSettimana != null;
    }

}
