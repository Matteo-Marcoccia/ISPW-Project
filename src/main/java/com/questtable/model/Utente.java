package com.questtable.model;

public class Utente {
    private final String username;
    private final String password;
    private final RuoloUtente ruolo;

    public Utente(String username, String password, RuoloUtente ruolo) {
        this.username = username;
        this.password = password;
        this.ruolo = ruolo;
    }

    public String fornisciUsername() {
        return username;
    }

    public boolean verificaPassword(String password) {
        return this.password.equals(password);
    }

    public RuoloUtente fornisciRuolo() {
        return ruolo;
    }
}
