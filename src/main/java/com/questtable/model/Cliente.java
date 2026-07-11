package com.questtable.model;

public class Cliente extends Utente {
    private int puntiFedelta;

    public Cliente(String username, String password, int puntiFedelta) {
        super(username, password, RuoloUtente.CLIENTE);
        this.puntiFedelta = puntiFedelta;
    }

    public int fornisciPuntiFedelta() {
        return puntiFedelta;
    }

    public void accreditaPuntiFedelta(int punti) {
        if (punti > 0) {
            puntiFedelta += punti;
        }
    }

    public void riceviPuntiFedeltaDa(float importo) {
        accreditaPuntiFedelta(RegolaPuntiFedelta.calcolaPuntiPer(importo));
    }

    public void aggiornaPuntiFedelta(int puntiFedelta) {
        this.puntiFedelta = Math.max(0, puntiFedelta);
    }
}
