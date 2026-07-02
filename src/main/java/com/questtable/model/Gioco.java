package com.questtable.model;

public class Gioco {
    private final String titolo;
    private final String imagePath;

    public Gioco(String titolo, String imagePath) {
        this.titolo = titolo;
        this.imagePath = imagePath;
    }

    public String fornisciTitolo() {
        return titolo;
    }

    public String fornisciPercorsoImmagine() {
        return imagePath;
    }
}
