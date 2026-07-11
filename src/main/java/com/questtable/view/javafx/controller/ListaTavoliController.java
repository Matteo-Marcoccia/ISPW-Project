package com.questtable.view.javafx.controller;

import com.questtable.bean.InfoTavoloBean;
import com.questtable.bean.ListaTavoliBean;
import com.questtable.bean.RicercaTavoliBean;
import com.questtable.controller.QuestTableController;
import com.questtable.model.GiornoSettimana;
import com.questtable.view.FormattatoreImporti;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.StringConverter;

import java.io.IOException;
import java.net.URL;

public class ListaTavoliController {
    private static final String FONT_PRINCIPALE = "Segoe UI";
    private static final String STILE_TESTO_DETTAGLIO_CARD = "-fx-text-fill: #4A4A4A;";

    private final QuestTableController questTableController = new QuestTableController();

    private String idSessione;

    @FXML
    private FlowPane containerTavoli;

    @FXML
    private TextField txtFiltroGioco;

    @FXML
    private ComboBox<GiornoSettimana> cmbFiltroGiorno;

    @FXML
    public void initialize() {
        containerTavoli.getChildren().clear();
        inizializzaFiltroGiorni();
    }

    public void inizializzaSessione(String idSessione) {
        this.idSessione = idSessione;
        caricaTavoliDisponibili();
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
    void onRichiediTavoloClick() {
        MessaggiGrafici.mostraFunzioneNonDisponibile("Richiesta tavolo non disponibile");
    }

    @FXML
    void onCercaTavoliClick() {
        RicercaTavoliBean ricercaTavoliBean = new RicercaTavoliBean(
                txtFiltroGioco.getText(),
                cmbFiltroGiorno.getValue()
        );
        caricaTavoliDisponibili(ricercaTavoliBean);
    }

    @FXML
    void onPulisciFiltriClick() {
        txtFiltroGioco.clear();
        cmbFiltroGiorno.getSelectionModel().clearSelection();
        caricaTavoliDisponibili();
    }

    private void inizializzaFiltroGiorni() {
        cmbFiltroGiorno.setItems(FXCollections.observableArrayList(GiornoSettimana.values()));
        cmbFiltroGiorno.setConverter(new StringConverter<>() {
            @Override
            public String toString(GiornoSettimana giornoSettimana) {
                if (giornoSettimana == null) {
                    return "";
                }
                return giornoSettimana.fornisciNomeVisualizzato();
            }

            @Override
            public GiornoSettimana fromString(String string) {
                return null;
            }
        });
    }

    private void caricaTavoliDisponibili() {
        caricaTavoliDisponibili(null);
    }

    private void caricaTavoliDisponibili(RicercaTavoliBean ricercaTavoliBean) {
        containerTavoli.getChildren().clear();

        try {
            ListaTavoliBean listaTavoliBean = questTableController.fornisciTavoliDisponibili(
                    idSessione,
                    ricercaTavoliBean
            );
            if (!listaTavoliBean.verificaPresenzaTavoli()) {
                containerTavoli.getChildren().add(creaMessaggioListaVuota());
                return;
            }

            for (InfoTavoloBean tavolo : listaTavoliBean.fornisciTavoli()) {
                containerTavoli.getChildren().add(creaCardTavolo(tavolo));
            }
        } catch (IllegalArgumentException | IllegalStateException exception) {
            mostraErrore(exception.getMessage());
        }
    }

    private VBox creaCardTavolo(InfoTavoloBean tavolo) {
        StackPane boxImmagine = creaBoxImmagine(tavolo);

        Label lblTitolo = new Label(tavolo.fornisciTitoloGioco());
        lblTitolo.setFont(Font.font(FONT_PRINCIPALE, FontWeight.BOLD, 18));
        lblTitolo.setStyle("-fx-text-fill: #1A1A1A;");
        lblTitolo.setWrapText(true);

        Label lblGiorno = new Label(tavolo.fornisciGiornoSettimana().fornisciNomeVisualizzato()
                + " | " + tavolo.fornisciFasciaOraria());
        lblGiorno.setFont(Font.font(FONT_PRINCIPALE, 13));
        lblGiorno.setStyle(STILE_TESTO_DETTAGLIO_CARD);

        Label lblPosti = new Label("Posti disponibili: "
                + tavolo.fornisciNumeroPostiDisponibili()
                + "/" + tavolo.fornisciNumeroPostiTotali());
        lblPosti.setFont(Font.font(FONT_PRINCIPALE, 13));
        lblPosti.setStyle(STILE_TESTO_DETTAGLIO_CARD);

        Label lblQuota = new Label(FormattatoreImporti.formattaQuotaAPersona(tavolo.fornisciQuotaPartecipazione()));
        lblQuota.setFont(Font.font(FONT_PRINCIPALE, 13));
        lblQuota.setStyle(STILE_TESTO_DETTAGLIO_CARD);

        Button btnSeleziona = new Button("Seleziona");
        btnSeleziona.setMaxWidth(Double.MAX_VALUE);
        btnSeleziona.setStyle("-fx-background-color: #6200EE; -fx-text-fill: white; "
                + "-fx-background-radius: 18; -fx-cursor: hand; -fx-padding: 8 14 8 14;");
        btnSeleziona.setOnAction(event -> apriDettaglioTavolo(event, tavolo));

        VBox card = new VBox(10, boxImmagine, lblTitolo, lblGiorno, lblPosti, lblQuota, btnSeleziona);
        card.setPrefWidth(240);
        card.setMinHeight(320);
        card.setAlignment(Pos.TOP_LEFT);
        card.setPadding(new Insets(18));
        card.setStyle("-fx-background-color: white; -fx-background-radius: 14; "
                + "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.16), 10, 0, 0, 4);");

        return card;
    }

    private StackPane creaBoxImmagine(InfoTavoloBean tavolo) {
        StackPane boxImmagine = new StackPane();
        boxImmagine.setPrefSize(204, 124);
        boxImmagine.setMinSize(204, 124);
        boxImmagine.setMaxSize(204, 124);
        boxImmagine.setAlignment(Pos.CENTER);
        boxImmagine.setStyle("-fx-background-color: #F2F2F2; -fx-background-radius: 10;");

        URL urlImmagine = getClass().getResource(tavolo.fornisciPercorsoImmagine());
        if (urlImmagine == null) {
            Label lblImmagineNonDisponibile = new Label("Immagine non disponibile");
            lblImmagineNonDisponibile.setFont(Font.font(FONT_PRINCIPALE, 11));
            lblImmagineNonDisponibile.setStyle("-fx-text-fill: #777777; -fx-font-size: 11px;");
            boxImmagine.getChildren().add(lblImmagineNonDisponibile);
            return boxImmagine;
        }

        ImageView immagineGioco = new ImageView(new Image(urlImmagine.toExternalForm()));
        immagineGioco.setFitWidth(204);
        immagineGioco.setFitHeight(124);
        immagineGioco.setPreserveRatio(true);
        immagineGioco.setSmooth(true);

        boxImmagine.getChildren().add(immagineGioco);
        return boxImmagine;
    }

    private Label creaMessaggioListaVuota() {
        Label label = new Label("Non ci sono tavoli disponibili.");
        label.setFont(Font.font(FONT_PRINCIPALE, FontWeight.BOLD, 15));
        label.setStyle("-fx-text-fill: white;");
        return label;
    }

    private void apriDettaglioTavolo(ActionEvent event, InfoTavoloBean tavolo) {
        try {
            FXMLLoader loader = NavigazioneGrafica.creaLoader(getClass(), "/com/questtable/view/DettagliTavoloView.fxml");
            Parent root = loader.load();

            DettagliTavoloController dettagliTavoloController = loader.getController();
            dettagliTavoloController.inizializzaDettaglio(idSessione, tavolo);

            NavigazioneGrafica.aggiornaScena(event, root);
        } catch (IOException exception) {
            mostraErrore(exception.getMessage());
        }
    }

    private void mostraErrore(String messaggio) {
        MessaggiGrafici.mostraErrore("Impossibile caricare i tavoli", messaggio);
    }
}

