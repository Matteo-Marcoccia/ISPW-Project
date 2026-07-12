package com.questtable.dao.file_system;

import com.questtable.dao.DAOFactory;
import com.questtable.dao.IPrenotazioneDAO;
import com.questtable.dao.ISessioneTavoloDAO;
import com.questtable.dao.IUtenteDAO;

public class FileSystemDAOFactory extends DAOFactory {
    private final FileSystemUtenteDAO utenteDAO = new FileSystemUtenteDAO();
    private final FileSystemSessioneTavoloDAO sessioneTavoloDAO = new FileSystemSessioneTavoloDAO();
    private final FileSystemPrenotazioneDAO prenotazioneDAO =
            new FileSystemPrenotazioneDAO(utenteDAO, sessioneTavoloDAO);

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
}
