package com.example.demo1.models;

import java.time.LocalDateTime;

public class ProductoFactory implements Producto {
    private long ID;
    private String nombre;
    private String descripcion;
    private double precio;
    private boolean activo;

    public ProductoFactory(String nombre, String descripcion, double precio) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.activo = true;
    }

    public ProductoFactory(ProductoFactory productoFactory) {
        this.ID = productoFactory.ID;
        this.nombre = productoFactory.nombre;
        this.descripcion = productoFactory.descripcion;
        this.precio = productoFactory.precio;
        this.activo = productoFactory.activo;
    }

    public long ID() {
        return ID;
    }

    @Override
    public String nombre() {
        return nombre;
    }

    @Override
    public double precio() {
        return precio;
    }

    public boolean activo() { return activo; }

    public void setID(long ID) {
        this.ID = ID;
    }

    public long getID() {
        return ID;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public double getPrecio() {
        return precio;
    }

    public boolean getActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
