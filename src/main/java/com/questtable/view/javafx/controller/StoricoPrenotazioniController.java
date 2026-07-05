package com.questtable.view.javafx.controller;

import com.questtable.bean.ListaPrenotazioniBean;
import com.questtable.bean.PrenotazioneBean;
import com.questtable.controller.QuestTableController;
import com.questtable.model.StatoPrenotazione;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class StoricoPrenotazioniController {
    private final QuestTableController questTableController = new QuestTableController();

    private String idSessione;

    @FXML
    private VBox containerPrenotazioni;

    public void inizializzaSessione(String idSessione) {
        this.idSessione = idSessione;
        caricaStoricoPrenotazioni();
    }

    @FXML
    void onTornaHomeClick(ActionEvent event) throws IOException {
        FXMLLoader loader = NavigazioneGrafica.creaLoader(getClass(), "/com/questtable/view/SchermataHomeView.fxml");
        Parent root = loader.load();

        SchermataHomeController schermataHomeController = loader.getController();
        schermataHomeController.inizializzaSessione(idSessione);

        NavigazioneGrafica.aggiornaScena(event, root);
    }

    private void caricaStoricoPrenotazioni() {
        containerPrenotazioni.getChildren().clear();

        try {
            ListaPrenotazioniBean listaPrenotazioniBean =
                    questTableController.fornisciPrenotazioniCliente(idSessione);

            if (listaPrenotazioniBean.verificaAssenzaPrenotazioni()) {
                containerPrenotazioni.getChildren().add(creaMessaggioListaVuota());
                return;
            }

            for (PrenotazioneBean prenotazione : listaPrenotazioniBean.fornisciPrenotazioni()) {
                containerPrenotazioni.getChildren().add(creaCardPrenotazione(prenotazione));
            }
        } catch (IllegalArgumentException | IllegalStateException exception) {
            MessaggiGrafici.mostraErrore("Impossibile caricare lo storico", exception.getMessage());
        }
    }

    private VBox creaCardPrenotazione(PrenotazioneBean prenotazione) {
        Label lblCodice = ComponentiPrenotazioneGrafici.creaCodicePrenotazione(
                prenotazione.fornisciIdentificativoPrenotazione()
        );
        Label lblGioco = ComponentiPrenotazioneGrafici.creaTitoloGioco(prenotazione.fornisciTitoloGioco());

        Label lblStato = new Label(prenotazione.fornisciStatoPrenotazione().fornisciNomeVisualizzato());
        lblStato.setStyle("-fx-background-color: " + scegliColoreStato(prenotazione.fornisciStatoPrenotazione())
                + "; -fx-text-fill: white; -fx-background-radius: 14; -fx-padding: 5 12 5 12; "
                + "-fx-font-weight: bold;");

        HBox rigaTestata = new HBox(
                10,
                lblGioco,
                ComponentiPrenotazioneGrafici.creaSpaziatore(),
                lblCodice,
                lblStato
        );
        rigaTestata.setAlignment(Pos.CENTER_LEFT);

        Label lblAttivita = ComponentiPrenotazioneGrafici.creaRigaInformazione(
                "Tavolo prenotato",
                prenotazione.fornisciGiornoAttivita().fornisciNomeVisualizzato()
                        + " | " + prenotazione.fornisciFasciaOrariaAttivita()
        );
        Label lblDataRichiesta = ComponentiPrenotazioneGrafici.creaRigaInformazione(
                "Richiesta inviata",
                prenotazione.fornisciDataPrenotazione() + " alle " + prenotazione.fornisciOraPrenotazione()
        );
        Label lblPosti = ComponentiPrenotazioneGrafici.creaRigaInformazione(
                "Posti",
                String.valueOf(prenotazione.fornisciNumeroPostiPrenotati())
        );
        Label lblImporto = ComponentiPrenotazioneGrafici.creaRigaInformazione(
                "Importo pagato",
                FormattatoreImporti.formattaImporto(prenotazione.fornisciImportoTotale())
        );

        VBox card = new VBox(9, rigaTestata, lblAttivita, lblDataRichiesta, lblPosti, lblImporto);
        card.setPadding(new Insets(18));
        ComponentiPrenotazioneGrafici.applicaStileCard(card);

        return card;
    }

    private Label creaMessaggioListaVuota() {
        return ComponentiPrenotazioneGrafici.creaMessaggioListaVuota("Non hai ancora prenotazioni.");
    }

    private String scegliColoreStato(StatoPrenotazione statoPrenotazione) {
        if (statoPrenotazione == StatoPrenotazione.CONFERMATA) {
            return "#12A866";
        }
        return "#B24A3B";
    }
}
