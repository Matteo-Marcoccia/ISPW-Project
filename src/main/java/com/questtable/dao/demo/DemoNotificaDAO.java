package com.questtable.dao.demo;

import com.questtable.dao.INotificaDAO;
import com.questtable.model.Notifica;

import java.util.ArrayList;
import java.util.List;

public class DemoNotificaDAO implements INotificaDAO {
    @Override
    public void salvaNuovaNotifica(Notifica notifica) {
        DemoDatabase.notifiche.add(notifica);
    }

    @Override
    public List<Notifica> recuperaNotificheNonLette(String usernameDestinatario) {
        List<Notifica> notificheNonLette = new ArrayList<>();

        for (Notifica notifica : DemoDatabase.notifiche) {
            if (verificaNotificaDaConsegnare(notifica, usernameDestinatario)) {
                notificheNonLette.add(notifica);
            }
        }

        return notificheNonLette;
    }

    @Override
    public void segnaNotificheComeLette(String usernameDestinatario) {
        for (Notifica notifica : DemoDatabase.notifiche) {
            if (verificaNotificaDaConsegnare(notifica, usernameDestinatario)) {
                notifica.segnaComeLetta();
            }
        }
    }

    private boolean verificaNotificaDaConsegnare(Notifica notifica, String usernameDestinatario) {
        return notifica.verificaNonLetta()
                && notifica.fornisciUsernameDestinatario().equals(usernameDestinatario);
    }

}
