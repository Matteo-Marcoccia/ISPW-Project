package com.questtable.bean;

import com.questtable.model.GiornoSettimana;

public class PreventivoBean {
    private final int idTavolo;
    private final String titoloGioco;
    private final GiornoSettimana giornoSettimana;
    private final String fasciaOraria;
    private final int postiRichiesti;
    private final float importoTotale;
    private final int puntiFedeltaPrevisti;

    public PreventivoBean(int idTavolo, String titoloGioco, GiornoSettimana giornoSettimana,
                          String fasciaOraria, int postiRichiesti, float importoTotale,
                          int puntiFedeltaPrevisti) {
        this.idTavolo = idTavolo;
        this.titoloGioco = titoloGioco;
        this.giornoSettimana = giornoSettimana;
        this.fasciaOraria = fasciaOraria;
        this.postiRichiesti = postiRichiesti;
        this.importoTotale = importoTotale;
        this.puntiFedeltaPrevisti = puntiFedeltaPrevisti;
    }

    public int fornisciIdentificativoTavolo() {
        return idTavolo;
    }

    public String fornisciTitoloGioco() {
        return titoloGioco;
    }

    public GiornoSettimana fornisciGiornoSettimana() {
        return giornoSettimana;
    }

    public String fornisciFasciaOraria() {
        return fasciaOraria;
    }

    public int fornisciNumeroPostiRichiesti() {
        return postiRichiesti;
    }

    public float fornisciImportoTotale() {
        return importoTotale;
    }

    public int fornisciPuntiFedeltaPrevisti() {
        return puntiFedeltaPrevisti;
    }

    public boolean verificaPreventivoValido() {
        return idTavolo > 0
                && titoloGioco != null
                && !titoloGioco.trim().isEmpty()
                && giornoSettimana != null
                && fasciaOraria != null
                && !fasciaOraria.trim().isEmpty()
                && postiRichiesti > 0
                && importoTotale > 0;
    }
}
