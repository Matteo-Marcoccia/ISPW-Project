package com.questtable.view.javafx.controller;

import com.questtable.bean.ListaPrenotazioniBean;
import com.questtable.bean.PrenotazioneBean;
import com.questtable.controller.QuestTableController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class ConfermaPrenotazioniController {
    private final QuestTableController questTableController = new QuestTableController();

    private String idSessione;

    @FXML
    private VBox containerPrenotazioni;

    @FXML
    private Label lblMessaggioGestore;

    public void inizializzaSessione(String idSessione) {
        this.idSessione = idSessione;
        caricaPrenotazioniInAttesa();
    }

    @FXML
    void onTornaHomeClick(ActionEvent event) throws IOException {
        FXMLLoader loader = NavigazioneGrafica.creaLoader(getClass(), "/com/questtable/view/SchermataHomeView.fxml");
        Parent root = loader.load();

        SchermataHomeController schermataHomeController = loader.getController();
        schermataHomeController.inizializzaSessione(idSessione);

        NavigazioneGrafica.aggiornaScena(event, root);
    }

    private void caricaPrenotazioniInAttesa() {
        containerPrenotazioni.getChildren().clear();

        try {
            ListaPrenotazioniBean listaPrenotazioniBean =
                    questTableController.fornisciPrenotazioniInAttesa(idSessione);

            if (listaPrenotazioniBean.verificaAssenzaPrenotazioni()) {
                containerPrenotazioni.getChildren().add(creaMessaggioListaVuota());
                return;
            }

            for (PrenotazioneBean prenotazione : listaPrenotazioniBean.fornisciPrenotazioni()) {
                containerPrenotazioni.getChildren().add(creaCardPrenotazione(prenotazione));
            }
        } catch (IllegalArgumentException | IllegalStateException exception) {
            mostraErrore(exception.getMessage());
        }
    }

    private VBox creaCardPrenotazione(PrenotazioneBean prenotazione) {
        Label lblCodice = ComponentiPrenotazioneGrafici.creaCodicePrenotazione(
                prenotazione.fornisciIdentificativoPrenotazione()
        );
        Label lblGioco = ComponentiPrenotazioneGrafici.creaTitoloGioco(prenotazione.fornisciTitoloGioco());

        HBox rigaTestata = new HBox(10, lblGioco, ComponentiPrenotazioneGrafici.creaSpaziatore(), lblCodice);
        rigaTestata.setAlignment(Pos.CENTER_LEFT);

        Label lblCliente = ComponentiPrenotazioneGrafici.creaRigaInformazione(
                "Cliente",
                prenotazione.fornisciUsernameCliente()
        );
        Label lblAttivita = ComponentiPrenotazioneGrafici.creaRigaInformazione(
                "Tavolo prenotato",
                prenotazione.fornisciGiornoAttivita().fornisciNomeVisualizzato()
                        + " | " + prenotazione.fornisciFasciaOrariaAttivita()
        );
        Label lblDataRichiesta = ComponentiPrenotazioneGrafici.creaRigaInformazione(
                "Richiesta ricevuta",
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

        Button btnConferma = new Button("Conferma");
        btnConferma.setStyle("-fx-background-color: #12A866; -fx-text-fill: white; "
                + "-fx-background-radius: 18; -fx-cursor: hand; -fx-padding: 8 18 8 18;");
        btnConferma.setOnAction(event -> confermaPrenotazione(prenotazione));

        HBox rigaAzione = new HBox(ComponentiPrenotazioneGrafici.creaSpaziatore(), btnConferma);
        rigaAzione.setAlignment(Pos.CENTER_RIGHT);

        VBox card = new VBox(9, rigaTestata, lblCliente, lblAttivita, lblDataRichiesta, lblPosti, lblImporto,
                rigaAzione);
        card.setPadding(new Insets(18));
        ComponentiPrenotazioneGrafici.applicaStileCard(card);

        return card;
    }

    private Label creaMessaggioListaVuota() {
        return ComponentiPrenotazioneGrafici.creaMessaggioListaVuota("Non ci sono prenotazioni in attesa.");
    }

    private void confermaPrenotazione(PrenotazioneBean prenotazione) {
        try {
            questTableController.confermaPrenotazione(
                    idSessione,
                    prenotazione.fornisciIdentificativoPrenotazione()
            );
            lblMessaggioGestore.setText("Prenotazione #QT"
                    + prenotazione.fornisciIdentificativoPrenotazione()
                    + " confermata con successo.");
            caricaPrenotazioniInAttesa();
        } catch (IllegalArgumentException | IllegalStateException exception) {
            mostraErrore(exception.getMessage());
        }
    }

    private void mostraErrore(String messaggio) {
        MessaggiGrafici.mostraErrore("Impossibile confermare la prenotazione", messaggio);
    }
}

