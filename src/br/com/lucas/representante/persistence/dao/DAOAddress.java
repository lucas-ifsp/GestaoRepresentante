package br.com.lucas.representante.persistence.dao;

import br.com.lucas.representante.model.Address;
import br.com.lucas.representante.persistence.utils.AbstractTemplateSqlDAO;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DAOAddress extends AbstractTemplateSqlDAO<Address, String> {

    @Override
    protected String createSaveSql() {
        return "INSERT INTO Address(street, number, area, city, state, " +
                "zipCode, pointOfReference, clientCnpjOrCpf) " +
                "VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
    }

    @Override
    protected String createUpdateSql() {
        return "UPDATE Address SET street = ?, number = ?, area = ?, city = ?, state = ?, " +
                "zipCode = ?, pointOfReference = ? WHERE clientCnpjOrCpf = ?";
    }

    @Override
    protected String createDeleteSql() {
        return "DELETE FROM Address WHERE clientCnpjOrCpf = ?";
    }

    @Override
    protected String createSelectSql(@Nullable String condition) {
        return "SELECT * FROM Address WHERE clientCnpjOrCpf = ?";
    }

    @Override
    protected String createSelectAllSql() {
        return "SELECT * FROM Address";
    }

    @Override
    protected void setEntityToPreparedStatement(@NotNull Address entity, @NotNull PreparedStatement stmt) throws SQLException {
        stmt.setString(1, entity.getStreet());
        stmt.setInt(2, entity.getNumber());
        stmt.setString(3, entity.getArea());
        stmt.setString(4, entity.getCity());
        stmt.setString(5, entity.getState());
        stmt.setString(6, entity.getZipCode());
        stmt.setString(7, entity.getPointOfReference());
        stmt.setString(8, entity.getResident().getCnpjOrCpf());
    }

    @Override
    protected void setKeyToPreparedStatement(@NotNull String key, @NotNull PreparedStatement stmt) throws SQLException {
        stmt.setString(1, key);
    }

    @Override
    protected Address getEntityFromResultSet(@NotNull ResultSet rs) throws SQLException {
        Address address = new Address(
                rs.getString("street"),
                rs.getInt("number"),
                rs.getString("area"),
                rs.getString("city"),
                rs.getString("state"),
                rs.getString("zipCode"),
                rs.getString("pointOfReference"));
        return address;
    }
}
