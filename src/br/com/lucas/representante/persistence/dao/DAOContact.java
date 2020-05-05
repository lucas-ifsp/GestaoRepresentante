package br.com.lucas.representante.persistence.dao;

import br.com.lucas.representante.model.entities.Contact;
import br.com.lucas.representante.persistence.utils.AbstractTemplateSqlDAO;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class DAOContact extends AbstractTemplateSqlDAO<Contact, String> {

    @Override
    protected String createSaveSql() {
        return "INSERT INTO Contact(name, position, phone, cpf, " +
                "rg, birthday, client, email) VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
    }

    @Override
    protected String createUpdateSql() {
        return "UPDATE Contact SET name = ?, position = ?, phone = ?, cpf = ?, " +
                "rg = ?, birthday = ?, client = ? WHERE email = ?";
    }

    @Override
    protected String createDeleteSql() {
        return "DELETE FROM Contact WHERE email = ?";
    }

    @Override
    protected String createSelectSql() {
        return "SELECT * FROM Contact WHERE email = ?";
    }

    @Override
    protected String createSelectAllSql() {
        return "SELECT * FROM Contact";
    }

    @Override
    protected String createSelectBySql(String field) {
        return "SELECT * FROM Contact WHERE "+ field +" = ?";
    }

    @Override
    protected void setEntityToPreparedStatement(@NotNull Contact entity, @NotNull PreparedStatement stmt) throws SQLException {
        LocalDate birthday = entity.getBirthday();
        stmt.setString(1, entity.getName());
        stmt.setString(2, entity.getPosition());
        stmt.setString(3, entity.getPhone());
        stmt.setString(4, entity.getCpf());
        stmt.setString(5, entity.getRg());
        stmt.setString(6, birthday != null? birthday.toString(): null);
        stmt.setString(7, entity.getCompany().getCnpjOrCpf());
        stmt.setString(8, entity.getEmail());
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
    protected Contact getEntityFromResultSet(@NotNull ResultSet rs) throws SQLException {
        String dateString = rs.getString("birthday");
        Contact contact = new Contact(
                rs.getString("name"),
                rs.getString("email"),
                rs.getString("position"),
                rs.getString("phone"),
                rs.getString("cpf"),
                rs.getString("rg"),
                dateString != null ? LocalDate.parse(dateString) : null);
        return contact;
    }

    @Override
    protected String getEntityKey(@NotNull Contact entity) {
        return entity.getEmail();
    }
}
