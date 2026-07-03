package com.questtable.bean;

import com.questtable.model.GiornoSettimana;
import com.questtable.model.StatoPrenotazione;

public class PrenotazioneBean {
    private final int idPrenotazione;
    private final String usernameCliente;
    private final TavoloPrenotatoBean tavoloPrenotato;
    private final String dataPrenotazione;
    private final String oraPrenotazione;
    private final StatoPrenotazione statoPrenotazione;

    public PrenotazioneBean(int idPrenotazione, String usernameCliente, TavoloPrenotatoBean tavoloPrenotato,
                            String dataPrenotazione, String oraPrenotazione,
                            StatoPrenotazione statoPrenotazione) {
        this.idPrenotazione = idPrenotazione;
        this.usernameCliente = usernameCliente;
        this.tavoloPrenotato = tavoloPrenotato;
        this.dataPrenotazione = dataPrenotazione;
        this.oraPrenotazione = oraPrenotazione;
        this.statoPrenotazione = statoPrenotazione;
    }

    public int fornisciIdentificativoPrenotazione() {
        return idPrenotazione;
    }

    public String fornisciUsernameCliente() {
        return usernameCliente;
    }

    public String fornisciTitoloGioco() {
        return tavoloPrenotato.fornisciTitoloGioco();
    }

    public GiornoSettimana fornisciGiornoAttivita() {
        return tavoloPrenotato.fornisciGiornoAttivita();
    }

    public String fornisciFasciaOrariaAttivita() {
        return tavoloPrenotato.fornisciFasciaOrariaAttivita();
    }

    public String fornisciDataPrenotazione() {
        return dataPrenotazione;
    }

    public String fornisciOraPrenotazione() {
        return oraPrenotazione;
    }

    public int fornisciNumeroPostiPrenotati() {
        return tavoloPrenotato.fornisciNumeroPostiPrenotati();
    }

    public float fornisciImportoTotale() {
        return tavoloPrenotato.fornisciImportoTotale();
    }

    public StatoPrenotazione fornisciStatoPrenotazione() {
        return statoPrenotazione;
    }
}
