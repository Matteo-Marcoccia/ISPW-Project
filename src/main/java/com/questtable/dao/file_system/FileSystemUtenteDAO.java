package com.questtable.dao.file_system;

import com.questtable.dao.IUtenteDAO;
import com.questtable.model.Cliente;
import com.questtable.model.RuoloUtente;
import com.questtable.model.Utente;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileSystemUtenteDAO implements IUtenteDAO {
    private static final String SEPARATORE_CAMPI = ";";
    private static final int CAMPI_RIGA_UTENTE = 4;
    private static final int INDICE_USERNAME = 0;
    private static final int INDICE_PASSWORD = 1;
    private static final int INDICE_RUOLO = 2;
    private static final int INDICE_PUNTI_FEDELTA = 3;

    private final Map<String, Utente> utentiInMemoria = new HashMap<>();

    @Override
    public Utente recuperaUtente(String username) {
        if (utentiInMemoria.containsKey(username)) {
            return utentiInMemoria.get(username);
        }

        inizializzaArchivioSeNecessario();

        for (String rigaUtente : leggiRigheUtenti()) {
            String[] campiUtente = dividiCampiUtente(rigaUtente);
            if (verificaRigaUtenteCercato(campiUtente, username)) {
                Utente utente = creaUtenteDa(campiUtente);
                utentiInMemoria.put(username, utente);
                return utente;
            }
        }

        return null;
    }

    @Override
    public void aggiornaPuntiFedelta(String username, int puntiFedelta) {
        inizializzaArchivioSeNecessario();

        List<String> righeAggiornate = new ArrayList<>();
        for (String rigaUtente : leggiRigheUtenti()) {
            String[] campiUtente = dividiCampiUtente(rigaUtente);
            if (verificaRigaUtenteCercato(campiUtente, username)) {
                campiUtente[INDICE_PUNTI_FEDELTA] = String.valueOf(Math.max(0, puntiFedelta));
                righeAggiornate.add(String.join(SEPARATORE_CAMPI, campiUtente));
            } else {
                righeAggiornate.add(rigaUtente);
            }
        }

        salvaRigheUtenti(righeAggiornate);
        aggiornaUtenteInMemoria(username, puntiFedelta);
    }

    private Utente creaUtenteDa(String[] campiUtente) {
        RuoloUtente ruolo = RuoloUtente.valueOf(campiUtente[INDICE_RUOLO]);
        if (ruolo == RuoloUtente.CLIENTE) {
            return new Cliente(
                    campiUtente[INDICE_USERNAME],
                    campiUtente[INDICE_PASSWORD],
                    Integer.parseInt(campiUtente[INDICE_PUNTI_FEDELTA])
            );
        }

        return new Utente(campiUtente[INDICE_USERNAME], campiUtente[INDICE_PASSWORD], ruolo);
    }

    private void aggiornaUtenteInMemoria(String username, int puntiFedelta) {
        Utente utente = utentiInMemoria.get(username);
        if (utente instanceof Cliente cliente) {
            cliente.aggiornaPuntiFedelta(puntiFedelta);
        }
    }

    private String[] dividiCampiUtente(String rigaUtente) {
        return rigaUtente.split(SEPARATORE_CAMPI, -1);
    }

    private boolean verificaRigaUtenteCercato(String[] campiUtente, String username) {
        return campiUtente.length == CAMPI_RIGA_UTENTE && campiUtente[INDICE_USERNAME].equals(username);
    }

    private List<String> leggiRigheUtenti() {
        try {
            return Files.readAllLines(FileSystemPaths.FILE_UTENTI);
        } catch (IOException exception) {
            throw new IllegalStateException("Impossibile leggere l'archivio utenti.", exception);
        }
    }

    private void salvaRigheUtenti(List<String> righeUtenti) {
        try {
            Files.write(FileSystemPaths.FILE_UTENTI, righeUtenti);
        } catch (IOException exception) {
            throw new IllegalStateException("Impossibile salvare l'archivio utenti.", exception);
        }
    }

    private void inizializzaArchivioSeNecessario() {
        try {
            Files.createDirectories(FileSystemPaths.CARTELLA_DATI);
            if (Files.notExists(FileSystemPaths.FILE_UTENTI)) {
                salvaRigheUtenti(creaRigheUtentiIniziali());
            }
        } catch (IOException exception) {
            throw new IllegalStateException("Impossibile inizializzare l'archivio utenti.", exception);
        }
    }

    private List<String> creaRigheUtentiIniziali() {
        return List.of(
                "matteo;1234;CLIENTE;0",
                "erik;1234;CLIENTE;0",
                "admin;admin;GESTORE;0"
        );
    }
}
