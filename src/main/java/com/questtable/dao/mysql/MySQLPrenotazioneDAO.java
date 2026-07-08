package com.questtable.dao.mysql;

import com.questtable.dao.IPrenotazioneDAO;
import com.questtable.dao.ISessioneTavoloDAO;
import com.questtable.dao.IUtenteDAO;
import com.questtable.model.Cliente;
import com.questtable.model.DatiPrenotazione;
import com.questtable.model.Prenotazione;
import com.questtable.model.SessioneTavolo;
import com.questtable.model.StatoPrenotazione;
import com.questtable.model.Utente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class MySQLPrenotazioneDAO implements IPrenotazioneDAO {
    private static final String QUERY_SALVA_PRENOTAZIONE = """
            INSERT INTO prenotazioni (
                id_prenotazione,
                username_cliente,
                id_tavolo,
                posti_prenotati,
                importo_totale,
                data_prenotazione,
                ora_prenotazione,
                stato
            )
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
            """;
    private static final String QUERY_RECUPERA_PRENOTAZIONE = """
            SELECT id_prenotazione, username_cliente, id_tavolo, posti_prenotati,
                   importo_totale, data_prenotazione, ora_prenotazione, stato
            FROM prenotazioni
            WHERE id_prenotazione = ?
            """;
    private static final String QUERY_PRENOTAZIONI_CLIENTE = """
            SELECT id_prenotazione, username_cliente, id_tavolo, posti_prenotati,
                   importo_totale, data_prenotazione, ora_prenotazione, stato
            FROM prenotazioni
            WHERE username_cliente = ?
            ORDER BY id_prenotazione DESC
            """;
    private static final String QUERY_PRENOTAZIONI_IN_ATTESA = """
            SELECT id_prenotazione, username_cliente, id_tavolo, posti_prenotati,
                   importo_totale, data_prenotazione, ora_prenotazione, stato
            FROM prenotazioni
            WHERE stato = ?
            ORDER BY id_prenotazione
            """;
    private static final String QUERY_CONFERMA_PRENOTAZIONE = """
            UPDATE prenotazioni
            SET stato = ?
            WHERE id_prenotazione = ?
            """;

    private final IUtenteDAO utenteDAO;
    private final ISessioneTavoloDAO sessioneTavoloDAO;

    public MySQLPrenotazioneDAO(IUtenteDAO utenteDAO, ISessioneTavoloDAO sessioneTavoloDAO) {
        this.utenteDAO = utenteDAO;
        this.sessioneTavoloDAO = sessioneTavoloDAO;
    }

    @Override
    public void salvaNuovaPrenotazione(Prenotazione prenotazione) {
        try (Connection connessione = MySQLConnectionManager.apriConnessione();
             PreparedStatement statement = connessione.prepareStatement(QUERY_SALVA_PRENOTAZIONE)) {

            statement.setInt(1, prenotazione.fornisciIdentificativo());
            statement.setString(2, prenotazione.fornisciUsernameCliente());
            statement.setInt(3, prenotazione.fornisciIdentificativoTavoloPrenotato());
            statement.setInt(4, prenotazione.fornisciNumeroPostiPrenotati());
            statement.setFloat(5, prenotazione.fornisciImportoTotale());
            statement.setObject(6, prenotazione.fornisciDataPrenotazione());
            statement.setObject(7, normalizzaOra(prenotazione.fornisciOraPrenotazione()));
            statement.setString(8, prenotazione.fornisciStatoCorrente().name());
            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new IllegalStateException("Impossibile salvare la prenotazione sul database.", exception);
        }
    }

    @Override
    public Prenotazione recuperaPrenotazione(int idPrenotazione) {
        try (Connection connessione = MySQLConnectionManager.apriConnessione();
             PreparedStatement statement = connessione.prepareStatement(QUERY_RECUPERA_PRENOTAZIONE)) {

            statement.setInt(1, idPrenotazione);
            try (ResultSet risultato = statement.executeQuery()) {
                if (risultato.next()) {
                    return creaPrenotazioneDa(risultato);
                }
            }

            return null;
        } catch (SQLException exception) {
            throw new IllegalStateException("Impossibile recuperare la prenotazione dal database.", exception);
        }
    }

    @Override
    public List<Prenotazione> recuperaPrenotazioniCliente(String usernameCliente) {
        try (Connection connessione = MySQLConnectionManager.apriConnessione();
             PreparedStatement statement = connessione.prepareStatement(QUERY_PRENOTAZIONI_CLIENTE)) {

            statement.setString(1, usernameCliente);
            return recuperaPrenotazioniDa(statement);
        } catch (SQLException exception) {
            throw new IllegalStateException("Impossibile recuperare le prenotazioni del cliente dal database.", exception);
        }
    }

    @Override
    public List<Prenotazione> recuperaPrenotazioniInAttesa() {
        try (Connection connessione = MySQLConnectionManager.apriConnessione();
             PreparedStatement statement = connessione.prepareStatement(QUERY_PRENOTAZIONI_IN_ATTESA)) {

            statement.setString(1, StatoPrenotazione.IN_ATTESA.name());
            return recuperaPrenotazioniDa(statement);
        } catch (SQLException exception) {
            throw new IllegalStateException("Impossibile recuperare le prenotazioni in attesa dal database.", exception);
        }
    }

    @Override
    public void confermaPrenotazione(int idPrenotazione) {
        try (Connection connessione = MySQLConnectionManager.apriConnessione();
             PreparedStatement statement = connessione.prepareStatement(QUERY_CONFERMA_PRENOTAZIONE)) {

            statement.setString(1, StatoPrenotazione.CONFERMATA.name());
            statement.setInt(2, idPrenotazione);
            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new IllegalStateException("Impossibile confermare la prenotazione sul database.", exception);
        }
    }

    private List<Prenotazione> recuperaPrenotazioniDa(PreparedStatement statement) throws SQLException {
        List<Prenotazione> prenotazioni = new ArrayList<>();
        try (ResultSet risultato = statement.executeQuery()) {
            while (risultato.next()) {
                prenotazioni.add(creaPrenotazioneDa(risultato));
            }
        }

        return prenotazioni;
    }

    private Prenotazione creaPrenotazioneDa(ResultSet risultato) throws SQLException {
        Cliente cliente = recuperaCliente(risultato.getString("username_cliente"));
        SessioneTavolo tavolo = recuperaSessioneTavolo(risultato.getInt("id_tavolo"));

        if (cliente == null || tavolo == null) {
            throw new IllegalStateException("Prenotazione collegata a dati non disponibili.");
        }

        return new Prenotazione(
                risultato.getInt("id_prenotazione"),
                cliente,
                tavolo,
                new DatiPrenotazione(
                        risultato.getObject("data_prenotazione", LocalDate.class),
                        risultato.getObject("ora_prenotazione", LocalTime.class),
                        StatoPrenotazione.valueOf(risultato.getString("stato"))
                ),
                risultato.getInt("posti_prenotati"),
                risultato.getFloat("importo_totale")
        );
    }

    private Cliente recuperaCliente(String usernameCliente) {
        Utente utente = utenteDAO.recuperaUtente(usernameCliente);
        if (utente instanceof Cliente cliente) {
            return cliente;
        }

        return null;
    }

    private SessioneTavolo recuperaSessioneTavolo(int idTavolo) {
        return sessioneTavoloDAO.recuperaTavolo(idTavolo);
    }

    private LocalTime normalizzaOra(LocalTime oraPrenotazione) {
        return oraPrenotazione.withSecond(0).withNano(0);
    }
}
