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
        this(
                idPrenotazione,
                cliente,
                sessioneTavolo,
                new DatiPrenotazione(
                        LocalDate.now(OROLOGIO_PRENOTAZIONE),
                        LocalTime.now(OROLOGIO_PRENOTAZIONE),
                        StatoPrenotazione.IN_ATTESA
                ),
                postiPrenotati,
                importoTotale
        );
    }

    public Prenotazione(int idPrenotazione, Cliente cliente, SessioneTavolo sessioneTavolo,
                        DatiPrenotazione datiPrenotazione, int postiPrenotati, float importoTotale) {
        this.idPrenotazione = idPrenotazione;
        this.cliente = cliente;
        this.sessioneTavolo = sessioneTavolo;
        this.dataPrenotazione = datiPrenotazione.fornisciDataPrenotazione();
        this.oraPrenotazione = datiPrenotazione.fornisciOraPrenotazione();
        this.postiPrenotati = postiPrenotati;
        this.importoTotale = importoTotale;
        this.stato = datiPrenotazione.fornisciStatoPrenotazione();
    }

    public int fornisciIdentificativo() {
        return idPrenotazione;
    }

    public String fornisciUsernameCliente() {
        return cliente.fornisciUsername();
    }

    public int fornisciPuntiFedeltaCliente() {
        return cliente.fornisciPuntiFedelta();
    }

    public String fornisciTitoloGiocoPrenotato() {
        return sessioneTavolo.fornisciTitoloGiocoAssociato();
    }

    public int fornisciIdentificativoTavoloPrenotato() {
        return sessioneTavolo.fornisciIdentificativo();
    }

    public GiornoSettimana fornisciGiornoAttivitaPrenotata() {
        return sessioneTavolo.fornisciGiornoSettimana();
    }

    public String fornisciFasciaOrariaAttivitaPrenotata() {
        return sessioneTavolo.fornisciFasciaOraria();
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

    public void assegnaPuntiFedeltaAlCliente() {
        cliente.riceviPuntiFedeltaDa(importoTotale);
    }
}
