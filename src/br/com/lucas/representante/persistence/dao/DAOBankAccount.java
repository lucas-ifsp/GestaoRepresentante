package br.com.lucas.representante.persistence.dao;

import br.com.lucas.representante.model.entities.BankAccount;
import br.com.lucas.representante.persistence.utils.AbstractTemplateSqlDAO;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DAOBankAccount extends AbstractTemplateSqlDAO<BankAccount, String> {

    @Override
    protected String createSaveSql() {
        return "INSERT INTO BankAccount(bankName, bankNumber, agency, " +
                "account, client) VALUES(?, ?, ?, ?, ?)";
    }

    @Override
    protected String createUpdateSql() {
        return "UPDATE BankAccount SET bankName = ?, bankNumber = ?, " +
                "agency = ?, account = ? WHERE client = ?";
    }

    @Override
    protected String createDeleteSql() {
        return "DELETE FROM BankAccount WHERE client = ?";
    }

    @Override
    protected String createSelectSql() {
        return "SELECT * FROM BankAccount WHERE client = ?";
    }

    @Override
    protected String createSelectAllSql() {
        return "SELECT * FROM BankAccount";
    }

    @Override
    protected String createSelectBySql(String field) {
        return "SELECT * FROM BankAccount WHERE "+ field +" = ?";
    }

    @Override
    protected void setEntityToPreparedStatement(@NotNull BankAccount entity, @NotNull PreparedStatement stmt) throws SQLException {
        stmt.setString(1, entity.getBankName());
        stmt.setString(2, entity.getBankNumber());
        stmt.setString(3, entity.getAgency());
        stmt.setString(4, entity.getAccount());
        stmt.setString(5, entity.getOwner().getCnpjOrCpf());
    }

    @Override
    protected void setKeyToPreparedStatement(@NotNull String key, @NotNull PreparedStatement stmt) throws SQLException {
        stmt.setString(1, key);
    }

    @Override
    protected void setFilterToPreparedStatement(@NotNull Object filter, @NotNull PreparedStatement stmt) throws SQLException {
        stmt.setString(1, filter.toString());
    }

    @Override
    protected BankAccount getEntityFromResultSet(@NotNull ResultSet rs) throws SQLException {
        BankAccount account = new BankAccount(
                rs.getString("bankName"),
                rs.getString("bankNumber"),
                rs.getString("agency"),
                rs.getString("account"));
        return account;
    }

    @Override
    protected String getEntityKey(@NotNull BankAccount entity) {
        return entity.getOwner().getCnpjOrCpf();
    }
}