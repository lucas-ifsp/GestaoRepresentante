package br.com.lucas.representante.persistence.utils;

import java.util.List;
import java.util.Optional;

public interface DAO <T, K> {
    void save(T entity);
    void update(T entity);
    void saveOrUpdate(T entity);
    void delete(K key);
    Optional<T> select(K key);
    List<T> selectAll();
    List<T> selectBy(String field, Object value);
}
