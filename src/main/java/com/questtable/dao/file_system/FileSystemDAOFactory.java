package com.questtable.dao.file_system;

import com.questtable.dao.DAOFactory;
import com.questtable.dao.IPrenotazioneDAO;
import com.questtable.dao.ISessioneTavoloDAO;
import com.questtable.dao.IUtenteDAO;

public class FileSystemDAOFactory extends DAOFactory {

    @Override
    public IUtenteDAO fornisciUtenteDAO() {
        throw new UnsupportedOperationException("Persistenza file system non ancora implementata.");
    }

    @Override
    public ISessioneTavoloDAO fornisciSessioneTavoloDAO() {
        throw new UnsupportedOperationException("Persistenza file system non ancora implementata.");
    }

    @Override
    public IPrenotazioneDAO fornisciPrenotazioneDAO() {
        throw new UnsupportedOperationException("Persistenza file system non ancora implementata.");
    }
}
