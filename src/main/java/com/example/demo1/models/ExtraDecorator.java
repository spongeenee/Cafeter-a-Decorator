package com.example.demo1.models;

public class ExtraDecorator implements Producto {
    private Producto decorated;
    private long ID;
    private String nombre;
    private double precio;
    private boolean activo;

    public ExtraDecorator(Producto decorated, ExtraDecorator decorator) {
        this.decorated = decorated;
        this.nombre = decorator.getNombre();
        this.precio = decorator.getPrecio();
        activo = true;
    }

    public ExtraDecorator(String nombre, double precio) {
        this.nombre = nombre;
        this.precio = precio;
    }

    @Override
    public double precio() {
        return decorated.precio() + precio;
    }

    @Override
    public String nombre() {
        return decorated.nombre() + " con " + nombre;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public long getID() {
        return ID;
    }

    public String getNombre() {
        return nombre;
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
