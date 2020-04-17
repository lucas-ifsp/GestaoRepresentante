package br.com.lucas.representante.persistence.dao;

import br.com.lucas.representante.model.Client;
import br.com.lucas.representante.persistence.utils.AbstractTemplateSqlDAO;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class DAOClient extends AbstractTemplateSqlDAO<Client, String> {

    @Override
    protected String createSaveSql() {
        return "INSERT INTO Client(companyName, tradeName, companyId, stateRegistration, " +
                "cityRegistration, clientSince, phone1, phone2, fax, cnpjOrCpf) " +
                "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    }

    @Override
    protected String createUpdateSql() {
        return "UPDATE Client SET companyName = ?, tradeName = ?, companyId = ?, stateRegistration = ?, " +
                "cityRegistration = ?, clientSince = ?, phone1 = ?, phone2 = ?, fax = ? WHERE cnpjOrCpf = ?";
    }

    @Override
    protected String createDeleteSql() {
        return "DELETE FROM Client WHERE cnpjOrCpf = ?";
    }

    @Override
    protected String createSelectSql(@Nullable String condition) {
        return "SELECT * FROM Client WHERE cnpjOrCpf = ?";
    }

    @Override
    protected String createSelectAllSql() {
        return "SELECT * FROM Client";
    }

    @Override
    protected void setEntityToPreparedStatement(@NotNull Client entity, @NotNull PreparedStatement stmt) throws SQLException {
        stmt.setString(1, entity.getCompanyName());
        stmt.setString(2, entity.getTradeName());
        stmt.setString(3, entity.getCompanyId());
        stmt.setString(4, entity.getStateRegistration());
        stmt.setString(5, entity.getCityRegistration());
        stmt.setString(6, entity.getClientSince().toString()); //TODO
        stmt.setString(7, entity.getPhone1());
        stmt.setString(8, entity.getPhone2());
        stmt.setString(9, entity.getFax());
        stmt.setString(10, entity.getCnpjOrCpf());
    }

    @Override
    protected void setKeyToPreparedStatement(@NotNull String key, @NotNull PreparedStatement stmt) throws SQLException {
        stmt.setString(1, key);
    }

    @Override
    protected Client getEntityFromResultSet(@NotNull ResultSet rs) throws SQLException {
        Client client = new Client(
                rs.getString("companyName"),
                rs.getString("tradeName"),
                rs.getString("companyId"),
                rs.getString("cnpjOrCpf"),
                rs.getString("stateRegistration"),
                rs.getString("cityRegistration"),
                LocalDate.parse(rs.getString("clientSince")),
                rs.getString("phone1"),
                rs.getString("phone2"),
                rs.getString("fax"));
        return client;
    }
}
