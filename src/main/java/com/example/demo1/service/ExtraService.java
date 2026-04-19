package com.example.demo1.service;

import com.example.demo1.database.ExtraDAO;
import com.example.demo1.models.ExtraDecorator;

import java.sql.SQLException;
import java.util.List;

public class ExtraService extends GenericService<ExtraDecorator> {
    public ExtraService() {
        try {
            this.DAO = new ExtraDAO();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean register(String nombre, String descripcion, double precio) {
        if (nombre.isBlank())
            return false;
        if (String.valueOf(precio).isBlank())
            return false;
        if (descripcion.isBlank())
            descripcion = "";

        element = new ExtraDecorator(nombre, precio);
        try {
            return DAO.save(element);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean delete(String nombre) {
        try {
            return DAO.delete(find(nombre).getID());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean update(String nombre) {
        element = find(nombre);
        return false;
    }

    public void activate() { element.setActivo(true); }

    public void deactivate() { element.setActivo(false); }

    public List<ExtraDecorator> findAll() {
        try {
            return DAO.findAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
