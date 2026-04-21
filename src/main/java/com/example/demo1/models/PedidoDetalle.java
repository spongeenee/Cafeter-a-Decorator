package com.example.demo1.models;

public record PedidoDetalle(long idDetalle, long idPedido, long idProducto, int cantidad, double precioBase) {
}
