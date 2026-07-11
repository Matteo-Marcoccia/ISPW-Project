package com.questtable.dao;

import com.questtable.model.Notifica;

import java.util.List;

public interface INotificaDAO {
    void salvaNuovaNotifica(Notifica notifica);

    List<Notifica> recuperaNotificheNonLette(String usernameDestinatario);

    void segnaNotificheComeLette(String usernameDestinatario);
}
