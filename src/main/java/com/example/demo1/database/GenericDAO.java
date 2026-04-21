package com.example.demo1.database;

import com.example.demo1.singleton.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public abstract class GenericDAO<T> {
    protected final Connection connection;

    public GenericDAO() throws SQLException {
        this.connection = DatabaseConnection.getConnection();
    }

    public abstract boolean save(T entity) throws SQLException;

    public abstract boolean update(T entity) throws SQLException;

    public abstract boolean delete(Long ID) throws SQLException;

    public abstract T findByID(Long ID) throws SQLException;

    public abstract List<T> findAll() throws SQLException;

    protected abstract T mapResultSetToEntity(ResultSet rs) throws SQLException;

    public abstract List<T> findByNombre(String nombre) throws SQLException;
}
