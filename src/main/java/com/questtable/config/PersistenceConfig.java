package com.questtable.config;

public final class PersistenceConfig {
    public static final int MYSQL = 1;
    public static final int FILE_SYSTEM = 2;
    public static final int DEMO = 3;

    private static final String MYSQL_URL_PREDEFINITO = "jdbc:mysql://localhost:3306/questtable";
    private static final String MYSQL_UTENTE_PREDEFINITO = "root";
    private static final String MYSQL_CREDENZIALE_PREDEFINITA = "MySQL";

    private static int tipoPersistenza = DEMO;

    private PersistenceConfig() {
    }

    public static int fornisciTipoPersistenza() {
        return tipoPersistenza;
    }

    public static void configuraTipoPersistenza(int nuovoTipoPersistenza) {
        tipoPersistenza = nuovoTipoPersistenza;
    }

    public static String fornisciUrlMySQL() {
        return MYSQL_URL_PREDEFINITO;
    }

    public static String fornisciUtenteMySQL() {
        return MYSQL_UTENTE_PREDEFINITO;
    }

    public static String fornisciCredenzialeMySQL() {
        return MYSQL_CREDENZIALE_PREDEFINITA;
    }

}
