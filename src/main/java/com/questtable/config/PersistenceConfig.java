package com.questtable.config;

import com.questtable.dao.DAOFactory;

public final class PersistenceConfig {

    private static int tipoPersistenza = DAOFactory.DEMO;

    private PersistenceConfig() {
    }

    public static int fornisciTipoPersistenza() {
        return tipoPersistenza;
    }

    public static void configuraTipoPersistenza(int nuovoTipoPersistenza) {
        tipoPersistenza = nuovoTipoPersistenza;
    }
}
