package br.com.lucas.representante.persistence.utils;

import br.com.lucas.representante.persistence.dao.DAO;
import org.jetbrains.annotations.NotNull;

import javax.swing.text.html.parser.Entity;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public abstract class AbstractTemplateSqlDAO<T, K> implements DAO<T, K> {

    protected abstract String createSaveSql();
    protected abstract String createUpdateSql();
    protected abstract String createDeleteSql();
    protected abstract String createSelectSql(K condition);
    protected abstract String createSelectAllSql();

    protected abstract void setEntityToPreparedStatement(@NotNull T entity, @NotNull PreparedStatement stmt) throws SQLException;
    protected abstract void setKeyToPreparedStatement(@NotNull K key, @NotNull PreparedStatement stmt) throws SQLException;
    protected abstract T getEntityFromResultSet(@NotNull ResultSet rs) throws SQLException;

    private String sql;

    @Override
    public void save(T entity) {
        sql = createSaveSql();
        executeSqlUsingEntity(entity);
    }

    private void executeSqlUsingEntity(T entity) {
        try (PreparedStatement stmt = ConnectionFactory.createPreparedStatement(sql)) {
            setEntityToPreparedStatement(entity, stmt);
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(T entity) {
        sql = createUpdateSql();
        executeSqlUsingEntity(entity);
    }

    @Override
    public void delete(K key) {
        sql = createDeleteSql();
        executeSqlUsingKey(key);
    }

    private void executeSqlUsingKey(K key) {
        try(PreparedStatement stmt = ConnectionFactory.createPreparedStatement(sql)){
            setKeyToPreparedStatement(key, stmt);
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<T> selectAll() {
        sql = createSelectAllSql();
        try {
            List<T> entities = executeSqlQuery();
            return entities;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    private List<T> executeSqlQuery() throws SQLException {
        return executeSqlQuery(null);
    }

    private List<T> executeSqlQuery(K key) throws SQLException {
        try(PreparedStatement stmt = ConnectionFactory.createPreparedStatement(sql)) {
            if(key != null)
                setKeyToPreparedStatement(key, stmt);
            ResultSet rs = stmt.executeQuery();
            List<T> resultList = getEntitiesFromResultSet(rs);
            return resultList;
        }
    }

    private List<T> getEntitiesFromResultSet(@NotNull ResultSet rs) throws SQLException {
        List<T> result = new ArrayList<>();
        while (rs.next()) {
            T address = getEntityFromResultSet(rs);
            result.add(address);
        }
        return result;
    }

    @Override
    public Optional<T> select(K key) {
        sql = createSelectSql(key);
        try {
            List<T> entities = executeSqlQuery(key);
            Optional<T> first = getFirstEntity(entities);
            return first;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    private Optional<T> getFirstEntity(@NotNull List<T> list) {
        return list.stream().findFirst();
    }
}
