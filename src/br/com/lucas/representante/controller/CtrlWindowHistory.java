package br.com.lucas.representante.controller;

import br.com.lucas.representante.model.entities.History;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDate;

public class CtrlWindowHistory {

    @FXML TextField txtTitulo;
    @FXML DatePicker dpData;
    @FXML TextArea txaDescricao;
    @FXML Button btnCancelar;
    @FXML Button btnExcluir;

    private History historyToSaveOrUpdate;
    private History historyToDelete;
    private History historyToSet;

    private String errorMessage;

    @FXML
    private void initialize(){
        dpData.setValue(LocalDate.now());
    }

    public void setEntityToView(History history) {
        historyToSet = history;
        txtTitulo.setText(history.getTitle());
        dpData.setValue(historyToSet.getDate());
        txaDescricao.setText(history.getDescription());
        setViewToEditMode();
    }

    private void setViewToEditMode() {
        btnExcluir.setVisible(true);
    }

    public void saveEntityOrNotifyErrors(ActionEvent actionEvent) {
        identifyErrorsAndBuildMsg();
        if(allViewDataIsOk()) {
            getEntityFromView();
            closeStage();
        }else {
            showErrorMessage();
        }
    }
    private void identifyErrorsAndBuildMsg() {
        if(txtTitulo.getText().isEmpty())
            appendErrorMessage("O campo 'resumo' é obrigatório.");
    }

    private void appendErrorMessage(String message) {
        if (errorMessage == null)
            errorMessage = " Por favor, corrija o(s) seguinte(s) erros: \n";
        errorMessage += "\n - ".concat(message);
    }

    private boolean allViewDataIsOk() {
        return errorMessage == null;
    }

    private void getEntityFromView(){
        historyToSaveOrUpdate = new History(
                txtTitulo.getText().trim(),
                txaDescricao.getText().trim(),
                dpData.getValue());
        if(isUpdateRequest())
            historyToSaveOrUpdate.setId(historyToSet.getId());
    }

    private boolean isUpdateRequest() {
        return historyToSet != null && historyToSet.getId() != -1;
    }

    private void closeStage() {
        Stage stage = (Stage) btnCancelar.getScene().getWindow();
        stage.close();
    }

    private void showErrorMessage() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Informações Incompletas");
        alert.setHeaderText(errorMessage);
        alert.setContentText(null);
        alert.showAndWait();
        errorMessage = null;
    }

    public History toSaveOrUpdate() {
        return historyToSaveOrUpdate;
    }

    public History toDelete() {
        return historyToDelete;
    }

    public void cancel(ActionEvent actionEvent) {
        closeStage();
    }

    public void delete(ActionEvent actionEvent) {
        if(isExistingHistory()){
            historyToDelete = historyToSet;
            closeStage();
        }
    }

    private boolean isExistingHistory() {
        return historyToSet != null;
    }
}
