package com.questtable.dao.mysql;

import com.questtable.dao.ISessioneTavoloDAO;
import com.questtable.model.GiornoSettimana;
import com.questtable.model.Gioco;
import com.questtable.model.SessioneTavolo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MySQLSessioneTavoloDAO implements ISessioneTavoloDAO {
    private static final String QUERY_BASE_TAVOLI_DISPONIBILI = """
            SELECT id_tavolo, titolo_gioco, percorso_immagine, posti_totali,
                   posti_disponibili, giorno_settimana, fascia_oraria, quota
            FROM sessioni_tavolo
            WHERE posti_disponibili > 0
            """;
    private static final String QUERY_RECUPERA_TAVOLO = """
            SELECT id_tavolo, titolo_gioco, percorso_immagine, posti_totali,
                   posti_disponibili, giorno_settimana, fascia_oraria, quota
            FROM sessioni_tavolo
            WHERE id_tavolo = ?
            """;
    private static final String QUERY_PRENOTA_POSTI = """
            UPDATE sessioni_tavolo
            SET posti_disponibili = posti_disponibili - ?
            WHERE id_tavolo = ?
              AND posti_disponibili >= ?
            """;
    private static final String FILTRO_TITOLO = " AND LOWER(titolo_gioco) LIKE ?";
    private static final String FILTRO_GIORNO = " AND giorno_settimana = ?";
    private static final String ORDINAMENTO_TAVOLI = " ORDER BY id_tavolo";

    @Override
    public List<SessioneTavolo> cercaTavoliDisponibili(String titoloGioco, GiornoSettimana giornoSettimana) {
        List<SessioneTavolo> tavoliDisponibili = new ArrayList<>();
        String query = creaQueryRicercaTavoli(titoloGioco, giornoSettimana);

        try (Connection connessione = MySQLConnectionManager.apriConnessione();
             PreparedStatement statement = connessione.prepareStatement(query)) {

            valorizzaParametriRicerca(statement, titoloGioco, giornoSettimana);
            try (ResultSet risultato = statement.executeQuery()) {
                while (risultato.next()) {
                    tavoliDisponibili.add(creaTavoloDa(risultato));
                }
            }

            return tavoliDisponibili;
        } catch (SQLException exception) {
            throw new IllegalStateException("Impossibile recuperare i tavoli disponibili dal database.", exception);
        }
    }

    @Override
    public SessioneTavolo recuperaTavolo(int idTavolo) {
        try (Connection connessione = MySQLConnectionManager.apriConnessione();
             PreparedStatement statement = connessione.prepareStatement(QUERY_RECUPERA_TAVOLO)) {

            statement.setInt(1, idTavolo);
            try (ResultSet risultato = statement.executeQuery()) {
                if (risultato.next()) {
                    return creaTavoloDa(risultato);
                }
            }

            return null;
        } catch (SQLException exception) {
            throw new IllegalStateException("Impossibile recuperare il tavolo dal database.", exception);
        }
    }

    @Override
    public boolean prenotaPosti(int idTavolo, int postiRichiesti) {
        if (postiRichiesti <= 0) {
            return false;
        }

        try (Connection connessione = MySQLConnectionManager.apriConnessione();
             PreparedStatement statement = connessione.prepareStatement(QUERY_PRENOTA_POSTI)) {

            statement.setInt(1, postiRichiesti);
            statement.setInt(2, idTavolo);
            statement.setInt(3, postiRichiesti);
            return statement.executeUpdate() == 1;
        } catch (SQLException exception) {
            throw new IllegalStateException("Impossibile prenotare i posti del tavolo sul database.", exception);
        }
    }

    private String creaQueryRicercaTavoli(String titoloGioco, GiornoSettimana giornoSettimana) {
        StringBuilder query = new StringBuilder(QUERY_BASE_TAVOLI_DISPONIBILI);

        if (verificaTitoloPresente(titoloGioco)) {
            query.append(FILTRO_TITOLO);
        }

        if (giornoSettimana != null) {
            query.append(FILTRO_GIORNO);
        }

        query.append(ORDINAMENTO_TAVOLI);
        return query.toString();
    }

    private void valorizzaParametriRicerca(PreparedStatement statement, String titoloGioco,
                                           GiornoSettimana giornoSettimana) throws SQLException {
        int indiceParametro = 1;

        if (verificaTitoloPresente(titoloGioco)) {
            statement.setString(indiceParametro, "%" + titoloGioco.trim().toLowerCase() + "%");
            indiceParametro++;
        }

        if (giornoSettimana != null) {
            statement.setString(indiceParametro, giornoSettimana.name());
        }
    }

    private boolean verificaTitoloPresente(String titoloGioco) {
        return titoloGioco != null && !titoloGioco.trim().isEmpty();
    }

    private SessioneTavolo creaTavoloDa(ResultSet risultato) throws SQLException {
        Gioco gioco = new Gioco(
                risultato.getString("titolo_gioco"),
                risultato.getString("percorso_immagine")
        );

        return new SessioneTavolo(
                risultato.getInt("id_tavolo"),
                gioco,
                risultato.getInt("posti_totali"),
                risultato.getInt("posti_disponibili"),
                GiornoSettimana.valueOf(risultato.getString("giorno_settimana")),
                risultato.getString("fascia_oraria"),
                risultato.getFloat("quota")
        );
    }
}
