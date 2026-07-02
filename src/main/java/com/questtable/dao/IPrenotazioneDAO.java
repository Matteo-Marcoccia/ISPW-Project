package com.questtable.dao;

import com.questtable.model.Prenotazione;

import java.util.List;

public interface IPrenotazioneDAO {
    void salvaNuovaPrenotazione(Prenotazione prenotazione);

    Prenotazione recuperaPrenotazione(int idPrenotazione);

    List<Prenotazione> recuperaPrenotazioniCliente(String usernameCliente);

    List<Prenotazione> recuperaPrenotazioniInAttesa();

    void confermaPrenotazione(int idPrenotazione);
}
