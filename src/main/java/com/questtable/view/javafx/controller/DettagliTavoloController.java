package com.questtable.view.javafx.controller;

import com.questtable.bean.InfoTavoloBean;
import com.questtable.bean.PreventivoBean;
import com.questtable.bean.RichiestaPreventivoBean;
import com.questtable.controller.QuestTableController;
import com.questtable.view.FormattatoreImporti;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.net.URL;

public class DettagliTavoloController {
    private static final String STILE_TESTO_RIEPILOGO = "-fx-text-fill: #1A1A1A; -fx-font-weight: bold;";

    private final QuestTableController questTableController = new QuestTableController();

    private String idSessione;
    private InfoTavoloBean tavoloSelezionato;
    private int numeroPartecipanti = 1;

    @FXML
    private ImageView imgGioco;

    @FXML
    private Label lblNomeTavoloStatico;

    @FXML
    private Label lblGiornoStatico;

    @FXML
    private Label lblFasciaOrariaStatica;

    @FXML
    private Button btnDiminuisciPartecipanti;

    @FXML
    private Label lblNumeroPartecipanti;

    @FXML
    private Button btnAumentaPartecipanti;

    @FXML
    private Label lblRiepilogoTavolo;

    @FXML
    private Label lblRiepilogoPartecipanti;

    @FXML
    private Label lblRiepilogoPrezzoUnitario;

    @FXML
    private Label lblRiepilogoPuntiFedelta;

    @FXML
    private Label lblRiepilogoCostoTotale;

    public void inizializzaDettaglio(String idSessione, InfoTavoloBean tavoloSelezionato) {
        inizializzaDettaglio(idSessione, tavoloSelezionato, 1);
    }

    public void inizializzaDettaglio(String idSessione, InfoTavoloBean tavoloSelezionato, int numeroPartecipanti) {
        this.idSessione = idSessione;
        this.tavoloSelezionato = tavoloSelezionato;
        this.numeroPartecipanti = numeroPartecipanti;

        lblNomeTavoloStatico.setText("Tavolo per " + tavoloSelezionato.fornisciTitoloGioco());
        lblGiornoStatico.setText(tavoloSelezionato.fornisciGiornoSettimana().fornisciNomeVisualizzato());
        lblFasciaOrariaStatica.setText(tavoloSelezionato.fornisciFasciaOraria());
        lblRiepilogoTavolo.setText(tavoloSelezionato.fornisciTitoloGioco());
        lblRiepilogoPrezzoUnitario.setText(FormattatoreImporti.formattaImporto(tavoloSelezionato.fornisciQuotaPartecipazione()));
        mostraImmagineGioco();
        applicaColoriTesti();

        aggiornaRiepilogo();
    }

    @FXML
    void onAumentaPartecipantiClick() {
        if (tavoloSelezionato == null
                || numeroPartecipanti >= tavoloSelezionato.fornisciNumeroPostiDisponibili()) {
            return;
        }

        numeroPartecipanti++;
        aggiornaRiepilogo();
    }

    @FXML
    void onDiminuisciPartecipantiClick() {
        if (numeroPartecipanti <= 1) {
            return;
        }

        numeroPartecipanti--;
        aggiornaRiepilogo();
    }

    @FXML
    void onConfermaPrenotazioneClick(ActionEvent event) {
        try {
            RichiestaPreventivoBean richiestaPreventivoBean = new RichiestaPreventivoBean(
                    tavoloSelezionato.fornisciIdentificativoTavolo(),
                    numeroPartecipanti
            );
            PreventivoBean preventivoBean = questTableController.calcolaPreventivo(richiestaPreventivoBean);
            apriPagamento(event, preventivoBean);
        } catch (IllegalArgumentException | IllegalStateException exception) {
            mostraErrore(exception.getMessage());
        }
    }

    @FXML
    void onTornaListaClick(ActionEvent event) throws IOException {
        FXMLLoader loader = NavigazioneGrafica.creaLoader(getClass(), "/com/questtable/view/ListaTavoliView.fxml");
        Parent root = loader.load();

        ListaTavoliController listaTavoliController = loader.getController();
        listaTavoliController.inizializzaSessione(idSessione);

        NavigazioneGrafica.aggiornaScena(event, root);
    }

    private void aggiornaRiepilogo() {
        float costoTotale = tavoloSelezionato.fornisciQuotaPartecipazione() * numeroPartecipanti;
        int puntiFedeltaPrevisti = Math.round(costoTotale * 10);

        lblNumeroPartecipanti.setText(String.valueOf(numeroPartecipanti));
        lblRiepilogoPartecipanti.setText(String.valueOf(numeroPartecipanti));
        lblRiepilogoPuntiFedelta.setText("+" + puntiFedeltaPrevisti + " punti");
        lblRiepilogoCostoTotale.setText(FormattatoreImporti.formattaImporto(costoTotale));

        btnDiminuisciPartecipanti.setDisable(numeroPartecipanti <= 1);
        btnAumentaPartecipanti.setDisable(
                numeroPartecipanti >= tavoloSelezionato.fornisciNumeroPostiDisponibili()
        );
    }

    private void applicaColoriTesti() {
        lblNomeTavoloStatico.setStyle("-fx-text-fill: #666666;");
        lblGiornoStatico.setStyle("-fx-background-color: #F4F4F6; -fx-background-radius: 6; "
                + "-fx-padding: 9 10 9 10; -fx-text-fill: #1A1A1A;");
        lblFasciaOrariaStatica.setStyle("-fx-background-color: #F4F4F6; -fx-background-radius: 6; "
                + "-fx-padding: 9 10 9 10; -fx-text-fill: #1A1A1A;");
        lblRiepilogoTavolo.setStyle(STILE_TESTO_RIEPILOGO);
        lblRiepilogoPartecipanti.setStyle(STILE_TESTO_RIEPILOGO);
        lblRiepilogoPrezzoUnitario.setStyle(STILE_TESTO_RIEPILOGO);
        lblRiepilogoPuntiFedelta.setStyle("-fx-text-fill: #4CAF50; -fx-font-weight: bold;");
        lblRiepilogoCostoTotale.setStyle("-fx-text-fill: #6200EE; -fx-font-weight: bold;");
    }

    private void mostraImmagineGioco() {
        URL urlImmagine = getClass().getResource(tavoloSelezionato.fornisciPercorsoImmagine());
        if (urlImmagine == null) {
            imgGioco.setVisible(false);
            imgGioco.setManaged(false);
            return;
        }

        imgGioco.setImage(new Image(urlImmagine.toExternalForm()));
        imgGioco.setVisible(true);
        imgGioco.setManaged(true);
    }

    private void apriPagamento(ActionEvent event, PreventivoBean preventivoBean) {
        try {
            FXMLLoader loader = NavigazioneGrafica.creaLoader(getClass(), "/com/questtable/view/PagamentoView.fxml");
            Parent root = loader.load();

            PagamentoController pagamentoController = loader.getController();
            pagamentoController.inizializzaPagamento(idSessione, preventivoBean, tavoloSelezionato);

            NavigazioneGrafica.aggiornaScena(event, root);
        } catch (IOException exception) {
            mostraErrore(exception.getMessage());
        }
    }

    private void mostraErrore(String messaggio) {
        MessaggiGrafici.mostraErrore("Impossibile calcolare il preventivo", messaggio);
    }
}

