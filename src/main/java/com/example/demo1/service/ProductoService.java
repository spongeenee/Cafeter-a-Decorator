package com.example.demo1.service;

import com.example.demo1.database.ProductoDAO;
import com.example.demo1.models.ProductoFactory;

import java.sql.SQLException;
import java.util.List;

public class ProductoService extends GenericService<ProductoFactory> {
    public ProductoService() {
        try {
            this.DAO = new ProductoDAO();
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

        element = new ProductoFactory(
                nombre, descripcion, precio);
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

    public List<ProductoFactory> findAll() {
        try {
            return DAO.findAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

