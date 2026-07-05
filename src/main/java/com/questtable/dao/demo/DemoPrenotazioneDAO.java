package com.questtable.dao.demo;

import com.questtable.dao.IPrenotazioneDAO;
import com.questtable.model.Prenotazione;
import com.questtable.model.StatoPrenotazione;

import java.util.ArrayList;
import java.util.List;

public class DemoPrenotazioneDAO implements IPrenotazioneDAO {
    @Override
    public void salvaNuovaPrenotazione(Prenotazione prenotazione) {
        DemoDatabase.prenotazioni.put(prenotazione.fornisciIdentificativo(), prenotazione);
    }

    @Override
    public Prenotazione recuperaPrenotazione(int idPrenotazione) {
        return DemoDatabase.prenotazioni.get(idPrenotazione);
    }

    @Override
    public List<Prenotazione> recuperaPrenotazioniCliente(String usernameCliente) {
        List<Prenotazione> prenotazioniCliente = new ArrayList<>();

        for (Prenotazione prenotazione : DemoDatabase.prenotazioni.values()) {
            if (prenotazione.fornisciUsernameCliente().equals(usernameCliente)) {
                prenotazioniCliente.add(prenotazione);
            }
        }

        return prenotazioniCliente;
    }

    @Override
    public List<Prenotazione> recuperaPrenotazioniInAttesa() {
        List<Prenotazione> prenotazioniInAttesa = new ArrayList<>();

        for (Prenotazione prenotazione : DemoDatabase.prenotazioni.values()) {
            if (prenotazione.fornisciStatoCorrente() == StatoPrenotazione.IN_ATTESA) {
                prenotazioniInAttesa.add(prenotazione);
            }
        }

        return prenotazioniInAttesa;
    }

    @Override
    public void confermaPrenotazione(int idPrenotazione) {
        Prenotazione prenotazione = DemoDatabase.prenotazioni.get(idPrenotazione);
        if (prenotazione != null) {
            prenotazione.confermaPrenotazione();
        }
    }
}
