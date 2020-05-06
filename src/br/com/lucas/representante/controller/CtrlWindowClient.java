package br.com.lucas.representante.controller;

import br.com.lucas.representante.model.entities.*;
import br.com.lucas.representante.model.usecases.UCManageClientPersistence;
import br.com.lucas.representante.persistence.dao.*;
import br.com.lucas.representante.view.loaders.WindowContact;
import br.com.lucas.representante.view.loaders.WindowHistory;
import br.com.lucas.representante.view.util.InputValidator;
import br.com.lucas.representante.view.util.TextFieldFormater;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CtrlWindowClient {

    @FXML TableView<Contact> tableContacts;
    @FXML TableColumn<Contact, String> cNome;
    @FXML TableColumn<Contact, String> cEmail;
    @FXML TableColumn<Contact, String> cTelefone;
    @FXML TableColumn<Contact, String> cCargo;

    @FXML TableView<History> tableHistory;
    @FXML TableColumn<Contact, String> cData;
    @FXML TableColumn<Contact, String> cResumo;


    @FXML TextField txtNome; 
    @FXML TextField txtFantasia;
    @FXML TextField txtCnpjCpf;
    @FXML TextField txtIDCorporativo;
    @FXML TextField txtInscEstadual;
    @FXML TextField txtInscMunicipal;
    @FXML TextField txtTelefone1;
    @FXML TextField txtTelefone2;
    @FXML TextField txtFax;
    @FXML TextField txtCEP;
    @FXML TextField txtRua;
    @FXML TextField txtNumero;
    @FXML TextField txtBairro;
    @FXML TextField txtCidade;
    @FXML TextField txtReferencia;
    @FXML TextField txtConta;
    @FXML TextField txtAgencia;
    @FXML TextField txtNumeroBanco;
    @FXML TextField txtNomeBanco;
    @FXML CheckBox ckProspeccao;
    @FXML CheckBox ckAtivo;
    @FXML DatePicker dpClienteDesde;
    @FXML ComboBox<String> cbEstado;
    @FXML Button btnAdicionarContato;
    @FXML Button btnAdicionarHistorico;
    @FXML Button btnCancelar;
    @FXML Button btnSalvar;
    @FXML Button btnExcluir;

    private Client client;
    private UCManageClientPersistence ucManageClient;
    private ObservableList<Contact> tableContactsData;
    private ObservableList<History> tableHistoryData;
    private String errorMessage;

    public CtrlWindowClient(){
        ucManageClient = new UCManageClientPersistence(
                new DAOClient(),
                new DAOAddress(),
                new DAOBankAccount(),
                new DAOContact(),
                new DAOHistory());
    }

    @FXML
    private void initialize(){
        bindStatesToComboBox();
        bindTableViewsToItemsList();
        bindColumnsToValueSources();
        configureTextFields();
    }

    private void bindStatesToComboBox() {
        ObservableList<String> states = FXCollections.observableArrayList();
        cbEstado.setItems(states);
        states.addAll("AC", "AL", "AP", "AM", "BA", "CE", "DF", "ES", "GO", "MA", "MT", "MS", "MG",
                "PA", "PB", "PR", "PE", "PI", "RJ", "RN", "RS", "RO", "RR", "SC", "SP", "SE", "TO");
    }

    private void bindTableViewsToItemsList(){
        tableContactsData = FXCollections.observableArrayList();
        tableContacts.setItems(tableContactsData);

        tableHistoryData = FXCollections.observableArrayList();
        tableHistory.setItems(tableHistoryData);
    }

    public void bindColumnsToValueSources(){
        cNome.setCellValueFactory(new PropertyValueFactory<>("name"));
        cEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        cCargo.setCellValueFactory(new PropertyValueFactory<>("position"));
        cTelefone.setCellValueFactory(new PropertyValueFactory<>("phone"));

        cResumo.setCellValueFactory(new PropertyValueFactory<>("title"));
        cData.setCellValueFactory(new PropertyValueFactory<>("formattedDate"));
    }

    private void configureTextFields() {
        TextFieldFormater.formatAsLiteralWithAccent(txtRua);
        TextFieldFormater.formatAsLiteralWithAccent(txtBairro);
        TextFieldFormater.formatAsLiteralWithAccent(txtCidade);
        TextFieldFormater.formatAsNumeric(txtNumero);
        TextFieldFormater.formatAsLiteralWithAccent(txtNomeBanco);
        TextFieldFormater.formatAsNumeric(txtNumeroBanco);
        TextFieldFormater.formatAsNumericWithDashes(txtConta);
        TextFieldFormater.formatAsNumericWithDashes(txtAgencia);
        TextFieldFormater.formatAsPhoneNumber(txtTelefone1);
        TextFieldFormater.formatAsPhoneNumber(txtTelefone2);
        TextFieldFormater.formatAsPhoneNumber(txtFax);
        TextFieldFormater.formatAsNumericWithDashesDotsAndSlashes(txtCnpjCpf);
        TextFieldFormater.formatAsNumericWithDashes(txtCEP);
        TextFieldFormater.formatAsNumericWithDashesAndDots(txtInscEstadual);
        TextFieldFormater.formatAsNumericWithDashesAndDots(txtInscMunicipal);
    }


    public void addNewContact(ActionEvent actionEvent) {
        WindowContact w = new WindowContact();
        w.startModal();
        initializeClientIfNull();
        updateContactsFromResult(w);
        refreshContactsTableData();
    }

    private void initializeClientIfNull() {
        if(client == null)
            client = new Client();
    }

    private void updateContactsFromResult(WindowContact w) {
        Contact deleteCandidate = w.toDelete();
        Contact saveOrUpdateCandidate = w.toSaveOrUpdate();
        if(deleteCandidate != null)
            client.removeContact(deleteCandidate);
        else if(saveOrUpdateCandidate != null)
            client.setContact(saveOrUpdateCandidate);
    }

    private void refreshContactsTableData() {
        List<Contact> contacts = client.getContacts();
        tableContactsData.setAll(contacts);
    }

    public void editContact(MouseEvent event) {
        SelectionModel<Contact> model = tableContacts.getSelectionModel();
        Contact selectedContact = model.getSelectedItem();
        if (event.getClickCount() == 2 && selectedContact != null) {
            WindowContact w = new WindowContact(selectedContact);
            w.startModal();
            updateContactsFromResult(w);
            refreshContactsTableData();
        }
    }

    public void addNewHistory(ActionEvent actionEvent) {
        WindowHistory w = new WindowHistory();
        w.startModal();
        initializeClientIfNull();
        updateHistoryFromResult(w);
        refreshHistoryTableData();
    }

    private void updateHistoryFromResult(WindowHistory w) {
        History deleteCandidate = w.toDelete();
        History saveOrUpdateCandidate = w.toSaveOrUpdate();
        if(deleteCandidate != null)
            client.removeHistory(deleteCandidate);
        else if(saveOrUpdateCandidate != null)
            client.setHistory(saveOrUpdateCandidate);
    }

    private void refreshHistoryTableData() {
        List<History> history = client.getHistory();
        List<History> historySortedByDate = history.stream().
                sorted(Comparator.comparing(History::getDate).reversed()).
                collect(Collectors.toList());
        tableHistoryData.setAll(historySortedByDate);
    }

    public void editHistory(MouseEvent event) {
        SelectionModel<History> model = tableHistory.getSelectionModel();
        History selectedHistory = model.getSelectedItem();
        if (event.getClickCount() == 2 && selectedHistory != null) {
            WindowHistory w = new WindowHistory(selectedHistory);
            w.startModal();
            updateHistoryFromResult(w);
            refreshHistoryTableData();
        }
    }

    public void saveOrUpdateClient(ActionEvent actionEvent) {
        identifyErrorsAndBuildMsg();
        if(allViewDataIsOk())
            requestSaveOrUpdate();
        else
            showErrorMessage();
    }

    private void identifyErrorsAndBuildMsg() {
        if(clientIsMissingMainInfo())
            appendErrorMessage("Os campos 'Razão Social', 'Nome Fantasia' e 'CNPJ ou CPF' são obrigatórios.");
        if(addressInfoIsIncomplete())
            appendErrorMessage("Informações sobre endereço são opcionais, mas se presentes não podem estar incompletas.");
        if(accountInfoIsIncomplete())
            appendErrorMessage("Informações bancárias são opcionais, mas se presentes não podem estar incompletas.");
        if(phoneNumbersAreNotValid())
            appendErrorMessage("O formato de um ou mais telefones não é válido.");
        if(textFieldIsNotEmpty(txtCEP) && !InputValidator.isCEP(txtCEP.getText()))
            appendErrorMessage("O formato do CEP não é válido.");
        if(textFieldIsNotEmpty(txtCnpjCpf) && !InputValidator.isCNPJOrCPF(txtCnpjCpf.getText()))
            appendErrorMessage("O formato do CNPJ ou CPF não é válido.");
    }

    private boolean clientIsMissingMainInfo(){
        boolean nomeIsNotFilled = txtNome.getText().equals("");
        boolean fantasiaIsNotFilled = txtFantasia.getText().equals("");
        boolean cnpjCpfIsNotFilled = txtCnpjCpf.getText().equals("");
        return nomeIsNotFilled || fantasiaIsNotFilled || cnpjCpfIsNotFilled;
    }

    private boolean addressInfoIsIncomplete() {
        List<String> values = getAddressDataAsString();
        return someStringsAreNotFilled(values);
    }

    private List<String> getAddressDataAsString() {
        String stateInfo = cbEstado.getValue() == null? "" : cbEstado.getValue();
        List<String> addressData = Arrays.asList(
                txtRua.getText(),
                txtNumero.getText(),
                txtBairro.getText(),
                txtCEP.getText(),
                txtCidade.getText(),
                stateInfo);
        return addressData;
    }

    private boolean someStringsAreNotFilled(List<String> list){
        boolean anyStringIsFilled = (int) list.parallelStream().filter(x -> !x.equals("")).count() > 0;
        return  anyStringIsFilled && !allStringsAreFilled(list);
    }

    private boolean accountInfoIsIncomplete() {
        List<String> values = getAccountDataAsString();
        return someStringsAreNotFilled(values);
    }

    private List<String> getAccountDataAsString() {
        List<String> accountData = Arrays.asList(
                txtAgencia.getText(),
                txtConta.getText(),
                txtNumeroBanco.getText(),
                txtNomeBanco.getText());
        return accountData;
    }

    private boolean phoneNumbersAreNotValid() {
        String phone1 = txtTelefone1.getText();
        String phone2 = txtTelefone2.getText();
        String fax = txtFax.getText();

        boolean isPhone1Valid = phone1.isEmpty() || InputValidator.isTelefone(phone1);
        boolean isPhone2Valid = phone2.isEmpty() || InputValidator.isTelefone(phone2);
        boolean isFaxValid = fax.isEmpty() || InputValidator.isTelefone(fax);

        return !(isPhone1Valid && isPhone2Valid && isFaxValid);
    }

    private boolean textFieldIsNotEmpty(TextField textField){
        String text = textField.getText();
        return !text.isEmpty();
    }

    private void appendErrorMessage(String message) {
        if (errorMessage == null)
            errorMessage = " Por favor, corrija o(s) seguinte(s) erros: \n";
        errorMessage += "\n - ".concat(message);
    }

    private boolean allViewDataIsOk() {
        return errorMessage == null;
    }

    private void requestSaveOrUpdate() {
        getEntityFromView();
        ucManageClient.saveOrUpdate(client);
        closeStage();
    }

    private void closeStage() {
        Stage stage = (Stage) btnCancelar.getScene().getWindow();
        stage.close();
    }

    private void showErrorMessage() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Informações Incompletas.");
        alert.setHeaderText(errorMessage);
        alert.setContentText(null);
        alert.showAndWait();
        errorMessage = null;
    }

    public void cancel(ActionEvent actionEvent) {
        closeStage();
    }

    public void deleteClient(ActionEvent actionEvent) {
        if(deletionIsConfirmed()) {
            getEntityFromView();
            ucManageClient.delete(client);
            closeStage();
        }
    }

    private boolean deletionIsConfirmed() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmação de Exclusão");
        alert.setHeaderText("Deseja confirmar a exclusão? Todos os dados do cliente serão perdidos. ");
        alert.setContentText("Se preferir, você pode preservar os dados do cliente desmarcando a opção Cadastro Ativo.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK)
            return true;
        else
            return false;
    }

    private void getEntityFromView(){
        initializeClientIfNull();
        client.setCompanyName(txtNome.getText().trim());
        client.setTradeName(txtFantasia.getText().trim());
        client.setCompanyId(txtIDCorporativo.getText().trim());
        client.setCnpjOrCpf(txtCnpjCpf.getText().trim());
        client.setStateRegistration(txtInscEstadual.getText().trim());
        client.setCityRegistration(txtInscMunicipal.getText().trim());
        client.setClientSince(dpClienteDesde.getValue());
        client.setPhone1(txtTelefone1.getText().trim());
        client.setPhone2(txtTelefone2.getText().trim());
        client.setFax(txtFax.getText().trim());
        client.setProspection(ckProspeccao.isSelected());
        client.setActive(ckAtivo.isSelected());

        getAddressFromView().ifPresent(x -> client.setAddress(x));
        getAccountFromView().ifPresent(x -> client.setAccount(x));
    }

    private Optional<Address> getAddressFromView() {
        if(addressInfoIsAvailable()) {
            Address address = new Address(
                    txtRua.getText().trim(),
                    Integer.valueOf(txtNumero.getText().trim()),
                    txtBairro.getText().trim(),
                    txtCEP.getText().trim(),
                    txtCidade.getText().trim(),
                    cbEstado.getValue(),
                    txtReferencia.getText().trim());
            return Optional.of(address);
        }
        return Optional.empty();
    }

    private boolean addressInfoIsAvailable() {
        List<String> values = getAddressDataAsString();
        return allStringsAreFilled(values);
    }

    private boolean allStringsAreFilled(List<String> list){
        int totalElements = list.size();
        int filledElements = (int) list.parallelStream().filter(x -> !x.equals("")).count();
        return filledElements == totalElements;
    }

    private Optional<BankAccount> getAccountFromView() {
        if(accountInfoIsAvailable()) {
            BankAccount account = new BankAccount(
                    txtNomeBanco.getText(),
                    txtNumeroBanco.getText(),
                    txtAgencia.getText(),
                    txtConta.getText());
            return Optional.of(account);
        }
        return Optional.empty();
    }

    private boolean accountInfoIsAvailable() {
        List<String> values = getAccountDataAsString();
        return allStringsAreFilled(values);
    }

    public void setEntityToView(Client client) {
        this.client = client;
        txtNome.setText(client.getCompanyName());
        txtFantasia.setText(client.getTradeName());
        txtIDCorporativo.setText(client.getCompanyId());
        txtCnpjCpf.setText(client.getCnpjOrCpf());
        txtInscEstadual.setText(client.getStateRegistration());
        txtInscMunicipal.setText(client.getCityRegistration());
        dpClienteDesde.setValue(client.getClientSince());
        txtTelefone1.setText(client.getPhone1());
        txtTelefone2.setText(client.getPhone2());
        txtFax.setText(client.getFax());
        ckProspeccao.setSelected(client.isProspection());
        ckAtivo.setSelected(client.isActive());

        Address address = client.getAddress();
        if(client.getAddress() != null)
            setAddressToView(address);

        BankAccount account = client.getAccount();
        if(account != null)
            setAccountToView(account);

        refreshContactsTableData();
        refreshHistoryTableData();
        setViewToEditMode();
    }

    private void setViewToEditMode() {
        txtCnpjCpf.setDisable(true);
        btnExcluir.setVisible(true);
    }

    private void setAddressToView(Address address) {
        txtRua.setText(address.getStreet());
        txtNumero.setText(address.getNumber()+"");
        txtBairro.setText(address.getArea());
        txtCEP.setText(address.getZipCode());
        txtCidade.setText(address.getCity());
        cbEstado.getSelectionModel().select(address.getState());
        txtReferencia.setText(address.getPointOfReference());
    }

    private void setAccountToView(BankAccount account) {
        txtNomeBanco.setText(account.getBankName());
        txtNumeroBanco.setText(account.getBankNumber());
        txtAgencia.setText(account.getAgency());
        txtConta.setText(account.getAccount());
    }
}
