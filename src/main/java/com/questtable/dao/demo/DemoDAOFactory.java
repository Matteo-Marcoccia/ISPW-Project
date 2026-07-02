package com.questtable.dao.demo;

import com.questtable.dao.DAOFactory;
import com.questtable.dao.IPrenotazioneDAO;
import com.questtable.dao.ISessioneTavoloDAO;
import com.questtable.dao.IUtenteDAO;

public class DemoDAOFactory extends DAOFactory {

    @Override
    public IUtenteDAO fornisciUtenteDAO() {
        return new DemoUtenteDAO();
    }

    @Override
    public ISessioneTavoloDAO fornisciSessioneTavoloDAO() {
        return new DemoSessioneTavoloDAO();
    }

    @Override
    public IPrenotazioneDAO fornisciPrenotazioneDAO() {
        return new DemoPrenotazioneDAO();
    }
}
