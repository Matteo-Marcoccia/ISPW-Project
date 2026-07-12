package com.questtable.session;

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
            return idSessioneAttiva;
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
}
