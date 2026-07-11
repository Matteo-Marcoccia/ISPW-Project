package com.questtable.bean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ListaNotificheBean {
    private final List<String> messaggi;

    public ListaNotificheBean(List<String> messaggi) {
        this.messaggi = new ArrayList<>(messaggi);
    }

    public List<String> fornisciMessaggi() {
        return Collections.unmodifiableList(messaggi);
    }

    public boolean verificaAssenzaNotifiche() {
        return messaggi.isEmpty();
    }
}
