package br.com.lucas.representante.controller;

import br.com.lucas.representante.model.Client;
import br.com.lucas.representante.persistence.dao.DAO;
import br.com.lucas.representante.persistence.dao.DAOClient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;

import java.util.List;
import java.util.stream.Collectors;

public class CtrlClientManager {
    @FXML TableView<Client> tableClientes;

    @FXML TableColumn<Client, String> cNome;
    @FXML TableColumn<Client, String> cFantasia;
    @FXML TableColumn<Client, String> cID;
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

    @FXML
    private void initialize(){
        bindTableViewToItemsList();
        bindColumnsToValueSources();
        loadTableDataFromDatabase();
    }

    private void bindTableViewToItemsList(){
        tableData = FXCollections.observableArrayList();
        tableClientes.setItems(tableData);
    }

    public void bindColumnsToValueSources(){
        cNome.setCellValueFactory(new PropertyValueFactory<>("companyName"));
        cFantasia.setCellValueFactory(new PropertyValueFactory<>("tradeName"));
        cID.setCellValueFactory(new PropertyValueFactory<>("companyId"));
        cCnjpCpf.setCellValueFactory(new PropertyValueFactory<>("cnpjOrCpf"));
        cTelefone1.setCellValueFactory(new PropertyValueFactory<>("phone1"));
        cTelefone2.setCellValueFactory(new PropertyValueFactory<>("phone2"));
        cTipo.setCellValueFactory(new PropertyValueFactory<>("state"));
    }

    private void loadTableDataFromDatabase() {
        DAO<Client, String> dao = new DAOClient();
        allClients = dao.selectAll();
        tableData.setAll(allClients);
    }

    public void filterTableData(KeyEvent keyEvent) {
        String filter = txtFiltrar.getText();
        filterData(filter);
        reloadTableWithFilteredData();
    }

    private void filterData(String filter) {
        if(filter.equals(""))
            filteredClients = allClients;
        else{
            filteredClients = allClients.
                    parallelStream().
                    filter(c -> c.matchesSearchName(filter)).
                    collect(Collectors.toList());
        }
    }

    private void reloadTableWithFilteredData() {
        tableData.clear();
        tableData.addAll(filteredClients);
    }

    public void filterProspeccao(ActionEvent actionEvent) {
    }

    public void filterInativo(ActionEvent actionEvent) {
    }

    public void exportData(ActionEvent actionEvent) {
    }

    public void addClient(ActionEvent actionEvent) {
    }
}
