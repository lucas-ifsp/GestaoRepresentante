package br.com.lucas.representante.persistence.utils;

import org.jetbrains.annotations.NotNull;

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
    protected abstract String createSelectSql();
    protected abstract String createSelectAllSql();
    protected abstract String createSelectBySql(String field);

    protected abstract void setEntityToPreparedStatement(@NotNull T entity, @NotNull PreparedStatement stmt) throws SQLException;
    protected abstract void setKeyToPreparedStatement(@NotNull K key, @NotNull PreparedStatement stmt) throws SQLException;
    protected abstract void setFilterToPreparedStatement(@NotNull Object filter, @NotNull PreparedStatement stmt) throws SQLException;
    protected abstract T getEntityFromResultSet(@NotNull ResultSet rs) throws SQLException;
    protected abstract K getEntityKey(@NotNull T entity);

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
    public void saveOrUpdate(T entity){
        Optional<T> result = select(getEntityKey(entity));
        if(result.isPresent())
            update(entity);
        else
            save(entity);
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
    public List<T> selectBy(String field, Object value) {
        try {
            List<T> entities = createAndExecuteQuery(field, value);
            return entities;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    private List<T> createAndExecuteQuery(String field, Object value) throws SQLException {
        sql = createProperSQL(field, value);
        List<T> result = executeQuery(field, value);
        return result;
    }

    protected String createProperSQL(String field, Object value) {
        if(isSelectAllQuery(field, value))
            return createSelectAllSql();
        if(isSelectQuery(field, value))
            return createSelectSql();
        if(isSelectByQuery(field, value))
            return createSelectBySql(field);
        throw new IllegalArgumentException("Parâmetros incompatíveis. Não foi possível criar SQL.");
    }

    private boolean isSelectAllQuery(String field, Object value){
        return field == null && value == null;
    }

    private boolean isSelectByQuery(String field, Object value){
        return field != null && value != null;
    }

    private boolean isSelectQuery(String field, Object value){
        return field == null && value != null;
    }

    private List<T> executeQuery(String field, Object value) throws SQLException {
        try(PreparedStatement stmt = ConnectionFactory.createPreparedStatement(sql)) {
            setFilterIfExists(field, value, stmt);
            ResultSet rs = stmt.executeQuery();
            List<T> resultList = getEntitiesFromResultSet(rs);
            loadNestedEntitiesHook(resultList);
            return resultList;
        }
    }

    protected void setFilterIfExists(String field, Object value, @NotNull PreparedStatement stmt) throws SQLException {
        if(isSelectAllQuery(field, value))
            return;
        if(isSelectQuery(field, value)) {
            setKeyToPreparedStatement((K) value, stmt);
            return;
        }
        if(isSelectByQuery(field, value)) {
            setFilterToPreparedStatement(value, stmt);
            return;
        }
        throw new IllegalArgumentException("Parâmetros incompatíveis. Não foi possível ajustar os valores da consulta.");
    }

    protected void loadNestedEntitiesHook(List<T> entities) throws SQLException{}

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
        List<T> entities = selectBy(null, key);;
        Optional<T> first = getFirstEntity(entities);
        return first;
    }

    private Optional<T> getFirstEntity(@NotNull List<T> list) {
        return list.stream().findFirst();
    }

    @Override
    public List<T> selectAll() {
        return selectBy(null, null);
    }
}
