package com.questtable.dao.demo;

import com.questtable.dao.IUtenteDAO;
import com.questtable.model.Cliente;
import com.questtable.model.Utente;

public class DemoUtenteDAO implements IUtenteDAO {
    @Override
    public Utente recuperaUtente(String username) {
        return DemoDatabase.utenti.get(username);
    }

    @Override
    public void aggiornaPuntiFedelta(String username, int puntiFedelta) {
        Utente utente = DemoDatabase.utenti.get(username);
        if (utente instanceof Cliente cliente) {
            cliente.aggiornaPuntiFedelta(puntiFedelta);
        }
    }
}
