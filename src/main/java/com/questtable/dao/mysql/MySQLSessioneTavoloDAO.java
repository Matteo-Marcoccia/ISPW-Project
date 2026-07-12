package com.questtable.dao.mysql;

import com.questtable.dao.ISessioneTavoloDAO;
import com.questtable.exception.DAOException;
import com.questtable.model.GiornoSettimana;
import com.questtable.model.Gioco;
import com.questtable.model.SessioneTavolo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("SqlResolve")
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

    private final Map<Integer, SessioneTavolo> tavoliInMemoria = new LinkedHashMap<>();
    private final Set<String> ricercheGiaEseguite = new HashSet<>();

    @Override
    public List<SessioneTavolo> cercaTavoliDisponibili(String titoloGioco, GiornoSettimana giornoSettimana) {
        String chiaveRicerca = creaChiaveRicerca(titoloGioco, giornoSettimana);
        if (!ricercheGiaEseguite.contains(chiaveRicerca)) {
            String query = creaQueryRicercaTavoli(titoloGioco, giornoSettimana);

            try (Connection connessione = MySQLConnectionManager.apriConnessione();
                 PreparedStatement statement = connessione.prepareStatement(query)) {

                valorizzaParametriRicerca(statement, titoloGioco, giornoSettimana);
                for (SessioneTavolo tavolo : recuperaTavoliDa(statement)) {
                    tavoliInMemoria.put(tavolo.fornisciIdentificativo(), tavolo);
                }
                ricercheGiaEseguite.add(chiaveRicerca);
            } catch (SQLException exception) {
                throw new DAOException("Impossibile recuperare i tavoli disponibili dal database.", exception);
            }
        }

        List<SessioneTavolo> tavoliDisponibili = new ArrayList<>();
        for (SessioneTavolo tavolo : tavoliInMemoria.values()) {
            if (verificaTavoloDisponibile(tavolo, titoloGioco, giornoSettimana)) {
                tavoliDisponibili.add(tavolo);
            }
        }

        return tavoliDisponibili;
    }

    @Override
    public SessioneTavolo recuperaTavolo(int idTavolo) {
        if (tavoliInMemoria.containsKey(idTavolo)) {
            return tavoliInMemoria.get(idTavolo);
        }

        try (Connection connessione = MySQLConnectionManager.apriConnessione();
             PreparedStatement statement = connessione.prepareStatement(QUERY_RECUPERA_TAVOLO)) {

            statement.setInt(1, idTavolo);
            try (ResultSet risultato = statement.executeQuery()) {
                if (risultato.next()) {
                    SessioneTavolo tavolo = creaTavoloDa(risultato);
                    tavoliInMemoria.put(idTavolo, tavolo);
                    return tavolo;
                }
            }

            return null;
        } catch (SQLException exception) {
            throw new DAOException("Impossibile recuperare il tavolo dal database.", exception);
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
            boolean postiPrenotati = statement.executeUpdate() == 1;
            if (postiPrenotati) {
                aggiornaTavoloInMemoria(idTavolo, postiRichiesti);
            }

            return postiPrenotati;
        } catch (SQLException exception) {
            throw new DAOException("Impossibile prenotare i posti del tavolo sul database.", exception);
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

    private boolean verificaTavoloDisponibile(SessioneTavolo tavolo, String titoloGioco,
                                              GiornoSettimana giornoSettimana) {
        return tavolo.fornisciNumeroPostiDisponibili() > 0
                && verificaTitoloCompatibile(tavolo, titoloGioco)
                && verificaGiornoCompatibile(tavolo, giornoSettimana);
    }

    private boolean verificaTitoloCompatibile(SessioneTavolo tavolo, String titoloGioco) {
        return !verificaTitoloPresente(titoloGioco)
                || tavolo.fornisciTitoloGiocoAssociato()
                .toLowerCase()
                .contains(titoloGioco.trim().toLowerCase());
    }

    private boolean verificaGiornoCompatibile(SessioneTavolo tavolo, GiornoSettimana giornoSettimana) {
        return giornoSettimana == null || tavolo.fornisciGiornoSettimana() == giornoSettimana;
    }

    private void aggiornaTavoloInMemoria(int idTavolo, int postiRichiesti) {
        SessioneTavolo tavolo = tavoliInMemoria.get(idTavolo);
        if (tavolo != null) {
            tavolo.prenotaPosti(postiRichiesti);
        }
    }

    private String creaChiaveRicerca(String titoloGioco, GiornoSettimana giornoSettimana) {
        String titoloNormalizzato = "";
        if (titoloGioco != null) {
            titoloNormalizzato = titoloGioco.trim().toLowerCase();
        }

        String giornoNormalizzato = "";
        if (giornoSettimana != null) {
            giornoNormalizzato = giornoSettimana.name();
        }

        return titoloNormalizzato + "|" + giornoNormalizzato;
    }

    private List<SessioneTavolo> recuperaTavoliDa(PreparedStatement statement) throws SQLException {
        List<SessioneTavolo> tavoli = new ArrayList<>();
        try (ResultSet risultato = statement.executeQuery()) {
            while (risultato.next()) {
                tavoli.add(creaTavoloDa(risultato));
            }
        }

        return tavoli;
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
