package com.example.demo1.models;

public record MetodoPago(long idMetodo, String nombre) {
    @Override
    public String toString() {
        return nombre;
    }
}
