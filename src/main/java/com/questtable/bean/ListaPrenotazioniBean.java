package com.questtable.bean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ListaPrenotazioniBean {
    private final List<PrenotazioneBean> prenotazioni;

    public ListaPrenotazioniBean(List<PrenotazioneBean> prenotazioni) {
        this.prenotazioni = new ArrayList<>(prenotazioni);
    }

    public List<PrenotazioneBean> fornisciPrenotazioni() {
        return Collections.unmodifiableList(prenotazioni);
    }

    public boolean verificaAssenzaPrenotazioni() {
        return prenotazioni.isEmpty();
    }
}
