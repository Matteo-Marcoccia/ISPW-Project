package com.questtable.dao;

import com.questtable.model.Utente;

public interface IUtenteDAO {
    Utente recuperaUtente(String username);

    void aggiornaPuntiFedelta(String username, int puntiFedelta);
}
