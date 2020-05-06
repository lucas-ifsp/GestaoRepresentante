package br.com.lucas.representante.controller;

import br.com.lucas.representante.model.entities.Client;
import br.com.lucas.representante.model.usecases.UCManageClientPersistence;
import br.com.lucas.representante.persistence.dao.DAOClient;
import br.com.lucas.representante.view.loaders.WindowClient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.util.List;
import java.util.stream.Collectors;

public class CtrlWindowClientManager {
    @FXML TableView<Client> tableClientes;

    @FXML TableColumn<Client, String> cNome;
    @FXML TableColumn<Client, String> cFantasia;
    @FXML TableColumn<Client, String> cID;
    @FXML TableColumn<Client, String> cCidade;
    @FXML TableColumn<Client, String> cCnjpCpf;
    @FXML TableColumn<Client, String> cTelefone1;
    @FXML TableColumn<Client, String> cTelefone2;
    @FXML TableColumn<Client, String> cTipo;
    @FXML Button btnAdicionar;
    @FXML Button btnExportar;
    @FXML CheckBox cbInativo;
    @FXML CheckBox cbProspeccao;
    @FXML TextField txtFiltrar;

    private List<Client> allClients;
    private List<Client> filteredClients;
    private ObservableList<Client> tableData;
    private String filter;
    private boolean showingInactives;
    private boolean showingProspections;

    private UCManageClientPersistence ucManageClient;

    public CtrlWindowClientManager(){
        ucManageClient = new UCManageClientPersistence(new DAOClient());
    }

    @FXML
    private void initialize(){
        bindTableViewToItemsList();
        bindColumnsToValueSources();
        loadDataAndShow();
    }

    private void bindTableViewToItemsList(){
        tableData = FXCollections.observableArrayList();
        tableClientes.setItems(tableData);
    }

    public void bindColumnsToValueSources(){
        cNome.setCellValueFactory(new PropertyValueFactory<>("companyName"));
        cFantasia.setCellValueFactory(new PropertyValueFactory<>("tradeName"));
        cID.setCellValueFactory(new PropertyValueFactory<>("companyId"));
        cCidade.setCellValueFactory(new PropertyValueFactory<>("city"));
        cCnjpCpf.setCellValueFactory(new PropertyValueFactory<>("cnpjOrCpf"));
        cTelefone1.setCellValueFactory(new PropertyValueFactory<>("phone1"));
        cTelefone2.setCellValueFactory(new PropertyValueFactory<>("phone2"));
        cTipo.setCellValueFactory(new PropertyValueFactory<>("state"));
    }

    private void loadDataAndShow() {
        loadTableDataFromDatabase();
        showFilteredData();
    }

    private void loadTableDataFromDatabase() {
        allClients = ucManageClient.selectAll();
    }

    private void showFilteredData() {
        getFiltersFromView();
        updateTableViewFromFilters();
    }

    private void getFiltersFromView(){
        filter = txtFiltrar.getText();
        showingInactives = cbInativo.isSelected();
        showingProspections = cbProspeccao.isSelected();
    }

    private void updateTableViewFromFilters() {
        filterData();
        loadTableWithFilteredData();
    }

    private void filterData() {
        filterDataFromCheckboxes();
        filterDataFromSubstring();
    }

    private void loadTableWithFilteredData() {
        tableData.setAll(filteredClients);
    }

    private void filterDataFromCheckboxes() {
        filteredClients = allClients.
                parallelStream().
                filter(c -> c.matchesSearchByProspection(showingProspections)).
                filter(c -> c.matchesSearchByInactive(showingInactives)).
                collect(Collectors.toList());
    }

    private void filterDataFromSubstring() {
        if(substringIsNotEmpty())
            filteredClients = filteredClients.
                    parallelStream().
                    filter(c -> c.matchesSearchString(filter)).
                    collect(Collectors.toList());
    }

    private boolean substringIsNotEmpty() {
        return !filter.equals("");
    }

    public void filterTableData(KeyEvent keyEvent) {
        showFilteredData();
    }

    public void filterInativo(ActionEvent actionEvent) {
        showFilteredData();
    }

    public void filterProspeccao(ActionEvent actionEvent) {
        showFilteredData();
    }

    public void addClient(ActionEvent actionEvent) {
        WindowClient w = new WindowClient();
        w.startModal();
        loadDataAndShow();
    }

    public void editClient(MouseEvent event) {
        Client selectedClient = tableClientes.getSelectionModel().getSelectedItem();
        if (event.getClickCount() == 2 && selectedClient != null) {
            WindowClient w = new WindowClient(selectedClient);
            w.startModal();
            loadDataAndShow();
        }
    }

    public void exportData(ActionEvent actionEvent) {
    }
}
