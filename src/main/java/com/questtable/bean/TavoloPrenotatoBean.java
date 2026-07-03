package com.questtable.bean;

import com.questtable.model.GiornoSettimana;

public class TavoloPrenotatoBean {
    private final String titoloGioco;
    private final GiornoSettimana giornoAttivita;
    private final String fasciaOrariaAttivita;
    private final int postiPrenotati;
    private final float importoTotale;

    public TavoloPrenotatoBean(String titoloGioco, GiornoSettimana giornoAttivita,
                               String fasciaOrariaAttivita, int postiPrenotati, float importoTotale) {
        this.titoloGioco = titoloGioco;
        this.giornoAttivita = giornoAttivita;
        this.fasciaOrariaAttivita = fasciaOrariaAttivita;
        this.postiPrenotati = postiPrenotati;
        this.importoTotale = importoTotale;
    }

    public String fornisciTitoloGioco() {
        return titoloGioco;
    }

    public GiornoSettimana fornisciGiornoAttivita() {
        return giornoAttivita;
    }

    public String fornisciFasciaOrariaAttivita() {
        return fasciaOrariaAttivita;
    }

    public int fornisciNumeroPostiPrenotati() {
        return postiPrenotati;
    }

    public float fornisciImportoTotale() {
        return importoTotale;
    }
}
