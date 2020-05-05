package br.com.lucas.representante.controller;

import br.com.lucas.representante.model.entities.Contact;
import br.com.lucas.representante.view.util.InputValidator;
import br.com.lucas.representante.view.util.TextFieldFormater;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CtrlWindowContact {

    @FXML TextField txtNome;
    @FXML TextField txtCargo;
    @FXML TextField txtRG;
    @FXML TextField txtCPF;
    @FXML DatePicker dpAniversario;
    @FXML TextField txtTelefone;
    @FXML TextField txtEmail;
    @FXML Button btnCancelar;
    @FXML Button btnExcluir;

    private Contact contactToSaveOrUpdate;
    private Contact contactToDelete;
    private Contact contactToSet;
    private String errorMessage;

    @FXML
    private void initialize(){
        configureTextFields();
    }

    private void configureTextFields() {
        TextFieldFormater.formatAsLiteralWithAccent(txtNome);
        TextFieldFormater.formatAsLiteralWithAccent(txtCargo);
        TextFieldFormater.formatAsRG(txtRG);
        TextFieldFormater.formatAsNumericWithDashesAndDots(txtCPF);
        TextFieldFormater.formatAsPhoneNumber(txtTelefone);
    }

    public void setEntityToView(Contact contact) {
        contactToSet = contact;
        txtNome.setText(contact.getName());
        txtCargo.setText(contact.getPosition());
        txtRG.setText(contact.getRg());
        txtCPF.setText(contact.getCpf());
        txtTelefone.setText(contact.getPhone());
        txtEmail.setText(contact.getEmail());
        dpAniversario.setValue(contact.getBirthday());
        setViewToEditMode();
    }

    private void setViewToEditMode() {
        txtEmail.setDisable(true);
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
        if(textFieldIsEmpty(txtNome))
            appendErrorMessage("O campo 'Nome' é obrigatório.");
        if(textFieldIsEmpty(txtEmail) || !InputValidator.isEmail(txtEmail.getText()))
            appendErrorMessage("O e-mail é obrigatório e precisa estar em um formato válido.");
        if(textFieldIsNotEmpty(txtCPF) && !InputValidator.isCNPJOrCPF(txtCPF.getText()))
            appendErrorMessage("O formato do CPF não é válido.");
        if(textFieldIsNotEmpty(txtTelefone) && !InputValidator.isTelefone(txtTelefone.getText()))
            appendErrorMessage("O formato do telefone não é válido.");
    }

    private boolean textFieldIsEmpty(TextField textField){
        String text = textField.getText();
        return text.isEmpty();
    }

    private boolean textFieldIsNotEmpty(TextField textField){
        return !textFieldIsEmpty(textField);
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
        contactToSaveOrUpdate = new Contact(
                txtNome.getText().trim(),
                txtEmail.getText().trim(),
                txtCargo.getText().trim(),
                txtTelefone.getText().trim(),
                txtCPF.getText().trim(),
                txtRG.getText().trim(),
                dpAniversario.getValue());
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

    public void cancel(ActionEvent actionEvent) {
        closeStage();
    }

    public void delete(ActionEvent actionEvent) {
        if(isExistingContact()){
            contactToDelete = contactToSet;
            closeStage();
        }
    }

    private boolean isExistingContact() {
        return contactToSet != null;
    }

    public Contact toSaveOrUpdate() {
        return contactToSaveOrUpdate;
    }

    public Contact toDelete() {
        return contactToDelete;
    }
}
