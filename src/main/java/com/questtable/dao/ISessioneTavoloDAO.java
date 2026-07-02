package com.questtable.dao;

import com.questtable.model.GiornoSettimana;
import com.questtable.model.SessioneTavolo;

import java.util.List;

public interface ISessioneTavoloDAO {
    List<SessioneTavolo> cercaTavoliDisponibili(String titoloGioco, GiornoSettimana giornoSettimana);

    SessioneTavolo recuperaTavolo(int idTavolo);

    boolean prenotaPosti(int idTavolo, int postiRichiesti);
}
