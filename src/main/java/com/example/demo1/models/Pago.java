package com.example.demo1.models;

import java.time.LocalDateTime;

public record Pago(long idPago, long idPedido, long idMetodo, double monto, LocalDateTime fecha, String referencia) {
}
