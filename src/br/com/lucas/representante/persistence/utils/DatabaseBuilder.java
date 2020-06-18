package br.com.lucas.representante.persistence.utils;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseBuilder {

    public void buildDatabaseIfMissing(){
        if(!isDatabaseAvailable()){
            System.out.println("Database is missing. Building database: \n");
            buildTables();
        }
    }

    private boolean isDatabaseAvailable(){
        return Files.exists(Paths.get("database.db"));
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
}
