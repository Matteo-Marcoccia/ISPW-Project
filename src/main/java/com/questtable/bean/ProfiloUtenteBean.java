package com.questtable.bean;

import com.questtable.model.RuoloUtente;

public class ProfiloUtenteBean {
    private final String username;
    private final RuoloUtente ruolo;
    private final int puntiFedelta;

    public ProfiloUtenteBean(String username, RuoloUtente ruolo, int puntiFedelta) {
        this.username = username;
        this.ruolo = ruolo;
        this.puntiFedelta = puntiFedelta;
    }

    public String fornisciUsername() {
        return username;
    }

    public RuoloUtente fornisciRuolo() {
        return ruolo;
    }

    public int fornisciPuntiFedelta() {
        return puntiFedelta;
    }

    public boolean verificaRuolo(RuoloUtente ruoloRichiesto) {
        return ruolo == ruoloRichiesto;
    }
}
