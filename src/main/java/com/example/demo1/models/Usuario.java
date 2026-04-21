package com.example.demo1.models;

import java.time.LocalDateTime;

public record Usuario(long idUsuario, String nombre, String email, String password, LocalDateTime fechaRegistro) {
}
