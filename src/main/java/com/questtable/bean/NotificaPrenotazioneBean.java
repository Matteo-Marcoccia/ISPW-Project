package com.questtable.bean;

public class NotificaPrenotazioneBean {
    private final int idPrenotazione;
    private final String destinatario;
    private final String messaggio;

    public NotificaPrenotazioneBean(int idPrenotazione, String destinatario, String messaggio) {
        this.idPrenotazione = idPrenotazione;
        this.destinatario = destinatario;
        this.messaggio = messaggio;
    }

    public int fornisciIdentificativoPrenotazione() {
        return idPrenotazione;
    }

    public String fornisciDestinatario() {
        return destinatario;
    }

    public String fornisciMessaggio() {
        return messaggio;
    }
}
