package com.questtable.dao.mysql;

import com.questtable.dao.DAOFactory;
import com.questtable.dao.IPrenotazioneDAO;
import com.questtable.dao.ISessioneTavoloDAO;
import com.questtable.dao.IUtenteDAO;

public class MySQLDAOFactory extends DAOFactory {

    @Override
    public IUtenteDAO fornisciUtenteDAO() {
        throw new UnsupportedOperationException("Persistenza MySQL non ancora implementata.");
    }

    @Override
    public ISessioneTavoloDAO fornisciSessioneTavoloDAO() {
        throw new UnsupportedOperationException("Persistenza MySQL non ancora implementata.");
    }

    @Override
    public IPrenotazioneDAO fornisciPrenotazioneDAO() {
        throw new UnsupportedOperationException("Persistenza MySQL non ancora implementata.");
    }
}
