package com.questtable.bean;

public class NotificaPrenotazioneBean {
    private final String destinatario;
    private final String messaggio;

    public NotificaPrenotazioneBean(String destinatario, String messaggio) {
        this.destinatario = destinatario;
        this.messaggio = messaggio;
    }

    public String fornisciDestinatario() {
        return destinatario;
    }

    public String fornisciMessaggio() {
        return messaggio;
    }
}
