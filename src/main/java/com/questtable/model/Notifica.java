package com.questtable.model;

public class Notifica {
    private final String usernameDestinatario;
    private final TipoNotifica tipoNotifica;
    private final String messaggio;
    private boolean letta;

    public Notifica(String usernameDestinatario, TipoNotifica tipoNotifica, String messaggio) {
        this(usernameDestinatario, tipoNotifica, messaggio, false);
    }

    public Notifica(String usernameDestinatario, TipoNotifica tipoNotifica, String messaggio, boolean letta) {
        this.usernameDestinatario = usernameDestinatario;
        this.tipoNotifica = tipoNotifica;
        this.messaggio = messaggio;
        this.letta = letta;
    }

    public String fornisciUsernameDestinatario() {
        return usernameDestinatario;
    }

    public TipoNotifica fornisciTipoNotifica() {
        return tipoNotifica;
    }

    public String fornisciMessaggio() {
        return messaggio;
    }

    public boolean verificaNonLetta() {
        return !letta;
    }

    public void segnaComeLetta() {
        letta = true;
    }
}
