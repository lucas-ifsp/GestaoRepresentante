package br.com.lucas.representante.persistence.dao;

import br.com.lucas.representante.model.entities.History;
import br.com.lucas.representante.persistence.utils.AbstractTemplateSqlDAO;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class DAOHistory extends AbstractTemplateSqlDAO<History, Integer> {

    @Override
    protected String createSaveSql() {
        return "INSERT INTO History(title, description, date, " +
                "client) VALUES(?, ?, ?, ?)";
    }

    @Override
    protected String createUpdateSql() {
        return "UPDATE History SET title = ?, description = ?, " +
                "date = ?, client = ? WHERE id = ?";
    }

    @Override
    protected String createDeleteSql() {
        return "DELETE FROM History WHERE id = ?";
    }

    @Override
    protected String createSelectSql() {
        return "SELECT * FROM History WHERE id = ?";
    }

    @Override
    protected String createSelectAllSql() {
        return "SELECT * FROM History";
    }

    @Override
    protected String createSelectBySql(String field) {
        return "SELECT * FROM History WHERE "+ field +" = ?";
    }

    @Override
    protected void setEntityToPreparedStatement(@NotNull History entity, @NotNull PreparedStatement stmt) throws SQLException {
        stmt.setString(1, entity.getTitle());
        stmt.setString(2, entity.getDescription());
        stmt.setString(3, entity.getDate().toString());
        stmt.setString(4, entity.getClient().getCnpjOrCpf());
    }

    @Override
    protected void setKeyToPreparedStatement(@NotNull Integer key, @NotNull PreparedStatement stmt) throws SQLException {
        stmt.setInt(1, key);
    }

    @Override
    protected void setFilterToPreparedStatement(@NotNull Object filter, @NotNull PreparedStatement stmt) throws SQLException {
        stmt.setString(1, filter.toString());
    }

    @Override
    protected History getEntityFromResultSet(@NotNull ResultSet rs) throws SQLException {
        History entity = new History(
                rs.getInt("id"),
                rs.getString("title"),
                rs.getString("description"),
                LocalDate.parse(rs.getString("date")));
        return entity;
    }

    @Override
    protected Integer getEntityKey(@NotNull History entity) {
        return entity.getId();
    }
}
