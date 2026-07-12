package com.questtable.dao.file_system;

import com.questtable.dao.IPrenotazioneDAO;
import com.questtable.dao.ISessioneTavoloDAO;
import com.questtable.dao.IUtenteDAO;
import com.questtable.exception.DAOException;
import com.questtable.model.Cliente;
import com.questtable.model.DatiPrenotazione;
import com.questtable.model.Prenotazione;
import com.questtable.model.SessioneTavolo;
import com.questtable.model.StatoPrenotazione;
import com.questtable.model.Utente;

import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FileSystemPrenotazioneDAO implements IPrenotazioneDAO {
    private static final String SEPARATORE_CAMPI = ";";
    private static final int CAMPI_RIGA_PRENOTAZIONE = 8;
    private static final int INDICE_ID_PRENOTAZIONE = 0;
    private static final int INDICE_USERNAME_CLIENTE = 1;
    private static final int INDICE_ID_TAVOLO = 2;
    private static final int INDICE_POSTI_PRENOTATI = 3;
    private static final int INDICE_IMPORTO = 4;
    private static final int INDICE_DATA = 5;
    private static final int INDICE_ORA = 6;
    private static final int INDICE_STATO = 7;
    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ISO_LOCAL_DATE;
    private static final DateTimeFormatter FORMATO_ORA = DateTimeFormatter.ofPattern("HH:mm");

    private final IUtenteDAO utenteDAO;
    private final ISessioneTavoloDAO sessioneTavoloDAO;
    private final Map<Integer, Prenotazione> prenotazioniInMemoria = new LinkedHashMap<>();
    private final Set<String> storiciClienteCaricati = new HashSet<>();
    private boolean prenotazioniInAttesaCaricate;

    public FileSystemPrenotazioneDAO(IUtenteDAO utenteDAO, ISessioneTavoloDAO sessioneTavoloDAO) {
        this.utenteDAO = utenteDAO;
        this.sessioneTavoloDAO = sessioneTavoloDAO;
    }

    @Override
    public void salvaNuovaPrenotazione(Prenotazione prenotazione) {
        inizializzaArchivioSeNecessario();

        List<String> righePrenotazioni = new ArrayList<>(leggiRighePrenotazioni());
        righePrenotazioni.add(creaRigaDa(prenotazione));
        salvaRighePrenotazioni(righePrenotazioni);
        prenotazioniInMemoria.put(prenotazione.fornisciIdentificativo(), prenotazione);
    }

    @Override
    public Prenotazione recuperaPrenotazione(int idPrenotazione) {
        if (prenotazioniInMemoria.containsKey(idPrenotazione)) {
            return prenotazioniInMemoria.get(idPrenotazione);
        }

        inizializzaArchivioSeNecessario();

        for (String rigaPrenotazione : leggiRighePrenotazioni()) {
            String[] campiPrenotazione = dividiCampiPrenotazione(rigaPrenotazione);
            if (verificaRigaPrenotazioneCercata(campiPrenotazione, idPrenotazione)) {
                Prenotazione prenotazione = creaPrenotazioneDa(campiPrenotazione);
                prenotazioniInMemoria.put(idPrenotazione, prenotazione);
                return prenotazione;
            }
        }

        return null;
    }

    @Override
    public List<Prenotazione> recuperaPrenotazioniCliente(String usernameCliente) {
        if (!storiciClienteCaricati.contains(usernameCliente)) {
            inizializzaArchivioSeNecessario();

            for (String rigaPrenotazione : leggiRighePrenotazioni()) {
                String[] campiPrenotazione = dividiCampiPrenotazione(rigaPrenotazione);
                if (verificaRigaPrenotazioneValida(campiPrenotazione)
                        && campiPrenotazione[INDICE_USERNAME_CLIENTE].equals(usernameCliente)) {
                    Prenotazione prenotazione = creaPrenotazioneDa(campiPrenotazione);
                    prenotazioniInMemoria.put(prenotazione.fornisciIdentificativo(), prenotazione);
                }
            }

            storiciClienteCaricati.add(usernameCliente);
        }

        List<Prenotazione> prenotazioniCliente = new ArrayList<>();
        for (Prenotazione prenotazione : prenotazioniInMemoria.values()) {
            if (prenotazione.fornisciUsernameCliente().equals(usernameCliente)) {
                prenotazioniCliente.add(prenotazione);
            }
        }

        return prenotazioniCliente;
    }

    @Override
    public List<Prenotazione> recuperaPrenotazioniInAttesa() {
        if (!prenotazioniInAttesaCaricate) {
            inizializzaArchivioSeNecessario();

            for (String rigaPrenotazione : leggiRighePrenotazioni()) {
                String[] campiPrenotazione = dividiCampiPrenotazione(rigaPrenotazione);
                if (verificaRigaPrenotazioneValida(campiPrenotazione)
                        && StatoPrenotazione.IN_ATTESA.name().equals(campiPrenotazione[INDICE_STATO])) {
                    Prenotazione prenotazione = creaPrenotazioneDa(campiPrenotazione);
                    prenotazioniInMemoria.put(prenotazione.fornisciIdentificativo(), prenotazione);
                }
            }

            prenotazioniInAttesaCaricate = true;
        }

        List<Prenotazione> prenotazioniInAttesa = new ArrayList<>();
        for (Prenotazione prenotazione : prenotazioniInMemoria.values()) {
            if (prenotazione.fornisciStatoCorrente() == StatoPrenotazione.IN_ATTESA) {
                prenotazioniInAttesa.add(prenotazione);
            }
        }

        return prenotazioniInAttesa;
    }

    @Override
    public void confermaPrenotazione(int idPrenotazione) {
        inizializzaArchivioSeNecessario();

        List<String> righeAggiornate = new ArrayList<>();
        for (String rigaPrenotazione : leggiRighePrenotazioni()) {
            String[] campiPrenotazione = dividiCampiPrenotazione(rigaPrenotazione);
            if (verificaRigaPrenotazioneCercata(campiPrenotazione, idPrenotazione)) {
                campiPrenotazione[INDICE_STATO] = StatoPrenotazione.CONFERMATA.name();
                righeAggiornate.add(String.join(SEPARATORE_CAMPI, campiPrenotazione));
            } else {
                righeAggiornate.add(rigaPrenotazione);
            }
        }

        salvaRighePrenotazioni(righeAggiornate);
        confermaPrenotazioneInMemoria(idPrenotazione);
    }

    private Prenotazione creaPrenotazioneDa(String[] campiPrenotazione) {
        Cliente cliente = recuperaCliente(campiPrenotazione[INDICE_USERNAME_CLIENTE]);
        SessioneTavolo tavolo = sessioneTavoloDAO.recuperaTavolo(
                Integer.parseInt(campiPrenotazione[INDICE_ID_TAVOLO])
        );

        if (cliente == null || tavolo == null) {
            throw new IllegalStateException("Prenotazione collegata a dati non disponibili.");
        }

        return new Prenotazione(
                Integer.parseInt(campiPrenotazione[INDICE_ID_PRENOTAZIONE]),
                cliente,
                tavolo,
                new DatiPrenotazione(
                        LocalDate.parse(campiPrenotazione[INDICE_DATA], FORMATO_DATA),
                        LocalTime.parse(campiPrenotazione[INDICE_ORA], FORMATO_ORA),
                        StatoPrenotazione.valueOf(campiPrenotazione[INDICE_STATO])
                ),
                Integer.parseInt(campiPrenotazione[INDICE_POSTI_PRENOTATI]),
                Float.parseFloat(campiPrenotazione[INDICE_IMPORTO])
        );
    }

    private Cliente recuperaCliente(String usernameCliente) {
        Utente utente = utenteDAO.recuperaUtente(usernameCliente);
        if (utente instanceof Cliente cliente) {
            return cliente;
        }

        return null;
    }

    private void confermaPrenotazioneInMemoria(int idPrenotazione) {
        Prenotazione prenotazione = prenotazioniInMemoria.get(idPrenotazione);
        if (prenotazione != null) {
            prenotazione.confermaPrenotazione();
        }
    }

    private String creaRigaDa(Prenotazione prenotazione) {
        return String.join(
                SEPARATORE_CAMPI,
                String.valueOf(prenotazione.fornisciIdentificativo()),
                prenotazione.fornisciUsernameCliente(),
                String.valueOf(prenotazione.fornisciIdentificativoTavoloPrenotato()),
                String.valueOf(prenotazione.fornisciNumeroPostiPrenotati()),
                String.valueOf(prenotazione.fornisciImportoTotale()),
                prenotazione.fornisciDataPrenotazione().format(FORMATO_DATA),
                prenotazione.fornisciOraPrenotazione().format(FORMATO_ORA),
                prenotazione.fornisciStatoCorrente().name()
        );
    }

    private String[] dividiCampiPrenotazione(String rigaPrenotazione) {
        return rigaPrenotazione.split(SEPARATORE_CAMPI, -1);
    }

    private boolean verificaRigaPrenotazioneValida(String[] campiPrenotazione) {
        return campiPrenotazione.length == CAMPI_RIGA_PRENOTAZIONE;
    }

    private boolean verificaRigaPrenotazioneCercata(String[] campiPrenotazione, int idPrenotazione) {
        return verificaRigaPrenotazioneValida(campiPrenotazione)
                && Integer.parseInt(campiPrenotazione[INDICE_ID_PRENOTAZIONE]) == idPrenotazione;
    }

    private List<String> leggiRighePrenotazioni() {
        try {
            return Files.readAllLines(FileSystemPaths.FILE_PRENOTAZIONI);
        } catch (IOException exception) {
            throw new DAOException("Impossibile leggere l'archivio prenotazioni.", exception);
        }
    }

    private void salvaRighePrenotazioni(List<String> righePrenotazioni) {
        try {
            Files.write(FileSystemPaths.FILE_PRENOTAZIONI, righePrenotazioni);
        } catch (IOException exception) {
            throw new DAOException("Impossibile salvare l'archivio prenotazioni.", exception);
        }
    }

    private void inizializzaArchivioSeNecessario() {
        try {
            Files.createDirectories(FileSystemPaths.CARTELLA_DATI);
            if (Files.notExists(FileSystemPaths.FILE_PRENOTAZIONI)) {
                salvaRighePrenotazioni(new ArrayList<>());
            }
        } catch (IOException exception) {
            throw new DAOException("Impossibile inizializzare l'archivio prenotazioni.", exception);
        }
    }
}
