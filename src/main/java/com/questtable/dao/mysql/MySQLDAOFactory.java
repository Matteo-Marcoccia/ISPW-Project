package com.questtable.dao.mysql;

import com.questtable.dao.DAOFactory;
import com.questtable.dao.INotificaDAO;
import com.questtable.dao.IPrenotazioneDAO;
import com.questtable.dao.ISessioneTavoloDAO;
import com.questtable.dao.IUtenteDAO;

public class MySQLDAOFactory extends DAOFactory {
    private final MySQLUtenteDAO utenteDAO = new MySQLUtenteDAO();
    private final MySQLSessioneTavoloDAO sessioneTavoloDAO = new MySQLSessioneTavoloDAO();
    private final MySQLPrenotazioneDAO prenotazioneDAO = new MySQLPrenotazioneDAO(utenteDAO, sessioneTavoloDAO);
    private final MySQLNotificaDAO notificaDAO = new MySQLNotificaDAO();

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
