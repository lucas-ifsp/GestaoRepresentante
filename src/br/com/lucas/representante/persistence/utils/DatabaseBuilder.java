package br.com.lucas.representante.persistence.utils;

import br.com.lucas.representante.model.entities.*;
import br.com.lucas.representante.model.usecases.UCImportData;
import br.com.lucas.representante.persistence.dao.DAOAddress;
import br.com.lucas.representante.persistence.dao.DAOBankAccount;
import br.com.lucas.representante.persistence.dao.DAOClient;
import br.com.lucas.representante.persistence.dao.DAOContact;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class DatabaseBuilder {

    public void buildDatabaseIfMissing(){
        if(!isDatabaseAvailable()){
            System.out.println("Database is missing. Building database: \n");
            buildTables();
            importDataFromCSV();
        }
    }

    private boolean isDatabaseAvailable(){
        return Files.exists(Paths.get(PathFinder.find()+"database.db"));
    }

    private void buildTables() {
        try (Statement stmt = ConnectionFactory.createStatement()) {
            stmt.addBatch(createClientTableSql());
            stmt.addBatch(createContactTableSql());
            stmt.addBatch(createClientContactsTableSql());
            stmt.addBatch(createAddressTableSql());
            stmt.addBatch(createBankAccountTableSql());
            stmt.addBatch(createHistoryTableSql());
            stmt.executeBatch();

            System.out.println("Database successfully created.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private String createClientTableSql(){
        StringBuilder builder = new StringBuilder();

        builder.append("CREATE TABLE Client (\n");
        builder.append("companyName TEXT NOT NULL, \n");
        builder.append("tradeName TEXT NOT NULL, \n");
        builder.append("companyId TEXT, \n");
        builder.append("cnpjOrCpf TEXT NOT NULL PRIMARY KEY, \n");
        builder.append("stateRegistration TEXT, \n");
        builder.append("cityRegistration TEXT, \n");
        builder.append("clientSince TEXT, \n");
        builder.append("phone1 TEXT, \n");
        builder.append("phone2 TEXT, \n");
        builder.append("fax TEXT, \n");
        builder.append("prospection INTEGER NOT NULL, \n");
        builder.append("active INTEGER NOT NULL \n");
        builder.append("); \n");

        System.out.println(builder.toString());
        return builder.toString();
    }


    private String createContactTableSql(){
        StringBuilder builder = new StringBuilder();

        builder.append("CREATE TABLE Contact (\n");
        builder.append("name TEXT NOT NULL, \n");
        builder.append("email TEXT NOT NULL PRIMARY KEY, \n");
        builder.append("position TEXT, \n");
        builder.append("phone TEXT, \n");
        builder.append("cpf TEXT, \n");
        builder.append("rg TEXT, \n");
        builder.append("birthday TEXT\n");
        builder.append("); \n");

        System.out.println(builder.toString());
        return builder.toString();
    }

    private String createClientContactsTableSql(){
        StringBuilder builder = new StringBuilder();

        builder.append("CREATE TABLE ClientContacts (\n");
        builder.append("client TEXT NOT NULL, \n");
        builder.append("contact TEXT NOT NULL, \n");
        builder.append("PRIMARY KEY (client, contact), \n");
        builder.append("FOREIGN KEY(client) REFERENCES client(cnpjOrCpf)\n");
        builder.append("FOREIGN KEY(contact) REFERENCES Contact(email)\n");
        builder.append("); \n");

        System.out.println(builder.toString());
        return builder.toString();
    }

    private String createAddressTableSql(){
        StringBuilder builder = new StringBuilder();

        builder.append("CREATE TABLE Address (\n");
        builder.append("street TEXT NOT NULL, \n");
        builder.append("number INTEGER, \n");
        builder.append("area TEXT NOT NULL, \n");
        builder.append("city TEXT NOT NULL, \n");
        builder.append("state TEXT NOT NULL, \n");
        builder.append("zipCode TEXT, ");
        builder.append("pointOfReference TEXT, \n");
        builder.append("client TEXT NOT NULL PRIMARY KEY, \n");
        builder.append("FOREIGN KEY(client) REFERENCES client(cnpjOrCpf)\n");
        builder.append("); \n");

        System.out.println(builder.toString());
        return builder.toString();
    }

    private String createBankAccountTableSql(){
        StringBuilder builder = new StringBuilder();

        builder.append("CREATE TABLE BankAccount (\n");
        builder.append("bankName TEXT NOT NULL, \n");
        builder.append("bankNumber TEXT NOT NULL, \n");
        builder.append("agency TEXT NOT NULL, \n");
        builder.append("account TEXT NOT NULL, \n");
        builder.append("client TEXT NOT NULL PRIMARY KEY, \n");
        builder.append("FOREIGN KEY(client) REFERENCES client(cnpjOrCpf)\n");
        builder.append("); \n");

        System.out.println(builder.toString());
        return builder.toString();
    }

    private String createHistoryTableSql(){
        StringBuilder builder = new StringBuilder();

        builder.append("CREATE TABLE History (\n");
        builder.append("id INTEGER PRIMARY KEY AUTOINCREMENT, \n");
        builder.append("title TEXT NOT NULL, \n");
        builder.append("description TEXT, \n");
        builder.append("date TEXT NOT NULL, \n");
        builder.append("client TEXT NOT NULL, \n");
        builder.append("FOREIGN KEY(client) REFERENCES client(cnpjOrCpf)\n");
        builder.append("); \n");

        System.out.println(builder.toString());
        return builder.toString();
    }

    private void importDataFromCSV() {
        System.out.println("CSV file has been loaded. Importing clients...");
        UCImportData uc = new UCImportData();

        List<Client> importedClients =  uc.importFromCSV(
                PathFinder.find()+"imp2.csv",
                PathFinder.find()+"imp1.csv");

        List<Client> sortedClients = importedClients.stream().
                sorted(Comparator.comparing(Client::getCompanyName)).
                collect(Collectors.toList());

        DAOClient daoClient = new DAOClient();
        DAO<Address,String> daoAddress = new DAOAddress();
        DAO<BankAccount, String> daoAccount = new DAOBankAccount();
        DAO<Contact, String> daoContact = new DAOContact();

        sortedClients.forEach(client -> {
            daoClient.save(client);
            daoAddress.save(client.getAddress());
            daoAccount.save(client.getAccount());
            client.getContacts().forEach(contact -> {
                daoContact.saveOrUpdate(contact);
                daoClient.addClientContact(contact);
            });
        });
        System.out.println("Clients have been imported.");
    }
}
