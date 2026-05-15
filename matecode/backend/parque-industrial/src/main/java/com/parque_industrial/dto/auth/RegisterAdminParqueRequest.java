package com.parque_industrial.dto.auth;


public record RegisterAdminParqueRequest (String nombre, String apellido, String email, String nombreUsuario, String password, String confirmarPassword, String cuitUsuario){
}

