package com.questtable.bean;

import com.questtable.model.GiornoSettimana;

public class InfoTavoloBean {
    private final int idTavolo;
    private final String titoloGioco;
    private final String percorsoImmagine;
    private final int postiTotali;
    private final int postiDisponibili;
    private final GiornoSettimana giornoSettimana;
    private final String fasciaOraria;
    private final float quota;

    public InfoTavoloBean(int idTavolo, String titoloGioco, String percorsoImmagine,
                          int postiTotali, int postiDisponibili, GiornoSettimana giornoSettimana,
                          String fasciaOraria, float quota) {
        this.idTavolo = idTavolo;
        this.titoloGioco = titoloGioco;
        this.percorsoImmagine = percorsoImmagine;
        this.postiTotali = postiTotali;
        this.postiDisponibili = postiDisponibili;
        this.giornoSettimana = giornoSettimana;
        this.fasciaOraria = fasciaOraria;
        this.quota = quota;
    }

    public int fornisciIdentificativoTavolo() {
        return idTavolo;
    }

    public String fornisciTitoloGioco() {
        return titoloGioco;
    }

    public String fornisciPercorsoImmagine() {
        return percorsoImmagine;
    }

    public int fornisciNumeroPostiTotali() {
        return postiTotali;
    }

    public int fornisciNumeroPostiDisponibili() {
        return postiDisponibili;
    }

    public GiornoSettimana fornisciGiornoSettimana() {
        return giornoSettimana;
    }

    public String fornisciFasciaOraria() {
        return fasciaOraria;
    }

    public float fornisciQuotaPartecipazione() {
        return quota;
    }

}
