package com.questtable.service;

import com.questtable.bean.NotificaPrenotazioneBean;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ServizioNotifichePrenotazione {
    private static final Logger LOGGER = Logger.getLogger(ServizioNotifichePrenotazione.class.getName());

    public void inviaNotificaAlGestore(NotificaPrenotazioneBean notificaPrenotazioneBean) {
        LOGGER.log(
                Level.INFO,
                "Comunicazione inviata al gestore per la prenotazione #{0}: {1}",
                new Object[]{
                        notificaPrenotazioneBean.fornisciIdentificativoPrenotazione(),
                        notificaPrenotazioneBean.fornisciMessaggio()
                }
        );
    }

    public void inviaNotificaAlCliente(NotificaPrenotazioneBean notificaPrenotazioneBean) {
        LOGGER.log(
                Level.INFO,
                "Comunicazione inviata a {0} per la prenotazione #{1}: {2}",
                new Object[]{
                        notificaPrenotazioneBean.fornisciDestinatario(),
                        notificaPrenotazioneBean.fornisciIdentificativoPrenotazione(),
                        notificaPrenotazioneBean.fornisciMessaggio()
                }
        );
    }
}
