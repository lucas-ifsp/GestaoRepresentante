package br.com.lucas.representante.persistence.utils;

import java.sql.*;

public class ConnectionFactory implements AutoCloseable{

    private static Connection connection;
    private static PreparedStatement preparedStatement;
    private static Statement statement;

    public static Connection createConnection() {
        try {
            if(connection == null)
                connection = DriverManager.getConnection("jdbc:sqlite:database.db");
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public static PreparedStatement createPreparedStatement(String sql) {
        try {
            preparedStatement = createConnection().prepareStatement(sql);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return preparedStatement;
    }

    public static Statement createStatement() {
        try {
            statement = createConnection().createStatement();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return statement;
    }

    @Override
    public void close() throws Exception {
            if(connection != null){
                connection.close();
                if(preparedStatement != null)
                    preparedStatement.close();
                if(statement != null)
                    statement.close();
        }
    }
}