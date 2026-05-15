package com.parque_industrial.dto.auth;

public record LoginResponse(
                String nombreUsuario,
                String nombre,
                String apellido,
                String email,
                String cuit,
                String rol,
                String contrasena,
                EmpresaResponse empresa,
                String token) {
}