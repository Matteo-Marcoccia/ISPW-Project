package com.questtable.service;

import com.questtable.bean.NotificaPrenotazioneBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServizioNotifichePrenotazione {
    private static final Logger LOGGER = Logger.getLogger(ServizioNotifichePrenotazione.class.getName());
    private static final Map<String, List<String>> comunicazioniDaConsegnare = new HashMap<>();

    public void inviaNotificaAlGestore(NotificaPrenotazioneBean notificaPrenotazioneBean) {
        inoltraComunicazione(notificaPrenotazioneBean);
        LOGGER.log(
                Level.INFO,
                "Comunicazione inviata al gestore: {0}",
                new Object[]{
                        notificaPrenotazioneBean.fornisciMessaggio()
                }
        );
    }

    public void inviaNotificaAlCliente(NotificaPrenotazioneBean notificaPrenotazioneBean) {
        inoltraComunicazione(notificaPrenotazioneBean);
        LOGGER.log(
                Level.INFO,
                "Comunicazione inviata a {0}: {1}",
                new Object[]{
                        notificaPrenotazioneBean.fornisciDestinatario(),
                        notificaPrenotazioneBean.fornisciMessaggio()
                }
        );
    }

    public List<String> consegnaComunicazioniDisponibiliPer(String destinatario) {
        synchronized (comunicazioniDaConsegnare) {
            List<String> comunicazioni = comunicazioniDaConsegnare.remove(destinatario);
            if (comunicazioni == null) {
                return new ArrayList<>();
            }

            return new ArrayList<>(comunicazioni);
        }
    }

    private void inoltraComunicazione(NotificaPrenotazioneBean notificaPrenotazioneBean) {
        /* In una versione con boundary concorrenti, qui il servizio consegnerebbe subito
         la comunicazione al destinatario online; nel prototipo locale la accoda
         per consegnarla automaticamente appena la boundary torna disponibile. */
        accodaComunicazionePerConsegna(notificaPrenotazioneBean);
    }

    private void accodaComunicazionePerConsegna(NotificaPrenotazioneBean notificaPrenotazioneBean) {
        synchronized (comunicazioniDaConsegnare) {
            comunicazioniDaConsegnare
                    .computeIfAbsent(notificaPrenotazioneBean.fornisciDestinatario(), destinatario -> new ArrayList<>())
                    .add(notificaPrenotazioneBean.fornisciMessaggio());
        }
    }
}
