package com.questtable.model;

public class SessioneTavolo {
    private final int idTavolo;
    private final Gioco gioco;
    private final int postiTotali;
    private int postiDisponibili;
    private final GiornoSettimana giornoSettimana;
    private final String fasciaOraria;
    private final float quota;

    public SessioneTavolo(int idTavolo, Gioco gioco, int postiTotali, int postiDisponibili,
                          GiornoSettimana giornoSettimana, String fasciaOraria, float quota) {
        this.idTavolo = idTavolo;
        this.gioco = gioco;
        this.postiTotali = postiTotali;
        this.postiDisponibili = postiDisponibili;
        this.giornoSettimana = giornoSettimana;
        this.fasciaOraria = fasciaOraria;
        this.quota = quota;
    }

    public int fornisciIdentificativo() {
        return idTavolo;
    }

    public Gioco fornisciGiocoAssociato() {
        return gioco;
    }

    public int fornisciNumeroPostiTotali() {
        return postiTotali;
    }

    public int fornisciNumeroPostiDisponibili() {
        return postiDisponibili;
    }

    public GiornoSettimana fornisciGiornoSettimana() {
        return giornoSettimana;
    }

    public String fornisciFasciaOraria() {
        return fasciaOraria;
    }

    public float fornisciQuotaPartecipazione() {
        return quota;
    }

    public boolean verificaPostiNonPrenotabili(int postiRichiesti) {
        return postiRichiesti <= 0 || postiRichiesti > postiDisponibili;
    }

    public boolean prenotaPosti(int postiRichiesti) {
        if (verificaPostiNonPrenotabili(postiRichiesti)) {
            return false;
        }

        postiDisponibili -= postiRichiesti;
        return true;
    }
}
