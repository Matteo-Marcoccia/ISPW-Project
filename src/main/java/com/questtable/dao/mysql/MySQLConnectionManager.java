package com.questtable.dao.mysql;

import com.questtable.config.PersistenceConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

final class MySQLConnectionManager {
    private MySQLConnectionManager() {
    }

    static Connection apriConnessione() throws SQLException {
        return DriverManager.getConnection(
                PersistenceConfig.fornisciUrlMySQL(),
                PersistenceConfig.fornisciUtenteMySQL(),
                PersistenceConfig.fornisciCredenzialeMySQL()
        );
    }
}
