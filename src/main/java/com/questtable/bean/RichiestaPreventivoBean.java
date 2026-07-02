package com.questtable.bean;

public class RichiestaPreventivoBean {
    private final int idTavolo;
    private final int postiRichiesti;

    public RichiestaPreventivoBean(int idTavolo, int postiRichiesti) {
        this.idTavolo = idTavolo;
        this.postiRichiesti = postiRichiesti;
    }

    public int fornisciIdentificativoTavolo() {
        return idTavolo;
    }

    public int fornisciNumeroPostiRichiesti() {
        return postiRichiesti;
    }

    public boolean verificaRichiestaValida() {
        return idTavolo > 0 && postiRichiesti > 0;
    }
}
