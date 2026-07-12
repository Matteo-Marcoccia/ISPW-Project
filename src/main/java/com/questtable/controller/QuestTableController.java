package com.questtable.controller;

import com.questtable.bean.InfoTavoloBean;
import com.questtable.bean.ListaPrenotazioniBean;
import com.questtable.bean.ListaTavoliBean;
import com.questtable.bean.NotificaPrenotazioneBean;
import com.questtable.bean.PagamentoBean;
import com.questtable.bean.PostiTavoloBean;
import com.questtable.bean.PrenotazioneBean;
import com.questtable.bean.PreventivoBean;
import com.questtable.bean.RicercaTavoliBean;
import com.questtable.bean.RichiestaPreventivoBean;
import com.questtable.bean.TavoloPrenotatoBean;
import com.questtable.dao.DAOFactory;
import com.questtable.dao.IPrenotazioneDAO;
import com.questtable.dao.ISessioneTavoloDAO;
import com.questtable.dao.IUtenteDAO;
import com.questtable.model.Cliente;
import com.questtable.model.GiornoSettimana;
import com.questtable.model.Prenotazione;
import com.questtable.model.RegolaPuntiFedelta;
import com.questtable.model.RuoloUtente;
import com.questtable.model.SessioneTavolo;
import com.questtable.model.StatoPrenotazione;
import com.questtable.model.Utente;
import com.questtable.service.ServizioNotifichePrenotazione;
import com.questtable.session.Session;
import com.questtable.session.SessionManagerSingleton;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class QuestTableController {
    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter FORMATO_ORA = DateTimeFormatter.ofPattern("HH:mm");
    private static final String DESTINATARIO_GESTORE = "gestore";

    private final IUtenteDAO utenteDAO;
    private final ISessioneTavoloDAO sessioneTavoloDAO;
    private final IPrenotazioneDAO prenotazioneDAO;
    private final ServizioNotifichePrenotazione servizioNotifichePrenotazione;
    private final SessionManagerSingleton sessionManager;

    public QuestTableController() {
        DAOFactory daoFactory = DAOFactory.fornisciDAOFactory();
        utenteDAO = daoFactory.fornisciUtenteDAO();
        sessioneTavoloDAO = daoFactory.fornisciSessioneTavoloDAO();
        prenotazioneDAO = daoFactory.fornisciPrenotazioneDAO();
        servizioNotifichePrenotazione = new ServizioNotifichePrenotazione();
        sessionManager = SessionManagerSingleton.fornisciIstanza();
    }

    public ListaTavoliBean fornisciTavoliDisponibili(String idSessione, RicercaTavoliBean ricercaTavoliBean) {
        fornisciSessioneConRuolo(idSessione, RuoloUtente.CLIENTE);

        String titoloGioco = null;
        GiornoSettimana giornoSettimana = null;
        if (ricercaTavoliBean != null) {
            if (ricercaTavoliBean.verificaFiltroGiocoPresente()) {
                titoloGioco = ricercaTavoliBean.fornisciTitoloGioco();
            }

            if (ricercaTavoliBean.verificaFiltroGiornoPresente()) {
                giornoSettimana = ricercaTavoliBean.fornisciGiornoSettimana();
            }
        }

        List<SessioneTavolo> tavoli = sessioneTavoloDAO.cercaTavoliDisponibili(
                titoloGioco,
                giornoSettimana
        );

        List<InfoTavoloBean> tavoliBean = new ArrayList<>();
        for (SessioneTavolo tavolo : tavoli) {
            tavoliBean.add(creaInfoTavoloBean(tavolo));
        }

        return new ListaTavoliBean(tavoliBean);
    }

    public PreventivoBean calcolaPreventivo(RichiestaPreventivoBean richiestaPreventivoBean) {
        if (richiestaPreventivoBean == null || !richiestaPreventivoBean.verificaRichiestaValida()) {
            throw new IllegalArgumentException("Richiesta preventivo non valida.");
        }

        SessioneTavolo tavolo = sessioneTavoloDAO.recuperaTavolo(
                richiestaPreventivoBean.fornisciIdentificativoTavolo()
        );

        if (tavolo == null) {
            throw new IllegalArgumentException("Tavolo non trovato.");
        }

        if (tavolo.verificaPostiNonPrenotabili(richiestaPreventivoBean.fornisciNumeroPostiRichiesti())) {
            throw new IllegalArgumentException("Posti non disponibili per il tavolo selezionato.");
        }

        int postiRichiesti = richiestaPreventivoBean.fornisciNumeroPostiRichiesti();
        float importoTotale = tavolo.calcolaImportoPer(postiRichiesti);
        int puntiFedeltaPrevisti = RegolaPuntiFedelta.calcolaPuntiPer(importoTotale);

        return new PreventivoBean(
                tavolo.fornisciIdentificativo(),
                tavolo.fornisciTitoloGiocoAssociato(),
                tavolo.fornisciGiornoSettimana(),
                tavolo.fornisciFasciaOraria(),
                postiRichiesti,
                importoTotale,
                puntiFedeltaPrevisti
        );
    }

    public PrenotazioneBean registraPrenotazione(String idSessione, PagamentoBean pagamentoBean) {
        Session sessione = fornisciSessioneConRuolo(idSessione, RuoloUtente.CLIENTE);
        if (pagamentoBean == null
                || !pagamentoBean.verificaDatiPagamentoValidi()
                || !pagamentoBean.verificaPagamentoEffettuato()) {
            throw new IllegalArgumentException("Pagamento non valido.");
        }

        Utente utente = utenteDAO.recuperaUtente(sessione.fornisciUsername());
        if (!(utente instanceof Cliente cliente)) {
            throw new IllegalStateException("L'utente collegato alla sessione non e' un cliente.");
        }

        SessioneTavolo tavolo = sessioneTavoloDAO.recuperaTavolo(pagamentoBean.fornisciIdentificativoTavolo());
        if (tavolo == null) {
            throw new IllegalArgumentException("Tavolo non trovato.");
        }

        boolean postiPrenotati = sessioneTavoloDAO.prenotaPosti(
                pagamentoBean.fornisciIdentificativoTavolo(),
                pagamentoBean.fornisciNumeroPostiRichiesti()
        );
        if (!postiPrenotati) {
            throw new IllegalArgumentException("Posti non disponibili per il tavolo selezionato.");
        }

        int idPrenotazione = creaNuovoIdentificativoPrenotazione();
        Prenotazione prenotazione = new Prenotazione(
                idPrenotazione,
                cliente,
                tavolo,
                pagamentoBean.fornisciNumeroPostiRichiesti(),
                pagamentoBean.fornisciImporto()
        );

        prenotazioneDAO.salvaNuovaPrenotazione(prenotazione);
        notificaGestorePrenotazioneInAttesa(prenotazione);

        return creaPrenotazioneBean(prenotazione);
    }

    public ListaPrenotazioniBean fornisciPrenotazioniInAttesa(String idSessione) {
        fornisciSessioneConRuolo(idSessione, RuoloUtente.GESTORE);

        List<PrenotazioneBean> prenotazioniInAttesa = new ArrayList<>();
        for (Prenotazione prenotazione : prenotazioneDAO.recuperaPrenotazioniInAttesa()) {
            prenotazioniInAttesa.add(creaPrenotazioneBean(prenotazione));
        }

        return new ListaPrenotazioniBean(prenotazioniInAttesa);
    }

    public ListaPrenotazioniBean fornisciPrenotazioniCliente(String idSessione) {
        Session sessione = fornisciSessioneConRuolo(idSessione, RuoloUtente.CLIENTE);

        List<PrenotazioneBean> prenotazioniCliente = new ArrayList<>();
        for (Prenotazione prenotazione : prenotazioneDAO.recuperaPrenotazioniCliente(sessione.fornisciUsername())) {
            prenotazioniCliente.add(creaPrenotazioneBean(prenotazione));
        }

        return new ListaPrenotazioniBean(prenotazioniCliente);
    }

    public void confermaPrenotazione(String idSessione, int idPrenotazione) {
        fornisciSessioneConRuolo(idSessione, RuoloUtente.GESTORE);

        Prenotazione prenotazione = prenotazioneDAO.recuperaPrenotazione(idPrenotazione);
        if (prenotazione == null) {
            throw new IllegalArgumentException("Prenotazione non trovata.");
        }

        if (prenotazione.fornisciStatoCorrente() != StatoPrenotazione.IN_ATTESA) {
            throw new IllegalStateException("La prenotazione non e' in attesa di conferma.");
        }

        prenotazioneDAO.confermaPrenotazione(idPrenotazione);
        prenotazione.assegnaPuntiFedeltaAlCliente();
        utenteDAO.aggiornaPuntiFedelta(
                prenotazione.fornisciUsernameCliente(),
                prenotazione.fornisciPuntiFedeltaCliente()
        );
        notificaClientePrenotazioneConfermata(prenotazione);
    }

    public boolean verificaPrenotazioniInAttesa(String idSessione) {
        fornisciSessioneConRuolo(idSessione, RuoloUtente.GESTORE);
        return !prenotazioneDAO.recuperaPrenotazioniInAttesa().isEmpty();
    }

    private Session fornisciSessione(String idSessione) {
        Session sessione = sessionManager.fornisciSessione(idSessione);
        if (sessione == null) {
            throw new IllegalStateException("Sessione non valida.");
        }

        return sessione;
    }

    private Session fornisciSessioneConRuolo(String idSessione, RuoloUtente ruoloRichiesto) {
        Session sessione = fornisciSessione(idSessione);
        if (!sessione.verificaRuolo(ruoloRichiesto)) {
            throw new IllegalStateException("Sessione non valida per l'operazione richiesta.");
        }

        return sessione;
    }

    private int creaNuovoIdentificativoPrenotazione() {
        int idPrenotazione = 1;
        while (prenotazioneDAO.recuperaPrenotazione(idPrenotazione) != null) {
            idPrenotazione++;
        }

        return idPrenotazione;
    }

    private InfoTavoloBean creaInfoTavoloBean(SessioneTavolo tavolo) {
        return new InfoTavoloBean(
                tavolo.fornisciIdentificativo(),
                tavolo.fornisciTitoloGiocoAssociato(),
                tavolo.fornisciPercorsoImmagineGiocoAssociato(),
                new PostiTavoloBean(
                        tavolo.fornisciNumeroPostiTotali(),
                        tavolo.fornisciNumeroPostiDisponibili()
                ),
                tavolo.fornisciGiornoSettimana(),
                tavolo.fornisciFasciaOraria(),
                tavolo.fornisciQuotaPartecipazione()
        );
    }

    private PrenotazioneBean creaPrenotazioneBean(Prenotazione prenotazione) {
        return new PrenotazioneBean(
                prenotazione.fornisciIdentificativo(),
                prenotazione.fornisciUsernameCliente(),
                new TavoloPrenotatoBean(
                        prenotazione.fornisciTitoloGiocoPrenotato(),
                        prenotazione.fornisciGiornoAttivitaPrenotata(),
                        prenotazione.fornisciFasciaOrariaAttivitaPrenotata(),
                        prenotazione.fornisciNumeroPostiPrenotati(),
                        prenotazione.fornisciImportoTotale()
                ),
                prenotazione.fornisciDataPrenotazione().format(FORMATO_DATA),
                prenotazione.fornisciOraPrenotazione().format(FORMATO_ORA),
                prenotazione.fornisciStatoCorrente()
        );
    }

    private void notificaGestorePrenotazioneInAttesa(Prenotazione prenotazione) {
        servizioNotifichePrenotazione.inviaNotificaAlGestore(new NotificaPrenotazioneBean(
                prenotazione.fornisciIdentificativo(),
                DESTINATARIO_GESTORE,
                "Nuova richiesta di prenotazione da "
                        + prenotazione.fornisciUsernameCliente()
                        + " per "
                        + prenotazione.fornisciTitoloGiocoPrenotato()
                        + "."
        ));
    }

    private void notificaClientePrenotazioneConfermata(Prenotazione prenotazione) {
        servizioNotifichePrenotazione.inviaNotificaAlCliente(new NotificaPrenotazioneBean(
                prenotazione.fornisciIdentificativo(),
                prenotazione.fornisciUsernameCliente(),
                "La tua prenotazione per "
                        + prenotazione.fornisciTitoloGiocoPrenotato()
                        + " e' stata confermata."
        ));
    }
}
