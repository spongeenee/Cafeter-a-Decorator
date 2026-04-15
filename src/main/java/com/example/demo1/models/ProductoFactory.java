package com.example.demo1.models;

import java.time.LocalDateTime;

public class ProductoFactory implements Producto {
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

    public String nombre() {
        return nombre;
    }

    public double precio() {
        return precio;
    }
}
