package com.questtable.dao.demo;

import com.questtable.model.Cliente;
import com.questtable.model.GiornoSettimana;
import com.questtable.model.Gioco;
import com.questtable.model.Prenotazione;
import com.questtable.model.RuoloUtente;
import com.questtable.model.SessioneTavolo;
import com.questtable.model.Utente;

import java.util.HashMap;
import java.util.Map;

final class DemoDatabase {
    static final Map<String, Utente> utenti = new HashMap<>();
    static final Map<Integer, SessioneTavolo> sessioniTavolo = new HashMap<>();
    static final Map<Integer, Prenotazione> prenotazioni = new HashMap<>();

    static {
        inizializzaUtenti();
        inizializzaSessioniTavolo();
        inizializzaPrenotazioni();
    }

    private DemoDatabase() {
    }

    private static void inizializzaUtenti() {
        utenti.put("matteo", new Cliente("matteo", "1234", 0));
        utenti.put("admin", new Utente("admin", "admin", RuoloUtente.GESTORE));
    }

    private static void inizializzaSessioniTavolo() {
        Gioco catan = new Gioco("Catan", "/com/questtable/images/giochi/catan.png");
        Gioco ticketToRide = new Gioco("Ticket to Ride", "/com/questtable/images/giochi/ticket-to-ride.png");
        Gioco pandemic = new Gioco("Pandemic", "/com/questtable/images/giochi/pandemic.png");

        sessioniTavolo.put(1, new SessioneTavolo(1, catan, 4, 4, GiornoSettimana.GIOVEDI, "18:00 - 20:00", 12.00f));
        sessioniTavolo.put(2, new SessioneTavolo(2, catan, 4, 2, GiornoSettimana.SABATO, "21:00 - 23:00", 12.00f));
        sessioniTavolo.put(3, new SessioneTavolo(3, ticketToRide, 5, 3, GiornoSettimana.VENERDI, "19:00 - 21:00", 10.00f));
        sessioniTavolo.put(4, new SessioneTavolo(4, pandemic, 4, 0, GiornoSettimana.DOMENICA, "17:00 - 19:00", 8.00f));
    }

    private static void inizializzaPrenotazioni() {
        Cliente cliente = (Cliente) utenti.get("matteo");
        SessioneTavolo tavoloCatan = sessioniTavolo.get(1);
        SessioneTavolo tavoloTicketToRide = sessioniTavolo.get(3);

        prenotazioni.put(1, new Prenotazione(1, cliente, tavoloCatan, 1, 12.00f));
        prenotazioni.put(2, new Prenotazione(2, cliente, tavoloTicketToRide, 1, 10.00f));
    }
}
