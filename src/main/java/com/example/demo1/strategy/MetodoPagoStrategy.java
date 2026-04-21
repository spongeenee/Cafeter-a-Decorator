package com.example.demo1.strategy;

import com.example.demo1.models.MetodoPago;
import com.example.demo1.service.MetodoPagoService;

import java.util.Optional;

public class MetodoPagoStrategy {
    public MetodoPago metodoPago(MetodoPagoService service, String identifier) {
        return service.find(identifier);
    }
}
