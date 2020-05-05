package br.com.lucas.representante.persistence.dao;

import br.com.lucas.representante.model.entities.Address;
import br.com.lucas.representante.persistence.utils.AbstractTemplateSqlDAO;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DAOAddress extends AbstractTemplateSqlDAO<Address, String> {

    @Override
    protected String createSaveSql() {
        return "INSERT INTO Address(street, number, area, city, state, " +
                "zipCode, pointOfReference, client) " +
                "VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
    }

    @Override
    protected String createUpdateSql() {
        return "UPDATE Address SET street = ?, number = ?, area = ?, city = ?, state = ?, " +
                "zipCode = ?, pointOfReference = ? WHERE client = ?";
    }

    @Override
    protected String createDeleteSql() {
        return "DELETE FROM Address WHERE client = ?";
    }

    @Override
    protected String createSelectSql() {
        return "SELECT * FROM Address WHERE client = ?";
    }

    @Override
    protected String createSelectAllSql() {
        return "SELECT * FROM Address";
    }

    @Override
    protected String createSelectBySql(String field) {
        return "SELECT * FROM Address WHERE " + field + " = ?";
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
    protected void setFilterToPreparedStatement(@NotNull Object filter, @NotNull PreparedStatement stmt) throws SQLException {
        if(filter instanceof  String)
            stmt.setString(1, filter.toString());
        else if(filter instanceof Integer)
            stmt.setInt(1, (Integer)filter);
        else
            throw new SQLException("O tipo do filtro fornecido não é suportado pela consulta");
    }

    @Override
    protected Address getEntityFromResultSet(@NotNull ResultSet rs) throws SQLException {
        Address address = new Address(
                rs.getString("street"),
                rs.getInt("number"),
                rs.getString("area"),
                rs.getString("zipCode"),
                rs.getString("city"),
                rs.getString("state"),
                rs.getString("pointOfReference"));
        return address;
    }

    @Override
    protected String getEntityKey(@NotNull Address entity) {
        return entity.getResident().getCnpjOrCpf();
    }
}
