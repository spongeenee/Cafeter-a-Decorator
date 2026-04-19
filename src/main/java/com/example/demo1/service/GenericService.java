package com.example.demo1.service;

import com.example.demo1.database.GenericDAO;

import java.util.List;

public class GenericService<T>{
    protected T element;
    protected GenericDAO<T> DAO;

    public T find(String nombre) {
        List<T> clienteList = null;
        try {
            clienteList = DAO.findByNombre(nombre);
        } catch (java.sql.SQLException throwable) {
            throw new RuntimeException(throwable);
        }
        return clienteList.getFirst();
    }
}
