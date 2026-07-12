package com.questtable.controller;

import com.questtable.bean.LoginBean;
import com.questtable.bean.ProfiloUtenteBean;
import com.questtable.dao.DAOFactory;
import com.questtable.dao.IUtenteDAO;
import com.questtable.exception.InvalidCredentialsException;
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
            throw new InvalidCredentialsException("Credenziali non compilate correttamente.");
        }

        Utente utente = utenteDAO.recuperaUtente(loginBean.fornisciUsername());
        if (utente == null) {
            throw new InvalidCredentialsException("Utente non registrato.");
        }

        if (!utente.verificaPassword(loginBean.fornisciPassword())) {
            throw new InvalidCredentialsException("Password non valida.");
        }

        return sessionManager.apriSessione(utente);
    }

    public ProfiloUtenteBean fornisciProfiloUtente(String idSessione) {
        Session sessione = sessionManager.fornisciSessioneValida(idSessione);
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
}
