package br.com.lucas.representante.persistence.dao;

import br.com.lucas.representante.model.BankAccount;
import br.com.lucas.representante.persistence.utils.AbstractTemplateSqlDAO;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DAOBankAccount extends AbstractTemplateSqlDAO<BankAccount, String> {

    @Override
    protected String createSaveSql() {
        return "INSERT INTO BankAccount(bankName, bankNumber, agency, " +
                "account, clientCnpjOrCpf) VALUES(?, ?, ?, ?, ?)";
    }

    @Override
    protected String createUpdateSql() {
        return "UPDATE BankAccount SET bankName = ?, bankNumber = ?, " +
                "agency = ?, account = ? WHERE clientCnpjOrCpf = ?";
    }

    @Override
    protected String createDeleteSql() {
        return "DELETE FROM BankAccount WHERE clientCnpjOrCpf = ?";
    }

    @Override
    protected String createSelectSql(@Nullable String condition) {
        return "SELECT * FROM BankAccount WHERE clientCnpjOrCpf = ?";
    }

    @Override
    protected String createSelectAllSql() {
        return "SELECT * FROM BankAccount";
    }

    @Override
    protected void setEntityToPreparedStatement(@NotNull BankAccount entity, @NotNull PreparedStatement stmt) throws SQLException {
        stmt.setString(1, entity.getBankName());
        stmt.setInt(2, entity.getBankNumber());
        stmt.setInt(3, entity.getAgency());
        stmt.setInt(4, entity.getAccount());
        stmt.setString(5, entity.getOwner().getCnpjOrCpf());
    }

    @Override
    protected void setKeyToPreparedStatement(@NotNull String key, @NotNull PreparedStatement stmt) throws SQLException {
        stmt.setString(1, key);
    }

    @Override
    protected BankAccount getEntityFromResultSet(@NotNull ResultSet rs) throws SQLException {
        BankAccount account = new BankAccount(
                rs.getString("bankName"),
                rs.getInt("bankNumber"),
                rs.getInt("agency"),
                rs.getInt("clientCnpjOrCpf"));
        return account;
    }
}