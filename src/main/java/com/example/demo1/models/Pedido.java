package com.example.demo1.models;

import java.time.LocalDateTime;

public record Pedido(long ID, long usuarioID, LocalDateTime fecha, double total, String estado) {

}
