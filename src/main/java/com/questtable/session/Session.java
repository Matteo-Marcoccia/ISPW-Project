package com.questtable.session;

import com.questtable.model.RuoloUtente;
import com.questtable.model.Utente;

public class Session {
    private final String idSessione;
    private final Utente utente;

    public Session(String idSessione, Utente utente) {
        this.idSessione = idSessione;
        this.utente = utente;
    }

    public String fornisciIdentificativoSessione() {
        return idSessione;
    }

    public String fornisciUsername() {
        return utente.fornisciUsername();
    }

    public boolean verificaRuolo(RuoloUtente ruoloRichiesto) {
        return utente.fornisciRuolo() == ruoloRichiesto;
    }
}
