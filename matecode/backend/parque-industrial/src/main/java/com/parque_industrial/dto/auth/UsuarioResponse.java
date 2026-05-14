package com.parque_industrial.dto.auth;

public record UsuarioResponse(
        String nombreUsuario,
        String nombre,
        String apellido,
        String email,
        String cuit
) {}