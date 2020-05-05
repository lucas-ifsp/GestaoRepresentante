package br.com.lucas.representante.persistence.utils;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseBuilder {

    public void buildDatabaseIfMissing(){
        if(!isDatabaseAvailable()){
            System.out.println("Database is missing. Building database: \n");
            buildTables();
            populateDatabase();
        }
    }

    private boolean isDatabaseAvailable(){
        return Files.exists(Paths.get("database.db"));
    }

    private void buildTables() {
        try (Statement stmt = ConnectionFactory.createStatement()) {
            stmt.addBatch(createClientTableSql());
            stmt.addBatch(createContactTableSql());
            stmt.addBatch(createAddressTableSql());
            stmt.addBatch(createBankAccountTableSql());
            stmt.addBatch(createHistoryTableSql());
            stmt.executeBatch();

            System.out.println("Database has been created ...");
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
        builder.append("birthday TEXT, \n");
        builder.append("client TEXT NOT NULL, \n");
        builder.append("FOREIGN KEY(client) REFERENCES client(cnpjOrCpf)\n");
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

    private  void populateDatabase(){
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:database.db");
             Statement stmt = conn.createStatement()) {
            stmt.addBatch("insert into Client (companyName, tradeName, companyId, cnpjOrCpf, stateRegistration, clientSince, cityRegistration, phone1, phone2, fax, prospection, active) values ('INSTITUTO FEDERAL DE EDUCACAO, CIENCIA E TECNOLOGIA DE SAO PAULO', 'INSTITUTO FEDERAL DE SAO PAULO', 'IFSP', '10.882.594/0001-65', '22.333.4444-1', '2000-01-02', '22.22.333.4444.333', '(16) 9739-8555', '(16) 4913 2828', '(16) 3303-0000', 0, 1);");
            stmt.addBatch("insert into Client (companyName, tradeName, companyId, cnpjOrCpf, stateRegistration, clientSince, cityRegistration, phone1, phone2, fax, prospection, active) values ('Universidade Federal de São Carlos', 'Federupa', 'UFSCar', '45.358.058/0001-40', '33.444.5555-1', '2014-02-25', '11.11.222.5555.000', '(11) 9739-8555', '(11) 4913 2828', '(11) 3303-0000', 0, 1);");
            stmt.addBatch("insert into Client (companyName, tradeName, companyId, cnpjOrCpf, stateRegistration, clientSince, cityRegistration, phone1, phone2, fax, prospection, active) values ('Universidade Estadual Paulista Júlio de Mesquita Filho', 'Universidade Estadual Paulista', 'UNESP', '48.031.918/0001-24', '55.555.5555-1', '2012-12-15', '44.11.444.1111.000', '(11) 9723-8532', '(11) 2313 2828', '(11) 3332-0230', 0, 1);");

            stmt.addBatch("INSERT INTO Address(street, number, area, city, state, " +
                    "zipCode, pointOfReference, client) " +
                    "VALUES('Rod. Araraquara-Jaú', 1, 'Machados', 'Araraquara', 'SP', '14800-901', 'Perto da Heineken', '48.031.918/0001-24')");


            stmt.addBatch("INSERT INTO Address(street, number, area, city, state, " +
                            "zipCode, pointOfReference, client) " +
                            "VALUES('Rua Pedro Vicente', 625, 'Canindé', 'São Paulo', 'SP', '01109-010', 'Metro Armênia', '10.882.594/0001-65')");

            stmt.addBatch("INSERT INTO BankAccount(bankName, bankNumber, agency, " +
                    "account, client) VALUES('Banco do Brasil', '001','0405-x', '0302', '10.882.594/0001-65')");

            stmt.addBatch("INSERT INTO BankAccount(bankName, bankNumber, agency, " +
                    "account, client) VALUES('Nubank', '260', '213213-0', '0001', '45.358.058/0001-40')");

            stmt.addBatch("INSERT INTO BankAccount(bankName, bankNumber, agency, " +
                    "account, client) VALUES('Caixa Econômica', '021', '32131-1', '2031-2', '48.031.918/0001-24')");


            stmt.executeBatch();
            stmt.close();
            System.out.println("Database has been populated ...");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
