package com.questtable.dao.file_system;

import com.questtable.dao.INotificaDAO;
import com.questtable.model.Notifica;
import com.questtable.model.TipoNotifica;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class FileSystemNotificaDAO implements INotificaDAO {
    private static final String SEPARATORE_CAMPI = ";";
    private static final int CAMPI_RIGA_NOTIFICA = 4;
    private static final int INDICE_USERNAME_DESTINATARIO = 0;
    private static final int INDICE_TIPO = 1;
    private static final int INDICE_MESSAGGIO = 2;
    private static final int INDICE_LETTA = 3;

    @Override
    public void salvaNuovaNotifica(Notifica notifica) {
        inizializzaArchivioSeNecessario();

        List<String> righeNotifiche = new ArrayList<>(leggiRigheNotifiche());
        righeNotifiche.add(creaRigaDa(notifica));
        salvaRigheNotifiche(righeNotifiche);
    }

    @Override
    public List<Notifica> recuperaNotificheNonLette(String usernameDestinatario) {
        inizializzaArchivioSeNecessario();

        List<Notifica> notificheNonLette = new ArrayList<>();
        for (String rigaNotifica : leggiRigheNotifiche()) {
            String[] campiNotifica = dividiCampiNotifica(rigaNotifica);
            if (verificaRigaNotificaDaConsegnare(campiNotifica, usernameDestinatario)) {
                notificheNonLette.add(creaNotificaDa(campiNotifica));
            }
        }

        return notificheNonLette;
    }

    @Override
    public void segnaNotificheComeLette(String usernameDestinatario) {
        inizializzaArchivioSeNecessario();

        List<String> righeAggiornate = new ArrayList<>();
        for (String rigaNotifica : leggiRigheNotifiche()) {
            String[] campiNotifica = dividiCampiNotifica(rigaNotifica);
            if (verificaRigaNotificaDaConsegnare(campiNotifica, usernameDestinatario)) {
                campiNotifica[INDICE_LETTA] = Boolean.TRUE.toString();
                righeAggiornate.add(String.join(SEPARATORE_CAMPI, campiNotifica));
            } else {
                righeAggiornate.add(rigaNotifica);
            }
        }

        salvaRigheNotifiche(righeAggiornate);
    }

    private Notifica creaNotificaDa(String[] campiNotifica) {
        return new Notifica(
                campiNotifica[INDICE_USERNAME_DESTINATARIO],
                TipoNotifica.valueOf(campiNotifica[INDICE_TIPO]),
                campiNotifica[INDICE_MESSAGGIO],
                Boolean.parseBoolean(campiNotifica[INDICE_LETTA])
        );
    }

    private String creaRigaDa(Notifica notifica) {
        return String.join(
                SEPARATORE_CAMPI,
                notifica.fornisciUsernameDestinatario(),
                notifica.fornisciTipoNotifica().name(),
                notifica.fornisciMessaggio(),
                Boolean.FALSE.toString()
        );
    }

    private String[] dividiCampiNotifica(String rigaNotifica) {
        return rigaNotifica.split(SEPARATORE_CAMPI, -1);
    }

    private boolean verificaRigaNotificaDaConsegnare(String[] campiNotifica, String usernameDestinatario) {
        return campiNotifica.length == CAMPI_RIGA_NOTIFICA
                && campiNotifica[INDICE_USERNAME_DESTINATARIO].equals(usernameDestinatario)
                && !Boolean.parseBoolean(campiNotifica[INDICE_LETTA]);
    }

    private List<String> leggiRigheNotifiche() {
        try {
            return Files.readAllLines(FileSystemPaths.FILE_NOTIFICHE);
        } catch (IOException exception) {
            throw new IllegalStateException("Impossibile leggere l'archivio notifiche.", exception);
        }
    }

    private void salvaRigheNotifiche(List<String> righeNotifiche) {
        try {
            Files.write(FileSystemPaths.FILE_NOTIFICHE, righeNotifiche);
        } catch (IOException exception) {
            throw new IllegalStateException("Impossibile salvare l'archivio notifiche.", exception);
        }
    }

    private void inizializzaArchivioSeNecessario() {
        try {
            Files.createDirectories(FileSystemPaths.CARTELLA_DATI);
            if (Files.notExists(FileSystemPaths.FILE_NOTIFICHE)) {
                salvaRigheNotifiche(new ArrayList<>());
            }
        } catch (IOException exception) {
            throw new IllegalStateException("Impossibile inizializzare l'archivio notifiche.", exception);
        }
    }
}
