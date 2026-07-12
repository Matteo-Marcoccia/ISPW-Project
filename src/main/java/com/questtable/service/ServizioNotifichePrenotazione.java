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
    private static final Map<String, List<String>> comunicazioniInAttesa = new HashMap<>();

    public void inviaNotificaAlGestore(NotificaPrenotazioneBean notificaPrenotazioneBean) {
        registraComunicazione(notificaPrenotazioneBean);
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
        registraComunicazione(notificaPrenotazioneBean);
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

    public List<String> prelevaComunicazioniPer(String destinatario) {
        synchronized (comunicazioniInAttesa) {
            List<String> comunicazioni = comunicazioniInAttesa.remove(destinatario);
            if (comunicazioni == null) {
                return new ArrayList<>();
            }

            return new ArrayList<>(comunicazioni);
        }
    }

    private void registraComunicazione(NotificaPrenotazioneBean notificaPrenotazioneBean) {
        synchronized (comunicazioniInAttesa) {
            comunicazioniInAttesa
                    .computeIfAbsent(notificaPrenotazioneBean.fornisciDestinatario(), destinatario -> new ArrayList<>())
                    .add(notificaPrenotazioneBean.fornisciMessaggio());
        }
    }
}
