package com.parque_industrial.dto.auth;

public record RegisterRequest(String nombre, String apellido, String email, String nombreUsuario, String password, String confirmarPassword, String cuitUsuario, String razonSocialEmpresa, String cuitEmpresa) {}