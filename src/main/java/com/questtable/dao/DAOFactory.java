package com.questtable.dao;

import com.questtable.config.PersistenceConfig;
import com.questtable.dao.demo.DemoDAOFactory;
import com.questtable.dao.file_system.FileSystemDAOFactory;
import com.questtable.dao.mysql.MySQLDAOFactory;

public abstract class DAOFactory {

    public static final int MYSQL = 1;
    public static final int FILE_SYSTEM = 2;
    public static final int DEMO = 3;

    public static DAOFactory fornisciDAOFactory() {
        int tipoPersistenza = PersistenceConfig.fornisciTipoPersistenza();

        return switch (tipoPersistenza) {
            case MYSQL -> MySQLFactoryHolder.ISTANZA;
            case FILE_SYSTEM -> FileSystemFactoryHolder.ISTANZA;
            case DEMO -> DemoFactoryHolder.ISTANZA;
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
