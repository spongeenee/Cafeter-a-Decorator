package com.example.demo1.models;

public class ExtraDecorator implements Producto {
    private Producto decorated;
    private String nombre;
    private double precio;
    private boolean estado;

    public ExtraDecorator(Producto decorated, String nombre, double precio) {
        this.decorated = decorated;
        this.nombre = nombre;
        this.precio = precio;
        estado = true;
    }

    @Override
    public double precio() {
        return decorated.precio() + precio;
    }

    @Override
    public String nombre() {
        return decorated.nombre() + " con " + nombre;
    }
}
