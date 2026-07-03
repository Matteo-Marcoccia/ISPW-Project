package com.questtable.bean;

public class PostiTavoloBean {
    private final int postiTotali;
    private final int postiDisponibili;

    public PostiTavoloBean(int postiTotali, int postiDisponibili) {
        this.postiTotali = postiTotali;
        this.postiDisponibili = postiDisponibili;
    }

    public int fornisciNumeroPostiTotali() {
        return postiTotali;
    }

    public int fornisciNumeroPostiDisponibili() {
        return postiDisponibili;
    }
}
