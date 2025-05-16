package com.example.exambank.dao;

import java.sql.SQLException;
import java.util.List;

public interface Dao<T, K> {
    T findById(K id) throws SQLException;
    List<T> findAll() throws SQLException;
    void save(T t) throws SQLException;
    void update(T t) throws SQLException;
    void delete(K id) throws SQLException;
}
