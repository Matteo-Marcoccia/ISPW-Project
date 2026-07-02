package com.questtable.session;

import com.questtable.model.RuoloUtente;

public class Session {
    private final String idSessione;
    private final String username;
    private final RuoloUtente ruolo;

    public Session(String idSessione, String username, RuoloUtente ruolo) {
        this.idSessione = idSessione;
        this.username = username;
        this.ruolo = ruolo;
    }

    public String fornisciIdentificativoSessione() {
        return idSessione;
    }

    public String fornisciUsername() {
        return username;
    }

    public RuoloUtente fornisciRuolo() {
        return ruolo;
    }

    public boolean verificaRuolo(RuoloUtente ruoloRichiesto) {
        return ruolo == ruoloRichiesto;
    }
}
