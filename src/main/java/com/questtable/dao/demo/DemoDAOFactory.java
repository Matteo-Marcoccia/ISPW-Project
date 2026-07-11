package com.questtable.dao.demo;

import com.questtable.dao.DAOFactory;
import com.questtable.dao.INotificaDAO;
import com.questtable.dao.IPrenotazioneDAO;
import com.questtable.dao.ISessioneTavoloDAO;
import com.questtable.dao.IUtenteDAO;

public class DemoDAOFactory extends DAOFactory {
    private final DemoUtenteDAO utenteDAO = new DemoUtenteDAO();
    private final DemoSessioneTavoloDAO sessioneTavoloDAO = new DemoSessioneTavoloDAO();
    private final DemoPrenotazioneDAO prenotazioneDAO = new DemoPrenotazioneDAO();
    private final DemoNotificaDAO notificaDAO = new DemoNotificaDAO();

    @Override
    public IUtenteDAO fornisciUtenteDAO() {
        return utenteDAO;
    }

    @Override
    public ISessioneTavoloDAO fornisciSessioneTavoloDAO() {
        return sessioneTavoloDAO;
    }

    @Override
    public IPrenotazioneDAO fornisciPrenotazioneDAO() {
        return prenotazioneDAO;
    }

    @Override
    public INotificaDAO fornisciNotificaDAO() {
        return notificaDAO;
    }
}
