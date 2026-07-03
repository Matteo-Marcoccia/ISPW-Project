package com.questtable.model;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;

public class Prenotazione {
    private static final Clock OROLOGIO_PRENOTAZIONE = Clock.system(ZoneId.of("Europe/Rome"));

    private final int idPrenotazione;
    private final Cliente cliente;
    private final SessioneTavolo sessioneTavolo;
    private final LocalDate dataPrenotazione;
    private final LocalTime oraPrenotazione;
    private final int postiPrenotati;
    private final float importoTotale;
    private StatoPrenotazione stato;

    public Prenotazione(int idPrenotazione, Cliente cliente, SessioneTavolo sessioneTavolo,
                        int postiPrenotati, float importoTotale) {
        this.idPrenotazione = idPrenotazione;
        this.cliente = cliente;
        this.sessioneTavolo = sessioneTavolo;
        this.postiPrenotati = postiPrenotati;
        this.importoTotale = importoTotale;
        this.dataPrenotazione = LocalDate.now(OROLOGIO_PRENOTAZIONE);
        this.oraPrenotazione = LocalTime.now(OROLOGIO_PRENOTAZIONE);
        this.stato = StatoPrenotazione.IN_ATTESA;
    }

    public int fornisciIdentificativo() {
        return idPrenotazione;
    }

    public Cliente fornisciClienteAssociato() {
        return cliente;
    }

    public SessioneTavolo fornisciSessioneTavoloAssociata() {
        return sessioneTavolo;
    }

    public LocalDate fornisciDataPrenotazione() {
        return dataPrenotazione;
    }

    public LocalTime fornisciOraPrenotazione() {
        return oraPrenotazione;
    }

    public int fornisciNumeroPostiPrenotati() {
        return postiPrenotati;
    }

    public float fornisciImportoTotale() {
        return importoTotale;
    }

    public StatoPrenotazione fornisciStatoCorrente() {
        return stato;
    }

    public void confermaPrenotazione() {
        stato = StatoPrenotazione.CONFERMATA;
    }

    public int calcolaPuntiFedeltaDaAccreditare() {
        return Math.round(importoTotale * 10);
    }
}
