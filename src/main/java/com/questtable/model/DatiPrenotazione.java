package com.questtable.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class DatiPrenotazione {
    private final LocalDate dataPrenotazione;
    private final LocalTime oraPrenotazione;
    private final StatoPrenotazione statoPrenotazione;

    public DatiPrenotazione(LocalDate dataPrenotazione, LocalTime oraPrenotazione,
                            StatoPrenotazione statoPrenotazione) {
        this.dataPrenotazione = dataPrenotazione;
        this.oraPrenotazione = oraPrenotazione;
        this.statoPrenotazione = statoPrenotazione;
    }

    public LocalDate fornisciDataPrenotazione() {
        return dataPrenotazione;
    }

    public LocalTime fornisciOraPrenotazione() {
        return oraPrenotazione;
    }

    public StatoPrenotazione fornisciStatoPrenotazione() {
        return statoPrenotazione;
    }
}
