package com.questtable.view.javafx.controller;

import com.questtable.bean.InfoTavoloBean;
import com.questtable.bean.PagamentoBean;
import com.questtable.bean.PrenotazioneBean;
import com.questtable.bean.PreventivoBean;
import com.questtable.controller.QuestTableController;
import com.questtable.model.MetodoPagamento;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.util.StringConverter;

import java.io.IOException;
import java.util.Optional;

public class PagamentoController {
    private static final String TESTO_CONFERMA_PAGAMENTO = "paga";

    private final QuestTableController questTableController = new QuestTableController();

    private String idSessione;
    private PreventivoBean preventivoBean;
    private InfoTavoloBean tavoloSelezionato;

    @FXML
    private Label lblGioco;

    @FXML
    private Label lblQuando;

    @FXML
    private Label lblPartecipanti;

    @FXML
    private Label lblPunti;

    @FXML
    private Label lblTotale;

    @FXML
    private ComboBox<MetodoPagamento> cmbMetodoPagamento;

    @FXML
    private Label lblMessaggioPagamento;

    @FXML
    public void initialize() {
        cmbMetodoPagamento.setItems(FXCollections.observableArrayList(MetodoPagamento.values()));
        cmbMetodoPagamento.setConverter(new StringConverter<>() {
            @Override
            public String toString(MetodoPagamento metodoPagamento) {
                if (metodoPagamento == null) {
                    return "";
                }
                return metodoPagamento.fornisciNomeVisualizzato();
            }

            @Override
            public MetodoPagamento fromString(String string) {
                return null;
            }
        });
        cmbMetodoPagamento.getSelectionModel().select(MetodoPagamento.CARTA_CREDITO);
    }

    public void inizializzaPagamento(String idSessione, PreventivoBean preventivoBean, InfoTavoloBean tavoloSelezionato) {
        this.idSessione = idSessione;
        this.preventivoBean = preventivoBean;
        this.tavoloSelezionato = tavoloSelezionato;

        lblGioco.setText(preventivoBean.fornisciTitoloGioco());
        lblQuando.setText(preventivoBean.fornisciGiornoSettimana().fornisciNomeVisualizzato()
                + " | " + preventivoBean.fornisciFasciaOraria());
        lblPartecipanti.setText(String.valueOf(preventivoBean.fornisciNumeroPostiRichiesti()));
        lblPunti.setText("+" + preventivoBean.fornisciPuntiFedeltaPrevisti() + " punti");
        lblTotale.setText(FormattatoreImporti.formattaImporto(preventivoBean.fornisciImportoTotale()));
        lblMessaggioPagamento.setText("");
    }

    @FXML
    void onPagaClick(ActionEvent event) {
        Optional<String> testoInserito = richiediConfermaPagamento();
        if (testoInserito.isEmpty()) {
            return;
        }

        if (!verificaTestoPagamento(testoInserito.get())) {
            lblMessaggioPagamento.setText("Pagamento non riuscito");
            return;
        }

        try {
            PagamentoBean pagamentoBean = new PagamentoBean(
                    preventivoBean.fornisciIdentificativoTavolo(),
                    preventivoBean.fornisciNumeroPostiRichiesti(),
                    preventivoBean.fornisciImportoTotale(),
                    cmbMetodoPagamento.getValue(),
                    true
            );

            PrenotazioneBean prenotazioneBean = questTableController.registraPrenotazione(idSessione, pagamentoBean);
            apriRichiestaPrenotazioneOk(event, prenotazioneBean);
        } catch (IllegalArgumentException | IllegalStateException exception) {
            lblMessaggioPagamento.setText(exception.getMessage());
        }
    }

    @FXML
    void onTornaDettaglioClick(ActionEvent event) throws IOException {
        FXMLLoader loader = NavigazioneGrafica.creaLoader(getClass(), "/com/questtable/view/DettagliTavoloView.fxml");
        Parent root = loader.load();

        DettagliTavoloController dettagliTavoloController = loader.getController();
        dettagliTavoloController.inizializzaDettaglio(
                idSessione,
                tavoloSelezionato,
                preventivoBean.fornisciNumeroPostiRichiesti()
        );

        NavigazioneGrafica.aggiornaScena(event, root);
    }

    private Optional<String> richiediConfermaPagamento() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Conferma pagamento");
        dialog.setHeaderText("Simulazione pagamento");
        dialog.setContentText("Scrivi paga per completare il pagamento.");
        return dialog.showAndWait();
    }

    private boolean verificaTestoPagamento(String testoInserito) {
        return TESTO_CONFERMA_PAGAMENTO.equalsIgnoreCase(testoInserito.trim());
    }

    private void apriRichiestaPrenotazioneOk(ActionEvent event, PrenotazioneBean prenotazioneBean) {
        try {
            FXMLLoader loader = NavigazioneGrafica.creaLoader(
                    getClass(),
                    "/com/questtable/view/RichiestaPrenotazioneOkView.fxml"
            );
            Parent root = loader.load();

            RichiestaPrenotazioneOkController richiestaController = loader.getController();
            richiestaController.inizializzaRichiestaInviata(idSessione, prenotazioneBean);

            NavigazioneGrafica.aggiornaScena(event, root);
        } catch (IOException exception) {
            lblMessaggioPagamento.setText("Richiesta registrata, ma schermata finale non disponibile.");
        }
    }
}

