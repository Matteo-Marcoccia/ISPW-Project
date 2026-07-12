package com.questtable.dao;

import com.questtable.config.PersistenceConfig;
import com.questtable.dao.demo.DemoDAOFactory;
import com.questtable.dao.file_system.FileSystemDAOFactory;
import com.questtable.dao.mysql.MySQLDAOFactory;

@SuppressWarnings("java:S6548")
public abstract class DAOFactory {

    public static DAOFactory fornisciDAOFactory() {
        int tipoPersistenza = PersistenceConfig.fornisciTipoPersistenza();

        return switch (tipoPersistenza) {
            case PersistenceConfig.MYSQL -> MySQLFactoryHolder.ISTANZA;
            case PersistenceConfig.FILE_SYSTEM -> FileSystemFactoryHolder.ISTANZA;
            case PersistenceConfig.DEMO -> DemoFactoryHolder.ISTANZA;
            default -> throw new IllegalStateException("Tipo di persistenza non valido: " + tipoPersistenza);
        };
    }

    public abstract IUtenteDAO fornisciUtenteDAO();

    public abstract ISessioneTavoloDAO fornisciSessioneTavoloDAO();

    public abstract IPrenotazioneDAO fornisciPrenotazioneDAO();

    private static class MySQLFactoryHolder {
        private static final DAOFactory ISTANZA = new MySQLDAOFactory();
    }

    private static class FileSystemFactoryHolder {
        private static final DAOFactory ISTANZA = new FileSystemDAOFactory();
    }

    private static class DemoFactoryHolder {
        private static final DAOFactory ISTANZA = new DemoDAOFactory();
    }
}
