package com.questtable.bean;

import com.questtable.model.GiornoSettimana;
import com.questtable.model.StatoPrenotazione;

public class PrenotazioneBean {
    private final int idPrenotazione;
    private final String usernameCliente;
    private final String titoloGioco;
    private final int idTavolo;
    private final GiornoSettimana giornoAttivita;
    private final String fasciaOrariaAttivita;
    private final String dataPrenotazione;
    private final String oraPrenotazione;
    private final int postiPrenotati;
    private final float importoTotale;
    private final StatoPrenotazione statoPrenotazione;

    public PrenotazioneBean(int idPrenotazione, String usernameCliente, String titoloGioco,
                            int idTavolo, GiornoSettimana giornoAttivita, String fasciaOrariaAttivita,
                            String dataPrenotazione, String oraPrenotazione,
                            int postiPrenotati, float importoTotale, StatoPrenotazione statoPrenotazione) {
        this.idPrenotazione = idPrenotazione;
        this.usernameCliente = usernameCliente;
        this.titoloGioco = titoloGioco;
        this.idTavolo = idTavolo;
        this.giornoAttivita = giornoAttivita;
        this.fasciaOrariaAttivita = fasciaOrariaAttivita;
        this.dataPrenotazione = dataPrenotazione;
        this.oraPrenotazione = oraPrenotazione;
        this.postiPrenotati = postiPrenotati;
        this.importoTotale = importoTotale;
        this.statoPrenotazione = statoPrenotazione;
    }

    public int fornisciIdentificativoPrenotazione() {
        return idPrenotazione;
    }

    public String fornisciUsernameCliente() {
        return usernameCliente;
    }

    public String fornisciTitoloGioco() {
        return titoloGioco;
    }

    public int fornisciIdentificativoTavolo() {
        return idTavolo;
    }

    public GiornoSettimana fornisciGiornoAttivita() {
        return giornoAttivita;
    }

    public String fornisciFasciaOrariaAttivita() {
        return fasciaOrariaAttivita;
    }

    public String fornisciDataPrenotazione() {
        return dataPrenotazione;
    }

    public String fornisciOraPrenotazione() {
        return oraPrenotazione;
    }

    public int fornisciNumeroPostiPrenotati() {
        return postiPrenotati;
    }

    public float fornisciImportoTotale() {
        return importoTotale;
    }

    public StatoPrenotazione fornisciStatoPrenotazione() {
        return statoPrenotazione;
    }
}
