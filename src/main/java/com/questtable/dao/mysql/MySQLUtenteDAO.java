package com.questtable.dao.mysql;

import com.questtable.dao.IUtenteDAO;
import com.questtable.model.Cliente;
import com.questtable.model.RuoloUtente;
import com.questtable.model.Utente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class MySQLUtenteDAO implements IUtenteDAO {
    private static final String QUERY_RECUPERA_UTENTE = """
            SELECT username, parola_accesso, ruolo, punti_fedelta
            FROM utenti
            WHERE username = ?
            """;
    private static final String QUERY_AGGIORNA_PUNTI = """
            UPDATE utenti
            SET punti_fedelta = ?
            WHERE username = ?
            """;

    private final Map<String, Utente> utentiInMemoria = new HashMap<>();

    @Override
    public Utente recuperaUtente(String username) {
        if (utentiInMemoria.containsKey(username)) {
            return utentiInMemoria.get(username);
        }

        try (Connection connessione = MySQLConnectionManager.apriConnessione();
             PreparedStatement statement = connessione.prepareStatement(QUERY_RECUPERA_UTENTE)) {

            statement.setString(1, username);
            try (ResultSet risultato = statement.executeQuery()) {
                if (risultato.next()) {
                    Utente utente = creaUtenteDa(risultato);
                    utentiInMemoria.put(username, utente);
                    return utente;
                }
            }

            return null;
        } catch (SQLException exception) {
            throw new IllegalStateException("Impossibile recuperare l'utente dal database.", exception);
        }
    }

    @Override
    public void aggiornaPuntiFedelta(String username, int puntiFedelta) {
        try (Connection connessione = MySQLConnectionManager.apriConnessione();
             PreparedStatement statement = connessione.prepareStatement(QUERY_AGGIORNA_PUNTI)) {

            statement.setInt(1, Math.max(0, puntiFedelta));
            statement.setString(2, username);
            statement.executeUpdate();
            aggiornaUtenteInMemoria(username, puntiFedelta);
        } catch (SQLException exception) {
            throw new IllegalStateException("Impossibile aggiornare i punti fedelta dell'utente.", exception);
        }
    }

    private Utente creaUtenteDa(ResultSet risultato) throws SQLException {
        String username = risultato.getString("username");
        String parolaAccesso = risultato.getString("parola_accesso");
        RuoloUtente ruolo = RuoloUtente.valueOf(risultato.getString("ruolo"));

        if (ruolo == RuoloUtente.CLIENTE) {
            return new Cliente(username, parolaAccesso, risultato.getInt("punti_fedelta"));
        }

        return new Utente(username, parolaAccesso, ruolo);
    }

    private void aggiornaUtenteInMemoria(String username, int puntiFedelta) {
        Utente utente = utentiInMemoria.get(username);
        if (utente instanceof Cliente cliente) {
            cliente.aggiornaPuntiFedelta(puntiFedelta);
        }
    }
}
