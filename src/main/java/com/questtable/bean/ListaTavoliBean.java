package com.questtable.bean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ListaTavoliBean {
    private final List<InfoTavoloBean> tavoli;

    public ListaTavoliBean(List<InfoTavoloBean> tavoli) {
        this.tavoli = new ArrayList<>(tavoli);
    }

    public List<InfoTavoloBean> fornisciTavoli() {
        return Collections.unmodifiableList(tavoli);
    }

    public boolean verificaPresenzaTavoli() {
        return !tavoli.isEmpty();
    }
}
