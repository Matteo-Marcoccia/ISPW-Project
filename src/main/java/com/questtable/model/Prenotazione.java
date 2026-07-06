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
                LocalDate.now(OROLOGIO_PRENOTAZIONE),
                LocalTime.now(OROLOGIO_PRENOTAZIONE),
                postiPrenotati,
                importoTotale,
                StatoPrenotazione.IN_ATTESA
        );
    }

    public Prenotazione(int idPrenotazione, Cliente cliente, SessioneTavolo sessioneTavolo,
                        LocalDate dataPrenotazione, LocalTime oraPrenotazione,
                        int postiPrenotati, float importoTotale, StatoPrenotazione stato) {
        this.idPrenotazione = idPrenotazione;
        this.cliente = cliente;
        this.sessioneTavolo = sessioneTavolo;
        this.dataPrenotazione = dataPrenotazione;
        this.oraPrenotazione = oraPrenotazione;
        this.postiPrenotati = postiPrenotati;
        this.importoTotale = importoTotale;
        this.stato = stato;
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

    public void accreditaPuntiFedeltaAlCliente() {
        cliente.accreditaPuntiFedelta(calcolaPuntiFedeltaDaAccreditare());
    }

    public int calcolaPuntiFedeltaDaAccreditare() {
        return Math.round(importoTotale * 10);
    }
}
