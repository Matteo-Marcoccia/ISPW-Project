package com.questtable.view.javafx.controller;

import com.questtable.bean.LoginBean;
import com.questtable.bean.ProfiloUtenteBean;
import com.questtable.controller.LoginControllerApplicativo;
import com.questtable.model.RuoloUtente;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;

public class LoginController {
    private final LoginControllerApplicativo loginControllerApplicativo = new LoginControllerApplicativo();
    private DestinazioneDopoLogin destinazioneDopoLogin = DestinazioneDopoLogin.HOME;

    @FXML
    private TextField txtUsername;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private Button btnAccediInForm;

    @FXML
    private Label lblMessaggioLogin;

    public void inizializzaDestinazione(DestinazioneDopoLogin destinazioneDopoLogin) {
        this.destinazioneDopoLogin = destinazioneDopoLogin;
    }

    @FXML
    void onLoginSubmit() throws IOException {
        LoginBean loginBean = new LoginBean(txtUsername.getText(), txtPassword.getText());

        try {
            String idSessione = loginControllerApplicativo.effettuaLogin(loginBean);
            ProfiloUtenteBean profiloUtente = loginControllerApplicativo.fornisciProfiloUtente(idSessione);
            apriSchermataSuccessiva(idSessione, profiloUtente);
        } catch (IllegalArgumentException | IllegalStateException exception) {
            mostraMessaggio(exception.getMessage());
        }
    }

    @FXML
    void onTornaHomeClick(ActionEvent event) throws IOException {
        tornaAllaHome(event);
    }

    private void apriSchermataSuccessiva(String idSessione, ProfiloUtenteBean profiloUtente) throws IOException {
        if (profiloUtente.verificaRuolo(RuoloUtente.CLIENTE)
                && destinazioneDopoLogin == DestinazioneDopoLogin.LISTA_TAVOLI) {
            apriListaTavoli(idSessione);
            return;
        }

        apriHomeConProfilo(idSessione);
    }

    private void apriListaTavoli(String idSessione) throws IOException {
        FXMLLoader loader = NavigazioneGrafica.creaLoader(getClass(), "/com/questtable/view/ListaTavoliView.fxml");
        Parent root = loader.load();

        ListaTavoliController listaTavoliController = loader.getController();
        listaTavoliController.inizializzaSessione(idSessione);

        aggiornaScena(root);
    }

    private void apriHomeConProfilo(String idSessione) throws IOException {
        FXMLLoader loader = NavigazioneGrafica.creaLoader(getClass(), "/com/questtable/view/SchermataHomeView.fxml");
        Parent root = loader.load();

        SchermataHomeController schermataHomeController = loader.getController();
        schermataHomeController.inizializzaSessione(idSessione);

        aggiornaScena(root);
    }

    private void tornaAllaHome(ActionEvent event) throws IOException {
        FXMLLoader loader = NavigazioneGrafica.creaLoader(getClass(), "/com/questtable/view/SchermataHomeView.fxml");
        Parent root = loader.load();
        NavigazioneGrafica.aggiornaScena(event, root);
    }

    private void aggiornaScena(Parent root) {
        NavigazioneGrafica.aggiornaScena(btnAccediInForm, root);
    }

    private void mostraMessaggio(String messaggio) {
        lblMessaggioLogin.setText(messaggio);
    }

}
