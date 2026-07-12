package com.questtable.controller;

import com.questtable.bean.LoginBean;
import com.questtable.bean.ProfiloUtenteBean;
import com.questtable.dao.DAOFactory;
import com.questtable.dao.IUtenteDAO;
import com.questtable.model.Cliente;
import com.questtable.model.Utente;
import com.questtable.session.Session;
import com.questtable.session.SessionManagerSingleton;

public class LoginControllerApplicativo {
    private final IUtenteDAO utenteDAO;
    private final SessionManagerSingleton sessionManager;

    public LoginControllerApplicativo() {
        DAOFactory daoFactory = DAOFactory.fornisciDAOFactory();
        utenteDAO = daoFactory.fornisciUtenteDAO();
        sessionManager = SessionManagerSingleton.fornisciIstanza();
    }

    public String effettuaLogin(LoginBean loginBean) {
        if (loginBean == null || !loginBean.verificaCampiCompilati()) {
            throw new IllegalArgumentException("Credenziali non compilate correttamente.");
        }

        Utente utente = utenteDAO.recuperaUtente(loginBean.fornisciUsername());
        if (utente == null) {
            throw new IllegalArgumentException("Utente non registrato.");
        }

        if (!utente.verificaPassword(loginBean.fornisciPassword())) {
            throw new IllegalArgumentException("Password non valida.");
        }

        return sessionManager.apriSessione(utente);
    }

    public ProfiloUtenteBean fornisciProfiloUtente(String idSessione) {
        Session sessione = fornisciSessione(idSessione);
        Utente utente = utenteDAO.recuperaUtente(sessione.fornisciUsername());
        if (utente == null) {
            throw new IllegalStateException("Utente collegato alla sessione non trovato.");
        }

        int puntiFedelta = 0;
        if (utente instanceof Cliente cliente) {
            puntiFedelta = cliente.fornisciPuntiFedelta();
        }

        return new ProfiloUtenteBean(
                utente.fornisciUsername(),
                utente.fornisciRuolo(),
                puntiFedelta
        );
    }

    public void effettuaLogout(String idSessione) {
        sessionManager.chiudiSessione(idSessione);
    }

    private Session fornisciSessione(String idSessione) {
        Session sessione = sessionManager.fornisciSessione(idSessione);
        if (sessione == null) {
            throw new IllegalStateException("Sessione non valida.");
        }

        return sessione;
    }
}
