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
    private static final String USERNAME_CLIENTE_DEMO = "matteo";
    private static final String USERNAME_ALTRO_CLIENTE_DEMO = "erik";
    private static final String USERNAME_GESTORE_DEMO = "admin";
    private static final String CREDENZIALE_GESTORE_DEMO = "admin";

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
        utenti.put(USERNAME_CLIENTE_DEMO, new Cliente(USERNAME_CLIENTE_DEMO, "1234", 0));
        utenti.put(USERNAME_ALTRO_CLIENTE_DEMO, new Cliente(USERNAME_ALTRO_CLIENTE_DEMO, "1234", 0));
        utenti.put(
                USERNAME_GESTORE_DEMO,
                new Utente(USERNAME_GESTORE_DEMO, CREDENZIALE_GESTORE_DEMO, RuoloUtente.GESTORE)
        );
    }

    private static void inizializzaSessioniTavolo() {
        Gioco catan = new Gioco("Catan", "/com/questtable/images/giochi/catan.png");
        Gioco ticketToRide = new Gioco("Ticket to Ride", "/com/questtable/images/giochi/ticket-to-ride.png");
        Gioco pandemic = new Gioco("Pandemic", "/com/questtable/images/giochi/pandemic.png");
        Gioco karakIi = new Gioco("Karak II", "/com/questtable/images/giochi/karak-ii.png");
        Gioco monopoly = new Gioco("Monopoly", "/com/questtable/images/giochi/monopoly.png");

        sessioniTavolo.put(1, new SessioneTavolo(1, catan, 4, 4, GiornoSettimana.GIOVEDI, "18:00 - 20:00", 12.00f));
        sessioniTavolo.put(2, new SessioneTavolo(2, catan, 4, 2, GiornoSettimana.SABATO, "21:00 - 23:00", 12.00f));
        sessioniTavolo.put(3, new SessioneTavolo(3, ticketToRide, 5, 3, GiornoSettimana.VENERDI, "19:00 - 21:00", 10.00f));
        sessioniTavolo.put(4, new SessioneTavolo(4, pandemic, 4, 0, GiornoSettimana.DOMENICA, "17:00 - 19:00", 8.00f));
        sessioniTavolo.put(5, new SessioneTavolo(5, karakIi, 5, 5, GiornoSettimana.MARTEDI, "18:30 - 20:30", 9.00f));
        sessioniTavolo.put(6, new SessioneTavolo(6, monopoly, 6, 6, GiornoSettimana.MERCOLEDI, "20:00 - 22:00", 7.00f));
    }

    private static void inizializzaPrenotazioni() {
        Cliente cliente = (Cliente) utenti.get(USERNAME_CLIENTE_DEMO);
        SessioneTavolo tavoloCatan = sessioniTavolo.get(1);
        SessioneTavolo tavoloTicketToRide = sessioniTavolo.get(3);

        registraPrenotazioneDemo(1, cliente, tavoloCatan, 1);
        registraPrenotazioneDemo(2, cliente, tavoloTicketToRide, 2);
    }

    private static void registraPrenotazioneDemo(int idPrenotazione, Cliente cliente,
                                                 SessioneTavolo tavolo, int postiPrenotati) {
        if (!tavolo.prenotaPosti(postiPrenotati)) {
            return;
        }

        prenotazioni.put(
                idPrenotazione,
                new Prenotazione(
                        idPrenotazione,
                        cliente,
                        tavolo,
                        postiPrenotati,
                        tavolo.calcolaImportoPer(postiPrenotati)
                )
        );
    }
}
