package com.questtable.view.javafx.controller;

import com.questtable.bean.PrenotazioneBean;
import com.questtable.view.FormattatoreImporti;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;

import java.io.IOException;

public class RichiestaPrenotazioneOkController {
    private String idSessione;

    @FXML
    private Label lblCodicePrenotazione;

    @FXML
    private Label lblGioco;

    @FXML
    private Label lblTavoloPrenotato;

    @FXML
    private Label lblDataRichiesta;

    @FXML
    private Label lblOraRichiesta;

    @FXML
    private Label lblPartecipanti;

    @FXML
    private Label lblImportoPagato;

    public void inizializzaRichiestaInviata(String idSessione, PrenotazioneBean prenotazioneBean) {
        this.idSessione = idSessione;

        lblCodicePrenotazione.setText("#QT" + prenotazioneBean.fornisciIdentificativoPrenotazione());
        lblGioco.setText(prenotazioneBean.fornisciTitoloGioco());
        lblTavoloPrenotato.setText(prenotazioneBean.fornisciGiornoAttivita().fornisciNomeVisualizzato()
                + " | " + prenotazioneBean.fornisciFasciaOrariaAttivita());
        lblDataRichiesta.setText(prenotazioneBean.fornisciDataPrenotazione());
        lblOraRichiesta.setText(prenotazioneBean.fornisciOraPrenotazione());
        lblPartecipanti.setText(String.valueOf(prenotazioneBean.fornisciNumeroPostiPrenotati()));
        lblImportoPagato.setText(FormattatoreImporti.formattaImporto(prenotazioneBean.fornisciImportoTotale()));
    }

    @FXML
    void onTornaHomeClick(ActionEvent event) throws IOException {
        FXMLLoader loader = NavigazioneGrafica.creaLoader(getClass(), "/com/questtable/view/SchermataHomeView.fxml");
        Parent root = loader.load();

        SchermataHomeController schermataHomeController = loader.getController();
        schermataHomeController.inizializzaSessione(idSessione);

        NavigazioneGrafica.aggiornaScena(event, root);
    }

    @FXML
    void onPrenotaAltroTavoloClick(ActionEvent event) throws IOException {
        FXMLLoader loader = NavigazioneGrafica.creaLoader(getClass(), "/com/questtable/view/ListaTavoliView.fxml");
        Parent root = loader.load();

        ListaTavoliController listaTavoliController = loader.getController();
        listaTavoliController.inizializzaSessione(idSessione);

        NavigazioneGrafica.aggiornaScena(event, root);
    }
}

