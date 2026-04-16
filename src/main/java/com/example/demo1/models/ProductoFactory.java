package com.example.demo1.models;

import java.time.LocalDateTime;

public class ProductoFactory implements Producto {
    private long ID;
    private String nombre;
    private String descripcion;
    private double precio;
    private LocalDateTime fecha;

    public ProductoFactory(String nombre, String descripcion, double precio) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.fecha = LocalDateTime.now();
    }

    public ProductoFactory(ProductoFactory productoFactory) {
        this.ID = productoFactory.ID;
        this.nombre = productoFactory.nombre;
        this.descripcion = productoFactory.descripcion;
        this.precio = productoFactory.precio;
        this.fecha = productoFactory.fecha;
    }

    public String nombre() {
        return nombre;
    }

    public String descripcion() {
        return descripcion;
    }

    public double precio() {
        return precio;
    }

    public LocalDateTime fecha() {
        return fecha;
    }
}
