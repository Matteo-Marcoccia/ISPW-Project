package com.questtable.dao.mysql;

import com.questtable.dao.INotificaDAO;
import com.questtable.model.Notifica;
import com.questtable.model.TipoNotifica;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MySQLNotificaDAO implements INotificaDAO {
    private static final String QUERY_SALVA_NOTIFICA = """
            INSERT INTO notifiche (username_destinatario, tipo, messaggio, letta)
            VALUES (?, ?, ?, FALSE)
            """;
    private static final String QUERY_NOTIFICHE_NON_LETTE = """
            SELECT username_destinatario, tipo, messaggio, letta
            FROM notifiche
            WHERE username_destinatario = ?
              AND letta = FALSE
            ORDER BY id_notifica
            """;
    private static final String QUERY_SEGNA_LETTE = """
            UPDATE notifiche
            SET letta = TRUE
            WHERE username_destinatario = ?
              AND letta = FALSE
            """;

    @Override
    public void salvaNuovaNotifica(Notifica notifica) {
        try (Connection connessione = MySQLConnectionManager.apriConnessione();
             PreparedStatement statement = connessione.prepareStatement(QUERY_SALVA_NOTIFICA)) {

            statement.setString(1, notifica.fornisciUsernameDestinatario());
            statement.setString(2, notifica.fornisciTipoNotifica().name());
            statement.setString(3, notifica.fornisciMessaggio());
            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new IllegalStateException("Impossibile salvare la notifica sul database.", exception);
        }
    }

    @Override
    public List<Notifica> recuperaNotificheNonLette(String usernameDestinatario) {
        try (Connection connessione = MySQLConnectionManager.apriConnessione();
             PreparedStatement statement = connessione.prepareStatement(QUERY_NOTIFICHE_NON_LETTE)) {

            statement.setString(1, usernameDestinatario);
            return recuperaNotificheDa(statement);
        } catch (SQLException exception) {
            throw new IllegalStateException("Impossibile recuperare le notifiche dal database.", exception);
        }
    }

    @Override
    public void segnaNotificheComeLette(String usernameDestinatario) {
        try (Connection connessione = MySQLConnectionManager.apriConnessione();
             PreparedStatement statement = connessione.prepareStatement(QUERY_SEGNA_LETTE)) {

            statement.setString(1, usernameDestinatario);
            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new IllegalStateException("Impossibile aggiornare le notifiche sul database.", exception);
        }
    }

    private List<Notifica> recuperaNotificheDa(PreparedStatement statement) throws SQLException {
        List<Notifica> notifiche = new ArrayList<>();
        try (ResultSet risultato = statement.executeQuery()) {
            while (risultato.next()) {
                notifiche.add(creaNotificaDa(risultato));
            }
        }

        return notifiche;
    }

    private Notifica creaNotificaDa(ResultSet risultato) throws SQLException {
        return new Notifica(
                risultato.getString("username_destinatario"),
                TipoNotifica.valueOf(risultato.getString("tipo")),
                risultato.getString("messaggio"),
                risultato.getBoolean("letta")
        );
    }
}
