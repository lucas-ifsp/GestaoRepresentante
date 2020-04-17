package br.com.lucas.representante.persistence.dao;

import br.com.lucas.representante.model.Contact;
import br.com.lucas.representante.persistence.utils.AbstractTemplateSqlDAO;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class DAOContact extends AbstractTemplateSqlDAO<Contact, String> {

    @Override
    protected String createSaveSql() {
        return "INSERT INTO Contact(name, position, phone, cpf, " +
                "rg, birthday, clientCnpjOrCpf, email) VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
    }

    @Override
    protected String createUpdateSql() {
        return "UPDATE Contact SET name = ?, position = ?, phone = ?, cpf = ?, " +
                "rg = ?, birthday = ?, clientCnpjOrCpf = ? WHERE email = ?";
    }

    @Override
    protected String createDeleteSql() {
        return "DELETE FROM Contact WHERE email = ?";
    }

    @Override
    protected String createSelectSql(@NotNull String condition) {
        return "SELECT * FROM Contact WHERE email = ?";
    }

    @Override
    protected String createSelectAllSql() {
        return "SELECT * FROM Contact";
    }

    @Override
    protected void setEntityToPreparedStatement(@NotNull Contact entity, @NotNull PreparedStatement stmt) throws SQLException {
        stmt.setString(1, entity.getName());
        stmt.setString(2, entity.getPosition());
        stmt.setString(3, entity.getPhone());
        stmt.setString(4, entity.getCpf());
        stmt.setString(5, entity.getRg());
        stmt.setString(6, entity.getBirthday().toString());
        stmt.setString(7, entity.getCompany().getCnpjOrCpf());
        stmt.setString(8, entity.getEmail());
    }

    @Override
    protected void setKeyToPreparedStatement(@NotNull String key, @NotNull PreparedStatement stmt) throws SQLException {
        stmt.setString(1, key);
    }

    @Override
    protected Contact getEntityFromResultSet(@NotNull ResultSet rs) throws SQLException {
        Contact contact = new Contact(
                rs.getString("name"),
                rs.getString("email"),
                rs.getString("position"),
                rs.getString("phone"),
                rs.getString("cpf"),
                rs.getString("rg"),
                LocalDate.parse(rs.getString("birthday")));
        return contact;
    }
}
