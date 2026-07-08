package com.questtable.view.javafx.controller;

import com.questtable.bean.ProfiloUtenteBean;
import com.questtable.controller.QuestTableController;
import com.questtable.model.RuoloUtente;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class SchermataHomeController {
    private final QuestTableController questTableController = new QuestTableController();

    private String idSessione;

    @FXML
    private Button btnAccedi;

    @FXML
    private Button btnProfiloUtente;


    @FXML
    private VBox boxPrenota;

    @FXML
    private VBox boxShop;

    @FXML
    private VBox boxGestore;

    @FXML
    private VBox boxDescrizione;

    @FXML
    private Button btnStoricoPrenotazioni;

    @FXML
    public void initialize() {
        btnProfiloUtente.setVisible(false);
        btnProfiloUtente.setManaged(false);
        boxGestore.setVisible(false);
        boxGestore.setManaged(false);
        btnStoricoPrenotazioni.setVisible(false);
        btnStoricoPrenotazioni.setManaged(false);
    }

    public void inizializzaSessione(String idSessione) {
        ProfiloUtenteBean profiloUtente = questTableController.fornisciProfiloUtente(idSessione);
        inizializzaSessione(idSessione, profiloUtente);
    }

    private void inizializzaSessione(String idSessione, ProfiloUtenteBean profiloUtente) {
        this.idSessione = idSessione;

        btnAccedi.setVisible(false);
        btnAccedi.setManaged(false);
        btnProfiloUtente.setVisible(true);
        btnProfiloUtente.setManaged(true);

        if (profiloUtente.verificaRuolo(RuoloUtente.CLIENTE)) {
            btnProfiloUtente.setText("");
            btnProfiloUtente.setGraphic(creaContenutoProfiloCliente(profiloUtente));
            boxDescrizione.setStyle("-fx-background-color: rgba(0, 150, 136, 0.22); -fx-background-radius: 16; "
                    + "-fx-border-color: rgba(178, 255, 244, 0.45); -fx-border-radius: 16; -fx-border-width: 1;");
            btnStoricoPrenotazioni.setVisible(true);
            btnStoricoPrenotazioni.setManaged(true);
            return;
        }

        btnProfiloUtente.setGraphic(null);
        btnProfiloUtente.setText(profiloUtente.fornisciUsername() + "  |  Gestore");
        boxPrenota.setVisible(false);
        boxPrenota.setManaged(false);
        boxShop.setVisible(false);
        boxShop.setManaged(false);
        boxGestore.setVisible(true);
        boxGestore.setManaged(true);
    }

    @FXML
    void onAccediClick(ActionEvent event) throws IOException {
        apriLogin(event, DestinazioneDopoLogin.HOME);
    }

    @FXML
    void onPrenotaOraClick(ActionEvent event) throws IOException {
        if (idSessione == null) {
            apriLogin(event, DestinazioneDopoLogin.LISTA_TAVOLI);
            return;
        }

        apriListaTavoli(event);
    }

    @FXML
    void onVaiAlloShopClick() {
        MessaggiGrafici.mostraFunzioneNonDisponibile("Acquisto giochi non disponibile");
    }

    @FXML
    void onProfiloUtenteClick() {
        boolean logoutConfermato = MessaggiGrafici.richiediConfermaLogout();
        if (!logoutConfermato) {
            return;
        }

        questTableController.effettuaLogout(idSessione);
        idSessione = null;

        btnAccedi.setVisible(true);
        btnAccedi.setManaged(true);
        btnProfiloUtente.setVisible(false);
        btnProfiloUtente.setManaged(false);
        btnProfiloUtente.setGraphic(null);

        boxPrenota.setVisible(true);
        boxPrenota.setManaged(true);
        boxShop.setVisible(true);
        boxShop.setManaged(true);
        boxGestore.setVisible(false);
        boxGestore.setManaged(false);
        btnStoricoPrenotazioni.setVisible(false);
        btnStoricoPrenotazioni.setManaged(false);
        boxDescrizione.setStyle("-fx-background-color: rgba(255, 255, 255, 0.08); -fx-background-radius: 16; "
                + "-fx-border-color: rgba(255, 255, 255, 0.1); -fx-border-radius: 16; -fx-border-width: 1;");
    }

    @FXML
    void onConfermaPrenotazioniClick(ActionEvent event) throws IOException {
        FXMLLoader loader = NavigazioneGrafica.creaLoader(
                getClass(),
                "/com/questtable/view/ConfermaPrenotazioniView.fxml"
        );
        Parent root = loader.load();

        ConfermaPrenotazioniController confermaPrenotazioniController = loader.getController();
        confermaPrenotazioniController.inizializzaSessione(idSessione);

        NavigazioneGrafica.aggiornaScena(event, root);
    }

    @FXML
    void onStoricoPrenotazioniClick(ActionEvent event) throws IOException {
        FXMLLoader loader = NavigazioneGrafica.creaLoader(getClass(), "/com/questtable/view/StoricoPrenotazioniView.fxml");
        Parent root = loader.load();

        StoricoPrenotazioniController storicoPrenotazioniController = loader.getController();
        storicoPrenotazioniController.inizializzaSessione(idSessione);

        NavigazioneGrafica.aggiornaScena(event, root);
    }

    private void apriLogin(ActionEvent event, DestinazioneDopoLogin destinazioneDopoLogin) throws IOException {
        FXMLLoader loader = NavigazioneGrafica.creaLoader(getClass(), "/com/questtable/view/LoginView.fxml");
        Parent root = loader.load();

        LoginController loginController = loader.getController();
        loginController.inizializzaDestinazione(destinazioneDopoLogin);

        NavigazioneGrafica.aggiornaScena(event, root);
    }

    private void apriListaTavoli(ActionEvent event) throws IOException {
        FXMLLoader loader = NavigazioneGrafica.creaLoader(getClass(), "/com/questtable/view/ListaTavoliView.fxml");
        Parent root = loader.load();

        ListaTavoliController listaTavoliController = loader.getController();
        listaTavoliController.inizializzaSessione(idSessione);

        NavigazioneGrafica.aggiornaScena(event, root);
    }

    private HBox creaContenutoProfiloCliente(ProfiloUtenteBean profiloUtente) {
        Label username = creaEtichettaProfilo(profiloUtente.fornisciUsername() + "  |");
        Label stella = new Label("\u2605");
        stella.setStyle("-fx-text-fill: #FFD43B; -fx-font-size: 18px; -fx-font-weight: bold;");
        Label punti = creaEtichettaProfilo(profiloUtente.fornisciPuntiFedelta() + " punti");

        HBox contenutoProfilo = new HBox(6, username, stella, punti);
        contenutoProfilo.setAlignment(Pos.CENTER);
        return contenutoProfilo;
    }

    private Label creaEtichettaProfilo(String testo) {
        Label etichetta = new Label(testo);
        etichetta.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");
        return etichetta;
    }
}
