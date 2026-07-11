package com.questtable.dao.file_system;

import com.questtable.dao.ISessioneTavoloDAO;
import com.questtable.model.GiornoSettimana;
import com.questtable.model.Gioco;
import com.questtable.model.SessioneTavolo;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FileSystemSessioneTavoloDAO implements ISessioneTavoloDAO {
    private static final String SEPARATORE_CAMPI = ";";
    private static final int CAMPI_RIGA_TAVOLO = 8;
    private static final int INDICE_ID_TAVOLO = 0;
    private static final int INDICE_TITOLO_GIOCO = 1;
    private static final int INDICE_IMMAGINE_GIOCO = 2;
    private static final int INDICE_POSTI_TOTALI = 3;
    private static final int INDICE_POSTI_DISPONIBILI = 4;
    private static final int INDICE_GIORNO = 5;
    private static final int INDICE_FASCIA_ORARIA = 6;
    private static final int INDICE_QUOTA = 7;

    private final Map<Integer, SessioneTavolo> tavoliInMemoria = new LinkedHashMap<>();
    private final Set<String> ricercheGiaEseguite = new HashSet<>();

    @Override
    public List<SessioneTavolo> cercaTavoliDisponibili(String titoloGioco, GiornoSettimana giornoSettimana) {
        String chiaveRicerca = creaChiaveRicerca(titoloGioco, giornoSettimana);
        if (!ricercheGiaEseguite.contains(chiaveRicerca)) {
            inizializzaArchivioSeNecessario();

            for (String rigaTavolo : leggiRigheTavoli()) {
                String[] campiTavolo = dividiCampiTavolo(rigaTavolo);
                if (verificaRigaTavoloValida(campiTavolo)) {
                    SessioneTavolo tavolo = creaTavoloDa(campiTavolo);
                    if (verificaTavoloDisponibile(tavolo, titoloGioco, giornoSettimana)) {
                        tavoliInMemoria.put(tavolo.fornisciIdentificativo(), tavolo);
                    }
                }
            }

            ricercheGiaEseguite.add(chiaveRicerca);
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

        inizializzaArchivioSeNecessario();

        for (String rigaTavolo : leggiRigheTavoli()) {
            String[] campiTavolo = dividiCampiTavolo(rigaTavolo);
            if (verificaRigaTavoloCercato(campiTavolo, idTavolo)) {
                SessioneTavolo tavolo = creaTavoloDa(campiTavolo);
                tavoliInMemoria.put(idTavolo, tavolo);
                return tavolo;
            }
        }

        return null;
    }

    @Override
    public boolean prenotaPosti(int idTavolo, int postiRichiesti) {
        inizializzaArchivioSeNecessario();

        boolean postiPrenotati = false;
        List<String> righeAggiornate = new ArrayList<>();

        for (String rigaTavolo : leggiRigheTavoli()) {
            String[] campiTavolo = dividiCampiTavolo(rigaTavolo);
            if (verificaRigaTavoloCercato(campiTavolo, idTavolo)) {
                SessioneTavolo tavolo = creaTavoloDa(campiTavolo);
                postiPrenotati = tavolo.prenotaPosti(postiRichiesti);
                righeAggiornate.add(postiPrenotati ? creaRigaDa(tavolo) : rigaTavolo); //rappresentazione di un if: [Condizione] ? [Valore se VERO] : [Valore se FALSO]
            } else {
                righeAggiornate.add(rigaTavolo);
            }
        }

        if (postiPrenotati) {
            salvaRigheTavoli(righeAggiornate);
            aggiornaTavoloInMemoria(idTavolo, postiRichiesti);
        }

        return postiPrenotati;
    }

    private boolean verificaTavoloDisponibile(SessioneTavolo tavolo, String titoloGioco,
                                              GiornoSettimana giornoSettimana) {
        return tavolo.fornisciNumeroPostiDisponibili() > 0
                && verificaTitoloCompatibile(tavolo, titoloGioco)
                && verificaGiornoCompatibile(tavolo, giornoSettimana);
    }

    private boolean verificaTitoloCompatibile(SessioneTavolo tavolo, String titoloGioco) {
        return titoloGioco == null
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

    private SessioneTavolo creaTavoloDa(String[] campiTavolo) {
        Gioco gioco = new Gioco(campiTavolo[INDICE_TITOLO_GIOCO], campiTavolo[INDICE_IMMAGINE_GIOCO]);
        return new SessioneTavolo(
                Integer.parseInt(campiTavolo[INDICE_ID_TAVOLO]),
                gioco,
                Integer.parseInt(campiTavolo[INDICE_POSTI_TOTALI]),
                Integer.parseInt(campiTavolo[INDICE_POSTI_DISPONIBILI]),
                GiornoSettimana.valueOf(campiTavolo[INDICE_GIORNO]),
                campiTavolo[INDICE_FASCIA_ORARIA],
                Float.parseFloat(campiTavolo[INDICE_QUOTA])
        );
    }

    private String creaRigaDa(SessioneTavolo tavolo) {
        return String.join(
                SEPARATORE_CAMPI,
                String.valueOf(tavolo.fornisciIdentificativo()),
                tavolo.fornisciTitoloGiocoAssociato(),
                tavolo.fornisciPercorsoImmagineGiocoAssociato(),
                String.valueOf(tavolo.fornisciNumeroPostiTotali()),
                String.valueOf(tavolo.fornisciNumeroPostiDisponibili()),
                tavolo.fornisciGiornoSettimana().name(),
                tavolo.fornisciFasciaOraria(),
                String.valueOf(tavolo.fornisciQuotaPartecipazione())
        );
    }

    private String[] dividiCampiTavolo(String rigaTavolo) {
        return rigaTavolo.split(SEPARATORE_CAMPI, -1);
    }

    private boolean verificaRigaTavoloValida(String[] campiTavolo) {
        return campiTavolo.length == CAMPI_RIGA_TAVOLO;
    }

    private boolean verificaRigaTavoloCercato(String[] campiTavolo, int idTavolo) {
        return verificaRigaTavoloValida(campiTavolo)
                && Integer.parseInt(campiTavolo[INDICE_ID_TAVOLO]) == idTavolo;
    }

    private List<String> leggiRigheTavoli() {
        try {
            return Files.readAllLines(FileSystemPaths.FILE_TAVOLI);
        } catch (IOException exception) {
            throw new IllegalStateException("Impossibile leggere l'archivio tavoli.", exception);
        }
    }

    private void salvaRigheTavoli(List<String> righeTavoli) {
        try {
            Files.write(FileSystemPaths.FILE_TAVOLI, righeTavoli);
        } catch (IOException exception) {
            throw new IllegalStateException("Impossibile salvare l'archivio tavoli.", exception);
        }
    }

    private void inizializzaArchivioSeNecessario() {
        try {
            Files.createDirectories(FileSystemPaths.CARTELLA_DATI);
            if (Files.notExists(FileSystemPaths.FILE_TAVOLI)) {
                salvaRigheTavoli(creaRigheTavoliIniziali());
            }
        } catch (IOException exception) {
            throw new IllegalStateException("Impossibile inizializzare l'archivio tavoli.", exception);
        }
    }

    private List<String> creaRigheTavoliIniziali() {
        return List.of(
                "1;Catan;/com/questtable/images/giochi/catan.png;4;4;GIOVEDI;18:00 - 20:00;12.0",
                "2;Catan;/com/questtable/images/giochi/catan.png;4;2;SABATO;21:00 - 23:00;12.0",
                "3;Ticket to Ride;/com/questtable/images/giochi/ticket-to-ride.png;5;3;VENERDI;19:00 - 21:00;10.0",
                "4;Pandemic;/com/questtable/images/giochi/pandemic.png;4;0;DOMENICA;17:00 - 19:00;8.0"
        );
    }
}
