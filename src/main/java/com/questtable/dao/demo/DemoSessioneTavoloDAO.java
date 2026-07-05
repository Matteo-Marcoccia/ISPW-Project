package com.questtable.dao.demo;

import com.questtable.dao.ISessioneTavoloDAO;
import com.questtable.model.GiornoSettimana;
import com.questtable.model.SessioneTavolo;

import java.util.ArrayList;
import java.util.List;

public class DemoSessioneTavoloDAO implements ISessioneTavoloDAO {
    @Override
    public List<SessioneTavolo> cercaTavoliDisponibili(String titoloGioco, GiornoSettimana giornoSettimana) {
        List<SessioneTavolo> tavoliDisponibili = new ArrayList<>();

        for (SessioneTavolo sessioneTavolo : DemoDatabase.sessioniTavolo.values()) {
            if (sessioneTavolo.fornisciNumeroPostiDisponibili() > 0
                    && verificaTitoloCompatibile(sessioneTavolo, titoloGioco)
                    && verificaGiornoCompatibile(sessioneTavolo, giornoSettimana)) {
                tavoliDisponibili.add(sessioneTavolo);
            }
        }

        return tavoliDisponibili;
    }

    @Override
    public SessioneTavolo recuperaTavolo(int idTavolo) {
        return DemoDatabase.sessioniTavolo.get(idTavolo);
    }

    @Override
    public boolean prenotaPosti(int idTavolo, int postiRichiesti) {
        SessioneTavolo sessioneTavolo = DemoDatabase.sessioniTavolo.get(idTavolo);
        return sessioneTavolo != null && sessioneTavolo.prenotaPosti(postiRichiesti);
    }

    private boolean verificaTitoloCompatibile(SessioneTavolo sessioneTavolo, String titoloGioco) {
        return titoloGioco == null
                || sessioneTavolo.fornisciTitoloGiocoAssociato()
                .toLowerCase()
                .contains(titoloGioco.trim().toLowerCase());
    }

    private boolean verificaGiornoCompatibile(SessioneTavolo sessioneTavolo, GiornoSettimana giornoSettimana) {
        return giornoSettimana == null || sessioneTavolo.fornisciGiornoSettimana() == giornoSettimana;
    }
}
