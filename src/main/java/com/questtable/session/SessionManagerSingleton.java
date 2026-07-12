package com.questtable.session;

import com.questtable.model.RuoloUtente;
import com.questtable.model.Utente;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@SuppressWarnings("java:S6548")
public class SessionManagerSingleton {
    private static SessionManagerSingleton istanzaSingleton;

    private final Map<String, Session> sessioniAttive;

    private SessionManagerSingleton() {
        sessioniAttive = new HashMap<>();
    }

    public static SessionManagerSingleton fornisciIstanza() {
        if (istanzaSingleton == null) {
            istanzaSingleton = new SessionManagerSingleton();
        }

        return istanzaSingleton;
    }

    public String apriSessione(Utente utente) {
        String idSessioneAttiva = cercaIdentificativoSessioneAttivaPer(utente.fornisciUsername());
        if (idSessioneAttiva != null) {
            throw new IllegalStateException("Utente gia' loggato.");
        }

        String idSessione = UUID.randomUUID().toString();
        Session session = new Session(
                idSessione,
                utente
        );

        sessioniAttive.put(idSessione, session);
        return idSessione;
    }

    public Session fornisciSessione(String idSessione) {
        return sessioniAttive.get(idSessione);
    }

    public Session fornisciSessioneValida(String idSessione) {
        Session sessione = fornisciSessione(idSessione);
        if (sessione == null) {
            throw new IllegalStateException("Sessione non valida.");
        }

        return sessione;
    }

    public void verificaSessioneConRuolo(String idSessione, RuoloUtente ruoloRichiesto) {
        Session sessione = fornisciSessioneValida(idSessione);
        verificaRuoloSessione(sessione, ruoloRichiesto);
    }

    public Session fornisciSessioneConRuolo(String idSessione, RuoloUtente ruoloRichiesto) {
        Session sessione = fornisciSessioneValida(idSessione);
        verificaRuoloSessione(sessione, ruoloRichiesto);

        return sessione;
    }

    public void chiudiSessione(String idSessione) {
        sessioniAttive.remove(idSessione);
    }

    private String cercaIdentificativoSessioneAttivaPer(String username) {
        for (Session session : sessioniAttive.values()) {
            if (session.fornisciUsername().equals(username)) {
                return session.fornisciIdentificativoSessione();
            }
        }
        return null;
    }

    private void verificaRuoloSessione(Session sessione, RuoloUtente ruoloRichiesto) {
        if (!sessione.verificaRuolo(ruoloRichiesto)) {
            throw new IllegalStateException("Sessione non valida per l'operazione richiesta.");
        }
    }
}
