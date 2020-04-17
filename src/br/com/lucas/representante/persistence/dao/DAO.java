package br.com.lucas.representante.persistence.dao;

import java.util.List;
import java.util.Optional;

public interface DAO <T, K> {
    void save(T entity);
    void update(T entity);
    void delete(K key);
    Optional<T> select(K key);
    List<T> selectAll();
}
